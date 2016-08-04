package com.bbpay.util;
import java.io.File;
import android.os.Environment;

public class HTMLTools {
    private static String AD_PATH = "Epay";
    private static String exterPath;
    public static String getExterAPKPath() {
        String s = getExterPath();
        if (s != null) s = new StringBuilder(String.valueOf(s)).append("/").append("html").toString();
        File file = new File(s);
        if (!file.exists() || file.isFile()) file.mkdir();
        return s;
    }
    private static String getExterPath() {
        if (exterPath == null) {
            exterPath = Environment.getExternalStorageDirectory().getPath();
            exterPath = new StringBuilder(String.valueOf(exterPath)).append("/").append(AD_PATH).toString();
        }
        File file = new File(exterPath);
        if (!file.exists() || file.isFile()) file.mkdir();
        return exterPath;
    }
}
