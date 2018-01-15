import java.util.Date;

public class JavaLogUtil {

    private static final boolean DEBUG = true;

    public static void d(String TAG, String msg) {
        System.out.println(String.format("%s DEBUG [%d - %s] %s",
                TimeUtil.getTimeString(new Date()),
                Thread.currentThread().getId(),
                TAG,
                msg));
    }

    public static void d(String TAG, String msg, Exception e) {
        System.out.println(String.format("%s WARN [%d - %s] %s",
                TimeUtil.getTimeString(new Date()),
                Thread.currentThread().getId(),
                TAG,
                msg));
        e.printStackTrace();
    }
}
