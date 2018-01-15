
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    public static String getCurrentTimeString(TimeZone zone) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EE yyy/MM/dd HH:mm:ss");

        if (zone == null) {
            c.setTimeZone(TimeZone.getTimeZone("UTC+0"));
        } else {
            c.setTimeZone(zone);
        }

        return sdf.format(c.getTime());
    }

    public static String millis2TimeString(long millis, boolean forceDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date = new Date(millis);
        String splits[] = sdf.format(date).split(" ");

        if (forceDate) {
            return splits[0];
        }

        Date today = new Date();
        if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth()
                && today.getDay() == date.getDay()) {
            return splits[1];
        } else {
            return splits[0];
        }
    }

    public static String getTimeString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    public static String getTimeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
