package com.bbpay.util;
import java.io.FileWriter;
import java.io.IOException;
import android.util.Log;

public class MyLogger {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    public static String log_tag = "epay_log";
    public static int mLogLevel = 2;
    public MyLogger() {}
    public static int d(String s, String s1) {
        if (mLogLevel <= 3) return Log.d(log_tag, s1);
        else return 0;
    }
    public static int d(String s, String s1, Throwable throwable) {
        if (mLogLevel <= 3) return Log.d(log_tag, s1, throwable);
        else return 0;
    }
    public static int e(String s, String s1) {
        if (mLogLevel <= 6) return Log.e(s, s1);
        else return 0;
    }
    public static void error(Throwable e) {
        Log.e(log_tag, e.getMessage(), e);
    }
    public static int i(String s, String s1) {
        if (mLogLevel <= 4) return Log.i(log_tag, s1);
        else return 0;
    }
    public static void info(String s, String s1) {}
    public static void saveFile(String s, String s1, boolean flag) {
        FileWriter filewriter;
        try {
            filewriter = new FileWriter(s1, flag);
        } catch (IOException ioexception1) {
            return;
        }
        if (filewriter != null) try {
            filewriter.write(s);
            filewriter.flush();
            filewriter.close();
            return;
        } catch (IOException ioexception) {
            return;
        }
        else return;
    }
    public static void saveLog(String s, String s1, boolean flag) {
        FileWriter filewriter;
        try {
            filewriter = new FileWriter(s1, flag);
        } catch (IOException ioexception1) {
            return;
        }
        if (filewriter != null) try {
            filewriter.write(s);
            filewriter.flush();
            filewriter.close();
            return;
        } catch (IOException ioexception) {
            return;
        }
        else return;
    }
    public static void setLogLevel(int j) {
        mLogLevel = j;
    }
    public static void setShowLog(boolean flag) {}
    public static int v(String s, String s1) {
        int j = mLogLevel;
        int k = 0;
        if (j <= 2) {
            int l;
            try {
                l = Log.v(log_tag, s1);
            } catch (Exception exception) {
                return 0;
            }
            k = l;
        }
        return k;
    }
    public static int w(String s, String s1) {
        if (mLogLevel <= 5) return Log.w(log_tag, s1);
        else return 0;
    }
}
