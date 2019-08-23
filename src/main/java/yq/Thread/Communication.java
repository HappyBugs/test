package yq.Thread;

import lombok.Data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * java并发编程之线程之间通讯
 */
public class Communication {


    /**
     * 例子一：使用wait，notify
     */
    @Data
    static final class MyThread {

        //volatile可以解决从排序，和线程可见性
        private volatile String userSex = null;
        private volatile String userName = null;
        //用于计数
        private volatile Integer number = 0;
        //是否创建
        private volatile Boolean isFalg = false;


        static class ProducerThread extends Thread {

            private MyThread myThread;

            public ProducerThread(MyThread myThread) {
                this.myThread = myThread;
            }

            @Override
            public void run() {
                int index = 0;
                while (true) {
                    try {
                        synchronized (myThread) {
                            //如果已经生产好了，还没人消费，那么我们这个线程就等待，先不进行生产
                            if(myThread.getIsFalg()){
                                myThread.wait();
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
                            index++;
                            //表示我们已经生产好了
                            myThread.setIsFalg(true);
                            //放开阻塞的线程
                            myThread.notify();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        static class ConsumerThread extends Thread {

            private MyThread myThread;

            public ConsumerThread(MyThread myThread) {
                this.myThread = myThread;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (myThread) {
                            //如果还没有生产好，我们就阻塞当前线程
                            if(! myThread.getIsFalg()){
                                myThread.wait();
                            }
                            Thread.sleep(30);
                            System.out.println(Thread.currentThread().getName() + myThread.getUserName() +
                                    "--------" + myThread.getUserSex() + "---------" + myThread.getNumber());
                            myThread.setIsFalg(false);
                            myThread.notify();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        public static void main(String[] args) {

            MyThread myThread = new MyThread();
            new ProducerThread(myThread).start();
            new ConsumerThread(myThread).start();
        }
    }


    /**
     * 例子二：使用Condition实现生产者消费者功能
     */
    @Data
    static final class MyThread2 {

        //volatile可以解决从排序，和线程可见性
        private volatile String userSex;
        private volatile String userName;
        //用于计数
        private volatile Integer number = 0;
        //是否创建
        private volatile Boolean isFalg = false;

        private Lock lock = new ReentrantLock();

        static class ProducerThread extends Thread {

            private Condition condition;

            private MyThread2 myThread2;

            public ProducerThread(MyThread2 myThread2, Condition condition) {
                this.myThread2 = myThread2;
                this.condition = condition;
            }

            @Override
            public void run() {
                int index = 0;
                while (true) {
                    try {
                        myThread2.getLock().lock();
                        //如果已经生产好了，还没人消费，那么我们这个线程就等待，先不进行生产
                        if (myThread2.getIsFalg()) {
                            condition.await();
                        }
                        Thread.sleep(50);
                        if (index % 2 == 0) {
                            myThread2.setUserName("小红");
                            myThread2.setUserSex("女");
                            myThread2.setNumber(index);
                        } else {
                            myThread2.setUserName("张三");
                            myThread2.setUserSex("男");
                            myThread2.setNumber(index);
                        }
                        ++index;
                        //表示我们已经生产好了
                        myThread2.setIsFalg(true);
                        //放开阻塞的线程
                        condition.signal();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        myThread2.getLock().unlock();
                    }
                }
            }

        }

        static class ConsumerThread extends Thread {

            private Condition condition;

            private MyThread2 myThread2;

            public ConsumerThread(MyThread2 myThread2, Condition condition) {
                this.myThread2 = myThread2;
                this.condition = condition;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        myThread2.getLock().lock();
                        //如果还没有生产好，我们就阻塞当前线程
                        if (!myThread2.getIsFalg()) {
                            condition.await();
                        }
                        Thread.sleep(30);
                        System.out.println(Thread.currentThread().getName() + myThread2.getUserName() +
                                "--------" + myThread2.getUserSex() + "---------" + myThread2.getNumber());
                        myThread2.setIsFalg(false);
                        condition.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        myThread2.getLock().unlock();
                    }
                }
            }
        }

        public static void main(String[] args) {
            MyThread2 myThread = new MyThread2();
            Condition condition = myThread.lock.newCondition();
            new ProducerThread(myThread,condition).start();
            new ConsumerThread(myThread,condition).start();
        }
    }

}
