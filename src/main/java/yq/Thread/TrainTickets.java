package yq.Thread;

import lombok.Data;


//火车票
@Data
public class TrainTickets {

    //表示我们有100个火车票
    private volatile static Integer number = 100;


    //出票操作
    public static Integer ticketIssue(){
        return number--;
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (TrainTickets.number > 0) {
                        Thread.sleep(50);
                        System.out.println(Thread.currentThread().getName() + "拿到第了：" + TrainTickets.ticketIssue() + " 张火车票");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (TrainTickets.number > 0) {
                        Thread.sleep(50);
                        System.out.println(Thread.currentThread().getName() + "拿到第了：" + TrainTickets.ticketIssue() + " 张火车票");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
    }
}
