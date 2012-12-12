import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadGroupTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new Thread() {
			@Override
			public void run() {
				try {
					new RunThread().runThread();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}; // <- .start();
		
		// Walk up all the way to the root thread group
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = rootGroup.getParent()) != null) {
            rootGroup = parent;
            if (rootGroup.getName().equals("areaCocde1"))
            	break;
        }

        // <- listThreads(rootGroup, " ");
        
        ExecutorService pool = Executors.newSingleThreadExecutor(ThreadFactoryTest.getInstance());
        
        ThreadGroup tgac1 = new ThreadGroup("areaCode1");
        // <- ThreadFactoryTest.getInstance().threadGroup(new ThreadGroup("areaCode1")).daemon(false).newThread(new ThisThread(10)).start();
        for (int i = 0; i < 2; i++) {
	        new Thread() {
				@Override
				public void run() {
					ThreadFactoryTest.getInstance().add("areaCode1", "123456").daemon(false).newThread("areaCode1", "123456", 7).spinOfExecutorService("areaCode1");
					ThreadFactoryTest.getInstance().add("areaCode2", "345678").daemon(false).newThread("areaCode2", "345678", 5).spinOfExecutorService("areaCode2");
				}
			}.start();
        }
		// <- ThreadFactoryTest.getInstance().stop("areaCode2");
		// <- ThreadFactoryTest.getInstance().add("areaCode2", "345678").daemon(false).newThread("areaCode2", "345678", 5).spinOfExecutorService("areaCode2");
        // <- ThreadFactoryTest.getInstance().add("areaCode2", "345678").daemon(false).newThread("areaCode2", "345678", 3).spin("areaCode2");
        // <- ThreadFactoryTest.getInstance().add("areaCode1", "456789").daemon(false).newThread("areaCode1", "456789", 6).spin("areaCode1");
        // <- ThreadFactoryTest.getInstance().threadGroup(new ThreadGroup("areaCode2")).daemon(false).newThread("areaCode2", "123456").start();
        // <- Thread.sleep(2000);
        // <- ThreadFactoryTest.getInstance().threadGroup(new ThreadGroup("areaCode1")).daemon(false).newThread("areaCode1", "234567").start();
		System.out.println("done..");
	}

	   // List all threads and recursively list all subgroup
    public static void listThreads(ThreadGroup group, String indent) {
        System.out.println(indent + "Group[" + group.getName() + 
                        ":" + group.getClass()+"]");
        int nt = group.activeCount();
        Thread[] threads = new Thread[nt*2 + 10]; //nt is not accurate
        nt = group.enumerate(threads, false);

        // List every thread in the group
        for (int i=0; i<nt; i++) {
            Thread t = threads[i];
            if (t.getName().equals("12345") && t.isAlive()) {
            	; // <- t.interrupt();
            }
            	
            System.out.println(indent + "  Thread[" + t.getName() 
                        + ":" + t.getClass() + "]");
        }

        // Recursively list all subgroups
        int ng = group.activeGroupCount();
        ThreadGroup[] groups = new ThreadGroup[ng*2 + 10];
        ng = group.enumerate(groups, false);

        for (int i=0; i<ng; i++) {
            listThreads(groups[i], indent + "  ");
        }
    }

    static class RunThread {
    	
    	public RunThread(){
    	}
    	
    	public void runThread() throws InterruptedException {
    		ThreadGroup threadGroup = new ThreadGroup("areaCode1");
    		for (int i = 0; i < 1; i++) {
    			ThisThread thisThread = new ThisThread(threadGroup, "12345", 10);
    			thisThread.start();
    			thisThread.join();
    			System.out.println("Done..");
    		}
    	}
    }
    
    static class ThisThread extends Thread {
    	
    	private int relapse;
    	
    	public ThisThread(ThreadGroup threadGroup, String programKey, int relapse) {
    		super(threadGroup, programKey);
    		this.relapse = relapse;
    	}
    	
    	public ThisThread(int relapse) {
    		this.relapse = relapse;
    	}
    	
		@Override
		public void run() {
			int cnt = 0;
			while(cnt < relapse)
				try {
					ThisThread.sleep(1000);
					System.out.println(cnt);
					cnt++;
				} catch (InterruptedException e) {
					e.printStackTrace(); // <- cnt = relapse;
				}
		}
    	
    }
}
