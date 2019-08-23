package yq.Thread;

public class MyThreadLocal implements Runnable {

    private static Integer number = 100;

    private static ThreadLocal<Integer> threadLocal =  new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return MyThreadLocal.number;
        }
    };

    public Integer get(){
        int number = threadLocal.get();
        threadLocal.set(--number);
        return number;
    }

    @Override
    public void run() {
        try{
            while (threadLocal.get() >0) {
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() + "拿到第了：" + get() + " 张火车票");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyThreadLocal myThreadLocal = new MyThreadLocal();

        Thread thread = new Thread(myThreadLocal);
        Thread thread1 = new Thread(myThreadLocal);
        thread.start();
        thread1.start();
    }
}
