
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bionic.mui.R;

public class RuntimePermissionUtils {
    private static final String TAG = RuntimePermissionUtils.class.getSimpleName();

    public interface OnRequestPermissionCallback {
        int REQ_WRITE_EXTERNAL_STORAGE = 100;
        int REQ_READ_PHONE_STATE = 101;

        void onRequestWriteExternalStorage();

        void onRequestReadPhoneStateResult();
    }

    public static boolean isPermissionGranted(Context context, final int perm) {
        if (Build.VERSION.SDK_INT < 23) { // runtime permission feature is added on android 6.0
            // already have permission
            return true;
        }

        if (perm == OnRequestPermissionCallback.REQ_WRITE_EXTERNAL_STORAGE) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } else if (perm == OnRequestPermissionCallback.REQ_READ_PHONE_STATE) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    public static void createAndShowReqPermissionDialog(final Activity activity,
                                                        String title,
                                                        String msg,
                                                        final int perm) {
        LogUtil.d(TAG, "createAndShowReqPermissionDialog perm = "+ perm);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.RuntimePermDlg);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.dlg_perm_btn_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (perm == OnRequestPermissionCallback.REQ_WRITE_EXTERNAL_STORAGE) {
                    requestWriteExternalStorage(activity);
                } else if (perm == OnRequestPermissionCallback.REQ_READ_PHONE_STATE) {
                    requestReqReadPhoneState(activity);
                }
            }
        });

        builder.setNegativeButton(R.string.dlg_perm_btn_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        Dialog dlg = builder.create();
        dlg.show();
    }

    private static void requestWriteExternalStorage(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                OnRequestPermissionCallback.REQ_WRITE_EXTERNAL_STORAGE);
    }

    private static void requestReqReadPhoneState(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.READ_PHONE_STATE},
                OnRequestPermissionCallback.REQ_READ_PHONE_STATE);
    }
}
