import java.lang.Thread.UncaughtExceptionHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryTest implements ThreadFactory{
	// <- List<ThreadGroup> tgl = new ArrayList<ThreadGroup>();
	Map<ThreadGroup, Queue<VjPlaylistWithThread>> map = new HashMap<ThreadGroup, Queue<VjPlaylistWithThread>>();
	Map<String, ExecutorService> esMap = new HashMap<String, ExecutorService>();
	// @Autowired
	// <- ChatService chatService
	
	//thread properties
    long stackSize;
    String pattern;
    ClassLoader ccl;
    ThreadGroup group;
    int priority;
    UncaughtExceptionHandler exceptionHandler;
    boolean daemon;

    private boolean configured;

    private boolean wrapRunnable;//if acc is present wrap or keep it
    protected final AccessControlContext acc;

    //thread creation counter
    protected final AtomicLong counter = new AtomicLong();

    private static class ThreadFactoryTestHolder {
    	private static final ThreadFactoryTest instance = new ThreadFactoryTest();
    }
    
    public static ThreadFactoryTest getInstance() {
    	return ThreadFactoryTestHolder.instance;
    }

    private ThreadFactoryTest() {        
		final Thread t = Thread.currentThread();
		ClassLoader loader;
		AccessControlContext acc = null;
		try {
			loader = t.getContextClassLoader();
			if (System.getSecurityManager() != null) {
				acc = AccessController.getContext();// keep current permissions
				acc.checkPermission(new RuntimePermission("setContextClassLoader"));
			}
		} catch (SecurityException _skip) {
			// no permission
			loader = null;
			acc = null;
		}

		this.ccl = loader;
		this.acc = acc;
		this.priority = t.getPriority();
		this.daemon = true;// Executors have it false by default

		this.wrapRunnable = true;// by default wrap if acc is present
									// (+SecurityManager)

		// default pattern - caller className
		StackTraceElement[] stack = new Exception().getStackTrace();
		pattern(stack.length > 1 ? getOuterClassName(stack[1].getClassName()) : "ThreadFactoryX", true);
    }
    
    public ThreadFactoryTest add(ThreadGroup threadGroup, String str) {
    	assertConfigurable();
    	if (!this.map.containsKey(threadGroup))		this.map.put(threadGroup, null);
    	if (this.map.get(threadGroup) == null) {
    		this.map.put(threadGroup, new LinkedList<VjPlaylistWithThread>());
    	}
    	
    	VjPlaylistWithThread vwt = new VjPlaylistWithThread(str, null);
    	
    	this.map.get(threadGroup).add(vwt);
    	return this;
    }
    
    public ThreadFactoryTest add(String name, String str) {
    	assertConfigurable();
    	Iterator<ThreadGroup> iterator = this.map.keySet().iterator();
    	ThreadGroup threadGroup = null;
    	while (iterator.hasNext()) {
    		ThreadGroup th = iterator.next();
    		if (th.getName().equals(name)) {
    			threadGroup = th;
    			break;
    		}
    	}
    	if (threadGroup == null)	this.map.put(threadGroup = new ThreadGroup(name), null);
    	if (this.map.get(threadGroup) == null) {
    		this.map.put(threadGroup, new LinkedList<VjPlaylistWithThread>());
    	}
    	
    	/* <-
    	VjPlaylistWithThread vwt = new VjPlaylistWithThread(str, null);
    	this.map.get(threadGroup).add(vwt);
    	*/
    	
    	return this;
    	
    }
    
    public ThreadFactory finishConfig(){
        configured = true;
        counter.addAndGet(0);//write fence "w/o" volatile
        return this;
    }

    public long getCreatedThreadsCount(){
        return counter.get();
    }

    protected void assertConfigurable(){
        if (configured)
            throw new IllegalStateException("already configured");
    }

    private static String getOuterClassName(String className){
        int idx = className.lastIndexOf('.')+1;
        className = className.substring(idx);//remove package
        idx = className.indexOf('$');
        if (idx<=0){
            return className;//handle classes starting w/ $
        }       
        return className.substring(0,idx);//assume inner class

    }

    @Override
    public Thread newThread(Runnable r) {
        configured = true;
        final Thread t = new Thread(group, wrapRunnable(r), composeName(r), stackSize);
        t.setPriority(priority);
        t.setDaemon(daemon);
        t.setUncaughtExceptionHandler(exceptionHandler);//securityException only if in the main group, shall be safe here

        applyCCL(t);
        return t;
    }
    
    public ThreadFactoryTest newThread(String name, String str, int interval) {
        // <- final Thread t = new Thread(group, wrapRunnable(r), composeName(r), stackSize);
    	ThreadGroup tg = whereIsThreadGroupOfName(name);
        final Timer t = new Timer(tg, str, interval);
        t.setPriority(priority);
        t.setDaemon(daemon);
        t.setUncaughtExceptionHandler(exceptionHandler);//securityException only if in the main group, shall be safe here

        applyCCL(t);
        VjPlaylistWithThread vwt = new VjPlaylistWithThread(str, t);
        vwt.setInterval(interval);
    	this.map.get(tg).add(vwt);
        // <- return t;
        return this;
    }

    public ThreadFactoryTest spinOfExecutorService(String name) {
    	ThreadGroup tg = whereIsThreadGroupOfName(name);
    	Iterator<VjPlaylistWithThread> iterator = this.map.get(tg).iterator();
    	Set<Future<String>> set = new HashSet<Future<String>>(); 
    	while (iterator.hasNext()) {
    		VjPlaylistWithThread vwt = iterator.next();
    		iterator.remove();
    		if (esMap.get(name) == null)	esMap.put(name,Executors.newSingleThreadExecutor());
    		if (vwt.thread == null)		continue;
    		System.out.println(tg.getName() + " : " + vwt.getVjPlaylist());
    		// <- esMap.get(name).submit(vwt.getThread());
    		set.add(esMap.get(name).submit(vwt.getThread(), name));
    	}
    	
    	try {
    		for (Future<String> future : set) {
    			String str = future.get();
    			if (str != null)	System.out.println("Future.get() is " + str);
    			else	System.out.println("Future.get() is null.");
    		}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return this;
    }
    
    
    public void spin(String name) {
    	// TODO : <- configured = true;
    	
    	// <- for (;i < this.map.get(threadGroup).size;)
    	ThreadGroup tg = whereIsThreadGroupOfName(name);
    	Iterator<VjPlaylistWithThread> iterator = this.map.get(tg).iterator();
    	while (iterator.hasNext()) {
    		VjPlaylistWithThread vwt = iterator.next();
    		iterator.remove();
    		if (vwt.thread == null)		continue;
    		System.out.println(tg.getName() + " : " + vwt.getVjPlaylist());
    		vwt.thread.start();
    		try {
    			// <- vwt.thread.join();
				Thread.sleep(vwt.getInterval());
			} catch (InterruptedException e) {
				vwt.thread.interrupt();
			}
    		
    		
    	}
    	
    	/* <-
    	VjPlaylistWithThread vwt = this.map.get(threadGroup).poll();
    	Thread t = vwt.getThread();
    	if (t == null)	return;
    	
    	t.start();
    	try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	
    	/* TODO : <-
    	while (true) {
    	}
    	*/
    }
    
    public void stop(String name) {
    	ThreadGroup tg = whereIsThreadGroupOfName(name);
    	Iterator<VjPlaylistWithThread> iterator = this.map.get(tg).iterator();
    	while (iterator.hasNext()) {
    		VjPlaylistWithThread vwt = iterator.next();
    		if (esMap.get(name) == null)	continue;
    		if (vwt.thread == null)		continue;
    		System.out.println(tg.getName() + " : ready to shutdown");
    		esMap.get(name).shutdownNow();
    		// <- if (!esMap.get(name).isShutdown())	esMap.get(name).shutdownNow();
    	}
    }
    
    private void applyCCL(final Thread t) {
        if (ccl!=null){//use factory creator ACC for setContextClassLoader
            AccessController.doPrivileged(new PrivilegedAction<Object>(){
                @Override
                public Object run() {
                    t.setContextClassLoader(ccl);
                    return null;
                }                               
            }, acc);        
        }
    }
    private Runnable wrapRunnable(final Runnable r){
        if (acc==null || !wrapRunnable){
            return r;
        }
        Runnable result = new Runnable(){
            public void run(){
                AccessController.doPrivileged(new PrivilegedAction<Object>(){
                    @Override
                    public Object run() {
                        r.run();
                        return null;
                    }                               
                }, acc);
            }
        };
        return result;      
    }


    protected String composeName(Runnable r) {
        return String.format(pattern, counter.incrementAndGet(), System.currentTimeMillis());
    }   


    //standard setters allowing chaining, feel free to add normal setXXX    
    public ThreadFactoryTest pattern(String patten, boolean appendFormat){
        assertConfigurable();
        if (appendFormat){
            patten+=": %d @ %tF %<tT";//counter + creation time
        }
        this.pattern = patten;
        return this;
    }


    public ThreadFactoryTest daemon(boolean daemon){
        assertConfigurable();
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryTest priority(int priority){
        assertConfigurable();
        if (priority<Thread.MIN_PRIORITY || priority>Thread.MAX_PRIORITY){//check before actual creation
            throw new IllegalArgumentException("priority: "+priority);
        }
        this.priority = priority;
        return this;
    }

    public ThreadFactoryTest stackSize(long stackSize){
        assertConfigurable();
        this.stackSize = stackSize;
        return this;
    }


    public ThreadFactoryTest threadGroup(ThreadGroup group){
        assertConfigurable();
        this.group= group;
        return this;        
    }

    public ThreadFactoryTest exceptionHandler(UncaughtExceptionHandler exceptionHandler){
        assertConfigurable();
        this.exceptionHandler= exceptionHandler;
        return this;                
    }

    public ThreadFactoryTest wrapRunnable(boolean wrapRunnable){
        assertConfigurable();
        this.wrapRunnable= wrapRunnable;
        return this;                        
    }

    public ThreadFactoryTest ccl(ClassLoader ccl){
        assertConfigurable();
        this.ccl = ccl;
        return this;
    }
    
    private ThreadGroup whereIsThreadGroupOfName (String name) {
    	Iterator<ThreadGroup> iterator = this.map.keySet().iterator();
    	ThreadGroup threadGroup = null;
    	while (iterator.hasNext()) {
    		ThreadGroup th = iterator.next();
    		if (th.getName().equals(name)) {
    			threadGroup = th;
    			break;
    		}
    	}
    	
    	return threadGroup;
    }
    
    static class VjPlaylistWithThread {
    	private String vjPlaylist;
    	private Timer thread;
    	private int interval;
    	
    	public VjPlaylistWithThread(String vjPlaylist, Timer thread) {
    		this.vjPlaylist = vjPlaylist;
    		this.thread = thread;
    	}
    	
    	public VjPlaylistWithThread(String vjPlaylist, Timer thread, int interval) {
    		this.vjPlaylist = vjPlaylist;
    		this.thread = thread;
    		this.interval = interval * 1000;
    	}

		public String getVjPlaylist() {
			return vjPlaylist;
		}

		public void setVjPlaylist(String vjPlaylist) {
			this.vjPlaylist = vjPlaylist;
		}

		public Thread getThread() {
			return thread;
		}

		public void setThread(Timer thread) {
			this.thread = thread;
		}

		public int getInterval() {
			return interval;
		}

		public void setInterval(int interval) {
			this.interval = interval * 1000;
		}
    }
    
	static class Timer extends Thread {
		private int endingTime;
		private int elapsedTime = 0;
		
		public Timer(ThreadGroup threadGroup, String str, int endingTime) {
			super(threadGroup, str);
			this.endingTime = endingTime;
		}
		
		public int getEndingTime() {
			return endingTime;
		}

		public void setEndingTime(int endingTime) {
			this.endingTime = endingTime;
		}

		@Override
		public void run() {
			while (elapsedTime < endingTime)
				try {
					Timer.sleep(1000);
					elapsedTime++;
					// <- System.out.print(elapsedTime + " ");
				} catch (InterruptedException e) {
					elapsedTime = endingTime;
				}
		}
	}
}
