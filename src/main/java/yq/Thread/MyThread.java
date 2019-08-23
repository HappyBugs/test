package yq.Thread;


import lombok.Data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//测试多线程
@Data
public class MyThread {

    //volatile可以解决从排序，和线程可见性
    private volatile String userSex = null;
    private volatile String userName = null;
    //用于计数
    private volatile Integer number = 0;
    //是否创建
    private volatile Boolean isFalg = false;
    private Lock lock = new ReentrantLock();


    static class ProducerThread extends Thread {

        private Condition condition;

        private MyThread myThread;

        public ProducerThread(MyThread myThread, Condition condition) {
            this.myThread = myThread;
            this.condition = condition;
        }

        @Override
        public void run() {
            int index = 0;
            while (true) {
                try {
                    myThread.getLock().lock();
                    //如果已经生产好了，还没人消费，那么我们这个线程就等待，先不进行生产
                    if (myThread.getIsFalg()) {
                        condition.await();
                    }
                    Thread.sleep(50);
                    if (index % 2 == 0) {
                        myThread.setUserName("小红");
                        myThread.setUserSex("女");
                        myThread.setNumber(index);
                    } else {
                        myThread.setUserName("张三");
                        myThread.setUserSex("男");
                        myThread.setNumber(index);
                    }
                    ++index;
                    //表示我们已经生产好了
                    myThread.setIsFalg(true);
                    //放开阻塞的线程
                    condition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    myThread.getLock().unlock();
                }
            }
        }

    }

    static class ConsumerThread extends Thread {

        private Condition condition;

        private MyThread myThread;

        public ConsumerThread(MyThread myThread, Condition condition) {
            this.myThread = myThread;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    myThread.getLock().lock();
                    //如果还没有生产好，我们就阻塞当前线程
                    if (!myThread.getIsFalg()) {
                        condition.await();
                    }
                    Thread.sleep(30);
                    System.out.println(Thread.currentThread().getName() + myThread.getUserName() +
                            "--------" + myThread.getUserSex() + "---------" + myThread.getNumber());
                    myThread.setIsFalg(false);
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    myThread.getLock().unlock();
                }
            }
        }
    }


    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        Condition condition = myThread.lock.newCondition();
        new ProducerThread(myThread,condition).start();
        new ConsumerThread(myThread,condition).start();
    }
}

