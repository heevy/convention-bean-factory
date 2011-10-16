package org.rosenvold.spring.convention;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Kristian Rosenvold
 */
public class ReentrantLockTest {
  
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition testSessionAvailable = lock.newCondition();

  @Test
  public void testTheLock() throws InterruptedException {
    OtherThread otherThread = new OtherThread();
           try {
             lock.lock();
             
             otherThread.start();
             
             Thread.sleep(200);
             testSessionAvailable.signal();
             Thread.sleep(100);

           } finally {
             lock.unlock();
           }

    otherThread.join();

  }
  
  
  class OtherThread extends Thread{

    OtherThread() {
      super("OtherThread");
    }

    @Override
    public void run() {
      long start = System.currentTimeMillis();
      try {
        lock.lock();
        
        Thread.sleep(400);
        //testSessionAvailable.await();

      } catch (InterruptedException e) {
      } finally {
        lock.unlock();
      }
    }
  }

}
