import java.util.*;
import java.util.concurrent.*;

public class CallableExample {

	public static class WordLengthCallable implements Callable {
		private String word;

		public WordLengthCallable(String word) {
			this.word = word;
		}

		public Integer call() {
			return Integer.valueOf(word.length());
		}
	}

	public static class WordLengthRunnable implements Runnable {
		private String word;

		public WordLengthRunnable(String word) {
			this.word = word;
		}

		public Integer call() {
			return Integer.valueOf(word.length());
		}

		@Override
		public void run() {
			try {
				System.out.println(word);
				Thread.sleep(5000);
				System.out.println(word);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

  public static void main(String args[]) throws Exception {
	  // <- go();
	  go2();
  }
  
  public static void go() throws Exception {
		ExecutorService pool = Executors.newCachedThreadPool();
		// <- ExecutorService pool = Executors.newnewFixedThreadPool(3);
		Set<Future<Integer>> set = new HashSet<Future<Integer>>();
		String[] args = { "test1", "test2" };
		for (String word : args) {
			Callable<Integer> callable = new WordLengthCallable(word);
			Future<Integer> future = pool.submit(callable);
			set.add(future);
		}
		int sum = 0;
		for (Future<Integer> future : set) {
			sum += future.get();
			System.out.printf("The sum of lengths is %s%n", sum);
		}
		pool.shutdown();
  }
  
  public static void go2() throws Exception {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		ExecutorService pool2 = Executors.newSingleThreadExecutor();
		// <- ExecutorService pool = Executors.newnewFixedThreadPool(3);
		Set<Future<Integer>> set = new HashSet<Future<Integer>>();
		String[] args = { "test1", "test2" };
		Future<Date> future = pool.submit(new WordLengthRunnable("1"), new Date());
		Future<Date> future2 = pool2.submit(new WordLengthRunnable("2"), new Date());
		pool2.submit(new WordLengthRunnable("3"), new Date());
//		for (String word : args) {
//			Runnable runnable = new WordLengthRunnable(word);
//			Future<Date> future = pool.submit(runnable, new Date());
//			System.out.println("The Date : " + (Date)future.get());
//		}
		pool.shutdown();
}
}