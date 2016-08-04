package com.bbads.android.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.bbads.android.MyLogger;

public class Store {
    public static void deleteData(Context context, String path) {
        if (path != null) {
            deleteFromPref(context, path);
            deleteFromSdCard(path);
        }
    }
    private static void deleteFromPref(Context context, String path) {
        try {
            Editor editor = getPref(context).edit();
            editor.remove(path);
            editor.commit();
        } catch (Exception e) {
            MyLogger.error(e);
        }
    }
    private static void deleteFromSdCard(String path) {
        try {
            File file = new File(android.os.Environment.getExternalStorageDirectory() + path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            MyLogger.error(e);
        }
    }
    public static String getData(Context context, String path, String password) {
        String data = readFromPref(context, path, password);
        if (TextUtils.isEmpty(data)) {
            data = readFromSdCard(path, password);
        }
        return data;
    }
    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(PRE_NAME, 0);
    }
    public static String readFromPref(Context context, String path, String password) {
        String result = null;
        try {
            String encodedValue = getPref(context).getString(path, null);
            if (encodedValue != null) {
                result = AES.decode(encodedValue, password);
            }
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return result;
    }
    public static String readFromSdCard(String path, String password) {
        File file = new File(android.os.Environment.getExternalStorageDirectory() + path);
        String result = null;
        BufferedReader reader = null;
        try {
            if (file.exists() && file.isFile()) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                result = AES.decode(sb.toString(), password);
            }
        } catch (Throwable e) {
            MyLogger.error(e);
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                MyLogger.error(e);
            }
        }
        return result;
    }
    public static void saveData(Context context, String path, String data, String password) {
        if (data != null && path != null && password != null) {
            writeToSdCard(path, data, password);
            writeToPref(context, path, data, password);
        }
    }
    public static boolean writeToPref(Context context, String path, String data, String password) {
        boolean success = false;
        try {
            if (data != null) {
                Editor editor = getPref(context).edit();
                editor.putString(path, AES.encode(data, password));
                editor.commit();
            }
            success = true;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return success;
    }
    public static boolean writeToSdCard(String path, String data, String password) {
        boolean success = false;
        String encodedData = AES.encode(data, password);
        FileOutputStream stream = null;
        try {
            File file = new File(android.os.Environment.getExternalStorageDirectory() + path);
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            stream = new FileOutputStream(file);
            stream.write(encodedData.getBytes("UTF-8"));
            stream.flush();
            success = true;
        } catch (Throwable e) {
            MyLogger.error(e);
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
                MyLogger.error(e);
            }
        }
        return success;
    }
    private static final String PRE_NAME = "uuuuuuuuuuuuuuuuuuuuuuuu";
}
