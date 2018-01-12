package com.bionic.mui.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class StorageUtils {

    // return with unit byte
    public static long getStorageAvailaleSize(boolean sdcard) {
        File path;
        if (!sdcard) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = new File("/mnt/m_external_sd");
        }

        if (!path.exists()) {
            return 0;
        }

        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();

            // (availableBlocks * blockSize)/1024 KB 单位
            // (availableBlocks * blockSize)/1024 /1024 MB单位
            return availableBlocks * blockSize;
        } else {
            int blockSize = stat.getBlockSize();
            int availBlocks = stat.getAvailableBlocks();
            return (long)availBlocks * blockSize;
        }
    }

    // return with unit byte
    public static long getStorageTotalSize(boolean sdcard) {
        File path;
        if (!sdcard) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = new File("/mnt/m_external_sd");
        }

        if (!path.exists()) {
            return 0;
        }

        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = stat.getBlockSizeLong();
            long totalBlock = stat.getBlockCountLong();
            return totalBlock * blockSize;
        } else {
            int totalBlocks = stat.getBlockCount();
            int blockSize = stat.getBlockSize();
            return (long)totalBlocks * blockSize;
        }
    }
}
