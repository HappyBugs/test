package yq.Thread;


import lombok.Data;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * java并发编程之队列
 */
@Data
public class MyThreadPool implements Comparable<MyThreadPool> {

    private String name;
    private String sex;
    private Integer level;

    public MyThreadPool(String name, String sex, Integer level) {
        this.name = name;
        this.sex = sex;
        this.level = level;
    }

    //设置优先级
    @Override
    public int compareTo(MyThreadPool o) {
        return this.level.compareTo(o.getLevel());
    }

    //生产者
    static final class Producer extends Thread {

        private Queue<MyThreadPool> queue;

        public Producer(Queue<MyThreadPool> queue) {
            this.queue = queue;
        }

        public void create() {
            int i = 0;
            int index = 20;
            MyThreadPool myThreadPool = null;
            while (true) {
                try {
                    Thread.sleep(1000);
                    //永远都是0 或者1 要不是小红 要不是张三
                    if (i % 2 == 0) {
                        //这里就是为了产生出不同的优先级，
                        myThreadPool = new MyThreadPool("张三", "男", --index);
                    } else {
                        //这里就是为了产生出不同的优先级，
                        myThreadPool = new MyThreadPool("小红", "女", i);
                    }
                    queue.offer(myThreadPool);
                    System.out.println("生产完毕：" + myThreadPool.toString());
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            create();
        }
    }

    //消费者
    static final class Consumer extends Thread {

        private Queue<MyThreadPool> queue;

        public Consumer(Queue<MyThreadPool> queue) {
            this.queue = queue;
        }

        public void consumer() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    MyThreadPool poll = queue.poll();
                    System.out.println("开始消费：" + poll.toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            consumer();
        }
    }

    //开始的方法
    public static void start(Queue queue) {
        new Producer(queue).start();
        try {
            //因为我们要使生产者先执行，不然获取不到值，会报错
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Consumer(queue).start();
    }

    /**
     * LinkedBlockingQueue: 无边界大小阻塞线程，说是无限大 其实也不是，只是底层给定了一个很大的默认值
     * ArrAyBlockingQueue：需要指定大小的一个阻塞队列
     */
    @Data
    static final class MyArrAyBlockingQueue {
        public static void main(String[] args) {
            ArrayBlockingQueue<MyThreadPool> arrAyBlockingQueues = new ArrayBlockingQueue<>(4);
            start(arrAyBlockingQueues);
        }
    }


    /**
     * SynchronousQueue 线程 一次只允许存入一个元素
     */
    static final class MySynchronousQueue {
        public static void main(String[] args) {
            SynchronousQueue<MyThreadPool> synchronousQueue = new SynchronousQueue<>();
            start(synchronousQueue);
        }
    }

    /**
     * PriorityBlockingQueue: 该队列也是一个没有边界的阻塞队列，而且可以指定排序规则
     */
    static final class MyPriorityBlockingQueue {
        public static void main(String[] args) {
            PriorityBlockingQueue<MyThreadPool> priorityBlockingQueue = new PriorityBlockingQueue<>();
            start(priorityBlockingQueue);

        }
    }


    /**
     * CountDownLatch 计数器：只有指定书目的子线程执行完毕才，被阻塞的线程才可以执行
     */
    static final class MyCountDownLatch extends Thread {

        private CountDownLatch countDownLatch;

        public MyCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public static void main(String[] args) {
            CountDownLatch countDownLatch = new CountDownLatch(4);
            try {
                System.out.println("主线程开始执行了");
                for (int i = 0; i < 5; i++) {
                    new MyCountDownLatch(countDownLatch).start();
                }
//                countDownLatch.await();
                System.out.println("好了所有线程执行完毕了");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "---------我执行了");
//            countDownLatch.countDown();
        }

    }

    /**
     * CyclicBarrier 屏障：只有达到指定数目线程数达到就绪状态才会进行执行
     */
    static final class MyCyclicBarrier extends Thread{
        private CyclicBarrier cyclicBarrier;

        public MyCyclicBarrier(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        public static void main(String[] args) {
            CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
            try {
                System.out.println("开始匹配");
                for(int i = 0 ; i < 10 ; i++){
                    Thread.sleep(500);
                    new MyCyclicBarrier(cyclicBarrier).start();
                }
                Thread.sleep(50);
                System.out.println("所有玩家准备完毕，游戏开始！！！");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        public void run() {
            try {
                System.out.println("我是玩家 "+Thread.currentThread().getName()+"我准备好了");
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Semaphore 计数信号量：设置指定资源只允许指定数量线程进行同时执行，其余线程会被阻塞
     * 有许可被释放时，被阻塞的线程会发起竞争。
     */
    static final class MySemaphore extends Thread{

        private Semaphore semaphore;
        private CyclicBarrier cyclicBarrier;

        public MySemaphore(Semaphore semaphore,CyclicBarrier cyclicBarrier){
            this.semaphore = semaphore;
            this.cyclicBarrier = cyclicBarrier;
        }

        public static void main(String[] args) {
            Semaphore semaphore = new Semaphore(3);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
            try {
                MySemaphore mySemaphore = null;
                for (int i = 0 ; i < 10 ; i++){
                    mySemaphore = new MySemaphore(semaphore,cyclicBarrier);
                    mySemaphore.start();
                    System.out.println(mySemaphore.getName()+"线程就绪");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
                //拿到许可
                semaphore.acquire();
                System.out.println("我是线程"+Thread.currentThread().getName()+"我拿到许可了");
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //释放许可
                semaphore.release();
            }
        }
    }

}
