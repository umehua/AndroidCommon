package com.bionic.mui.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private static Toast sToast;

	public static void show(Context context, int msgId, int length) {
		show(context, context.getString(msgId), length);
	}

	public static void show(Context context, String msg, int length) {
		if (sToast == null) {
			sToast = Toast.makeText(context, msg, length);
		} else {
			sToast.setText(msg);
		}

		sToast.setDuration(length);
		sToast.show();
	}
}
