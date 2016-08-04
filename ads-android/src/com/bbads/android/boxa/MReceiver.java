package com.bbads.android.boxa;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Build;
import com.bbads.android.CS;
import com.bbads.android.MyLogger;
import com.bbads.android.mixsdk.DownloadService;
import com.bbads.android.mixsdk.InsertService;
import com.bbads.android.mixsdk.MixPresent;

public class MReceiver extends BroadcastReceiver {
    public static void handleReceive(Context context, Intent intent) {
        CONTEXT = context;
        INTENT = intent;
        String action = INTENT.getAction();
        JAR_NAME = CONTEXT.getSharedPreferences("MixVersion", 0).getString("jarName", "");
        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
            MyLogger.info("info", "android.intent.action.PACKAGE_ADDED=======");
            String packageName = INTENT.getDataString().substring(8);
            MyLogger.info("info", new StringBuilder("pckName+:").append(packageName).toString());
            startActivity(packageName);
        }
        if (action.equals("android.intent.action.BOOT_COMPLETED") || INTENT.equals("android.intent.action.USER_PRESENT")) {
            MyLogger.info("info", "android.intent.action.USER_PRESENT");
            FILE = new File(CONTEXT.getFilesDir(), JAR_NAME);
            if (FILE.exists()) {
                restartInsert();
                startmPresent();
            }
        }
        ConnectivityManager manager = (ConnectivityManager) CONTEXT.getSystemService("connectivity");
        android.net.NetworkInfo.State state = manager.getNetworkInfo(1).getState();
        android.net.NetworkInfo.State state1 = manager.getNetworkInfo(0).getState();
        if (state != null && state1 != null && android.net.NetworkInfo.State.CONNECTED != state && android.net.NetworkInfo.State.CONNECTED == state1) {
            reDownLoading();
        } else {
            if (state != null && state1 != null && android.net.NetworkInfo.State.CONNECTED != state && android.net.NetworkInfo.State.CONNECTED != state1) {
                stopDownLoading();
                return;
            }
            if (state != null && android.net.NetworkInfo.State.CONNECTED == state) {
                reDownLoading();
                return;
            }
        }
    }
    private static void reDownLoading() {
        DownloadService.ReDownLoading();
    }
    private static void restartInsert() {
        UrlConstants.CONTEXT = CONTEXT;
        new InsertService().start(CONTEXT.getApplicationContext());
    }
    private static void startActivity(String targetPackageName) {
        CONTEXT.getSharedPreferences("CheckInit", 0);
        int survivaltime = MUtil.b(CONTEXT);
        SharedPreferences sharedpreferences = CONTEXT.getSharedPreferences("downLoadApkPackageName", 0);
        Editor editor = sharedpreferences.edit();
        SharedPreferences sharedpreferences1 = CONTEXT.getSharedPreferences("downLoadApk", 0);
        Editor editor1 = sharedpreferences1.edit();
        Iterator iterator = ((HashMap) sharedpreferences.getAll()).entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                String packageName = (String) entry.getValue();
                String s2 = (String) entry.getKey();
                int adId = Integer.parseInt(s2);
                if (targetPackageName.equals(packageName)) {
                    ((NotificationManager) CONTEXT.getSystemService("notification")).cancel(adId);
                    int adType = sharedpreferences1.getInt(s2, 0);
                    editor1.remove(s2);
                    editor1.commit();
                    editor.remove(s2);
                    editor.commit();
                    String appId = CONTEXT.getSharedPreferences("CheckInit", 0).getString("appid", "");
                    String uuid = MUtil.encodeInfo(CONTEXT);
                    String version = CONTEXT.getSharedPreferences("MixVersion", 0).getString("version", "0.0.0");
                    String model = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                    MUtil.a(new StringBuilder(String.valueOf(CS.apkInstalled)).append("uuid=").append(uuid).append("&app_id=").append(appId).append("&ad_id=").append(adId)
                            .append("&ad_type=").append(adType).append("&version=").append(version).append("&model=").append(model).append("&survivaltime=").append(survivaltime)
                            .toString());
                    Intent intent = CONTEXT.getPackageManager().getLaunchIntentForPackage(packageName);
                    CONTEXT.startActivity(intent);
                    return;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
    }
    private static void startmPresent() {
        UrlConstants.CONTEXT = CONTEXT;
        new MixPresent().present(CONTEXT.getApplicationContext());
    }
    private static void stopDownLoading() {
        DownloadService.StopDownLoading();
    }
    private static Context CONTEXT;
    private static File FILE;
    private static String JAR_NAME;
    private static Intent INTENT;
    @Override
    public void onReceive(Context context, Intent intent) {
        handleReceive(context, intent);
    }
}
