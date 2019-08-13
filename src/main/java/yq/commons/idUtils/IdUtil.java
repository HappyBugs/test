package yq.commons.idUtils;

/**
 * id生成
 */
public class IdUtil {
    public static SnowFlake snowFlake = new SnowFlake();

    public static long createId(){
        return snowFlake.nextId();
    }

    public static void main(String[] args) {
        while(true){
//            System.out.println(System.currentTimeMillis());
            System.out.println(createId());
        }
    }
}
