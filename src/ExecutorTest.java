import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorTest
{
   public static void main(String[] args) throws ExecutionException, InterruptedException
   {
	   ExecutorService executorService = Executors.newFixedThreadPool(5);
       int tasksCount = 3;
       final CountDownLatch latch = new CountDownLatch(tasksCount);
       for (int i = 0; i < tasksCount; i++ ) {
           executorService.execute(new Runnable() {
               public void run() {
                   try {
                	   int elapsedTime = 0;
                	   System.out.println("Executor Test...");
                	   while (elapsedTime < 5)
            		   try {
							Thread.sleep(1000);
							elapsedTime++;
							System.out.print(elapsedTime + " ");
						} catch (InterruptedException e) {
							elapsedTime = 5;
						}
                   } finally {
                       latch.countDown();
                   }
               }
           });
       }
       try {
           latch.await();
       } catch (InterruptedException e) {
           // todo >> handle exception
       }
       // All done!
       // Do some more work
   }
}

