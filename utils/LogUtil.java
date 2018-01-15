
import android.util.Log;

import com.bionic.mui.BuildConfig;

public class LogUtil {
	private static final String TAG = "test";

	public static void v(String tag, String msg) {
		if (BuildConfig.LOG_ENABLED) {
			Log.v(TAG, String.format("[%s] %s", tag, msg));
		}
	}
	
	public static void v(String tag, String msg, Exception e) {
		if (BuildConfig.LOG_ENABLED) {
			Log.v(TAG, String.format("[%s] %s", tag, msg), e);
		}
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.LOG_ENABLED){
			Log.d(TAG, String.format("[%s] %s", tag, msg));
		}
	}

    // ignore sDebug setting
    public static void forceD(String tag, String msg) {
        Log.d(TAG, String.format("[%s] %s", tag, msg));
    }

    public static void forceD(String tag, String msg, Exception e) {
        Log.d(TAG, String.format("[%s] %s", tag, msg), e);
    }
	
	public static void d(String tag, String msg, Exception e) {
		if (BuildConfig.LOG_ENABLED){
			Log.d(TAG, String.format("[%s] %s", tag, msg), e);
		}
	}
	
	public static void w(String tag, String msg) {
		if (BuildConfig.LOG_ENABLED) {
			Log.w(TAG, String.format("[%s] %s", tag, msg));
		}
	}
	
	public static void w(String tag, String msg, Exception e) {
		if (BuildConfig.LOG_ENABLED) {
			Log.w(TAG, String.format("[%s] %s", tag, msg), e);
		}
	}

	public static void e(String tag, String msg) {
		if (BuildConfig.LOG_ENABLED) {
			Log.e(TAG, String.format("[%s] %s", tag, msg));
		}
	}
	
	public static void e(String tag, String msg, Exception e) {
		if (BuildConfig.LOG_ENABLED) {
			Log.e(TAG, String.format("[%s] %s", tag, msg), e);
		}
	}
}
