
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarHelper {
	
	private static final String TAG = StatusBarHelper.class.getSimpleName();
	
	public static int getStatusBarHeight(Context context) {
	    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
	    return context.getResources().getDimensionPixelSize(resourceId);
	}
	
    /**
      * 当Activity布局是DrawerLayout时，应该使用DrawerLayout.setStatusBarColor来改变statusbar的颜色
      * 否则Drawer Header会被StatusBar遮挡
      */
	public static void setStatusBarColor(Activity activity, int color) {
		// 取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
		activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		activity.getWindow().setStatusBarColor(color);
	}
	
	public static void setStatusNavBarColor(Activity activity, int color) {
		if (Build.VERSION.SDK_INT >= 21) { // android 5.1
			try {
//				activity.getWindow().getAttributes().systemUiVisibility |= 
//				    (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//				        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//				        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//				activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//						| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); // 去掉translucent flag
				Field drawsSysBackgroundsField = WindowManager.LayoutParams.class
						.getField("FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS");
				activity.getWindow().addFlags(drawsSysBackgroundsField.getInt(null));
		
				Method setStatusBarColorMethod = Window.class.getDeclaredMethod("setStatusBarColor", int.class);
				Method setNavigationBarColorMethod = Window.class.getDeclaredMethod("setNavigationBarColor", int.class);
				setStatusBarColorMethod.invoke(activity.getWindow(), color);
				setNavigationBarColorMethod.invoke(activity.getWindow(), color);
			} catch (Exception e) {
				LogUtil.d(TAG, "setStatusNavBarColor() failed", e);
			}
		}
	}
}
