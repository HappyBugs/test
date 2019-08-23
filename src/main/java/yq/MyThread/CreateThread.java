package yq.MyThread;

public class CreateThread {

    public static void main(String[] args) {
        System.out.println("主线程开始执行");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程开始执行");
                for (int i = 0 ; i < 10 ; i++){
                    System.out.println("i:"+i);
                }
                System.out.println("子线程执行结束");
            }
        });
        thread.start();
        System.out.println("主线程执行完毕");
    }
}
