
import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectivityHelper {
	public static boolean hasNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connected = cm.getActiveNetworkInfo() != null ? cm.getActiveNetworkInfo().isConnected() : false;
		return connected;
	}
}
