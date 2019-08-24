package yq.Thread;

import java.util.concurrent.*;

/**
 * java并发编程之线程池
 */
public class MyThreadPool {

    /**
     * 第一个例子 ThreadPoolExecutor
     * 实现思路：使用CyclicBarrier模拟高并发情况。一次当线程数达到30才进行执行
     * 。但是我们的线程池设置的最大线程数和队列数量是不够30的 所以肯定会发生拒绝策略。
     */
    static final class MyThreadPoolExecutor01 extends Thread {

        //屏障 用于模拟多线程并发访问
        private CyclicBarrier cyclicBarrier;

        private ThreadPoolExecutor threadPoolExecutor;

        public MyThreadPoolExecutor01(CyclicBarrier cyclicBarrier,ThreadPoolExecutor threadPoolExecutor) {
            this.cyclicBarrier = cyclicBarrier;
            this.threadPoolExecutor = threadPoolExecutor;
        }

        @Override
        public void run() {
            System.out.println("我创建成功了-----------》"+Thread.currentThread().getName());
            try {
                //阻塞，只有30个线程就绪之后才会被放行
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            threadPoolExecutor.execute(new RunThread());
        }

        public static void main(String[] args) {
            //达到30和线程之后 统一放开
            CyclicBarrier cyclicBarrier = new CyclicBarrier(30);
            //创建一个 核心线程数为4 最大线程数为8，队列最大容量为8 自定义拒绝策略
            ThreadPoolExecutor threadPoolExecutor1 = new ThreadPoolExecutor(4,
                    8, 3L, TimeUnit.MINUTES,
                    new ArrayBlockingQueue<>(8),new MyHandler());
            //理论上 我们的这个线程池可以同时运行最大16个线程
            while (true){
                //为了防止跑得太快 我们阻塞一下生产的速度
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new MyThreadPoolExecutor01(cyclicBarrier,threadPoolExecutor1).start();
            }
        }

        /**
         * 这是线程池的拒绝策略，
         * 当任务数量大于 maximumPoolSize + workQueue 队列数量的时候的拒绝策略
         */
        static final class MyHandler implements RejectedExecutionHandler{

            //拒绝的时候发生的策略
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println(r.toString()+"被我们的线程池拒绝了");
            }
        }

        //该类是用来运行的线程
        static final class RunThread implements Runnable{
            @Override
            public void run() {
                System.out.println("我是线程---->"+Thread.currentThread().getName());
            }
        }

        /**
         * 这是例子一里举例说明返回值怎么使用 偷个懒 具体的代码我就不实现了
         */

//        @PostMapping(value = "/api/clothAward")
//        public Object clothAward(@RequestBody JackpotParam jackpotParam){
//            CountDownLatch countDownLatch = new CountDownLatch(1);
//            try{
//                Optional<MyOrder> myOrder = myOrderRepo.findById(jackpotParam.getMyOrderId());
//                Assert.isTrue(myOrder.isPresent(),"该订单已经参与布奖，可以执行修改布奖操作");
//                //使用线程池进行调用线程执行
//                Future<Object> submit = threadPoolExecutor.submit(new JackPotCallable(jackpotParam, boxService, jackpotService, productService, false, getUserId()));
//                //调用拿到结果的方法
//                Object object = getObject(countDownLatch, submit);
//                //阻塞主线程，只有拿到真实结果才允许放行，不然会导致返回客户端结果不一致
//                countDownLatch.await();
//                return object;
//            }catch (Exception e){
//                countDownLatch.countDown();
//                throw new DIYException("布奖失败："+e.getMessage());
//            }
//        }
//
//
//        //拿到执行结果的方法
//        public Object getObject(CountDownLatch countDownLatch,Future<Object> submit){
//            Object result = null;
//            while (true){
//                try {
//                    //25秒没用拿到执行结果，那么会抛出异常
//                    result = submit.get(25,TimeUnit.SECONDS);
//                    if(result != null){
//                        countDownLatch.countDown();
//                        break;
//                    }
//                } catch (Exception e) {
//                    throw new DIYException("获取布奖结果超时:"+e.getMessage());
//                }finally {
//                    countDownLatch.countDown();
//                }
//            }
//            return result;
//        }

    }


    /**
     * 例子二  Executors
     * 因为其他几个都是一样的 我们只演示 newScheduledThreadPool 定时执行
     */
    static final class MyThreadPoolExecutor02 extends Thread{

        public static void main(String[] args) {
            //该线程池 核心线程数 0 最大 基本上接近无限大 为Integer.MAX_VALUE 空闲回收时间为60秒
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
            while (true){
                //三秒执行一次 其余的任务会被缓存到 队列里面
                scheduledExecutorService.schedule(new RunThread(),3,TimeUnit.SECONDS);
            }
        }

        //该类是用来运行的线程
        static final class RunThread implements Runnable{
            @Override
            public void run() {
                System.out.println("我是线程---->"+Thread.currentThread().getName());
            }
        }
    }

}
