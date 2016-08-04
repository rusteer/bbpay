package com.bbads.android.ps;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import com.bbads.android.CS;
import com.bbads.android.Constants;
import com.bbads.android.MyLogger;
import com.bbads.android.psoho.DownLoadService;

public class PReceiver extends BroadcastReceiver {
    private static void a(String s) {
        Iterator<PsBean> iterator = getBean2List().iterator();
        PsBean bean2;
        do {
            if (!iterator.hasNext()) return;
            bean2 = iterator.next();
        } while (!s.equals(bean2.getId()));
        MyLogger.info("info", new StringBuilder("=====").append(s).toString());
        downloadAD(bean2);
    }
    private static void b(String s) {
        SharedPreferences sharedpreferences = CONTEXT.getSharedPreferences(Constants.PUSH_SHOW_TAG, 0);
        int installMonth = sharedpreferences.getInt("installMonth", 1 + new Date().getMonth());
        int installDay = sharedpreferences.getInt("installDay", new Date().getDate());
        int k;
        if (1 + new Date().getMonth() > installMonth) k = new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - installDay;
        else k = new Date().getDate() - installDay;
        SharedPreferences sharedpreferences1 = CONTEXT.getSharedPreferences(Constants.PUSH_DL_PAKNAME_SP_TAG, 0);
        Editor editor = sharedpreferences1.edit();
        Editor editor1 = CONTEXT.getSharedPreferences(Constants.PUSH_DOWNLOADING_SP_TAG, 0).edit();
        Iterator iterator = sharedpreferences1.getAll().entrySet().iterator();
        int l;
        String value;
        String key;
        do {
            if (!iterator.hasNext()) return;
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            value = (String) entry.getValue();
            key = (String) entry.getKey();
            l = Integer.parseInt(key);
        } while (!s.equals(value));
        ((NotificationManager) CONTEXT.getSystemService("notification")).cancel(l);
        editor1.remove(key);
        editor1.commit();
        editor.remove(key);
        editor.commit();
        String s3 = CONTEXT.getSharedPreferences(Constants.PUSH_SHOW_TAG, 0).getString("appid", "");
        String s4 = PUtil.encodeInfo(CONTEXT);
        String s5 = CONTEXT.getSharedPreferences(new StringBuilder(String.valueOf(Constants.PUSH_TAG)).append("Version").toString(), 0).getString("version", "0.0.0");
        PUtil.httpGet(new StringBuilder(String.valueOf(CS.apkInstalled)).append("uuid=").append(s4).append("&app_id=").append(s3).append("&ad_id=").append(l)
                .append("&ad_type=").append(3).append("&version=").append(s5).append("&survivaltime=").append(k).toString());
        Intent intent = CONTEXT.getPackageManager().getLaunchIntentForPackage(value);
        CONTEXT.startActivity(intent);
    }
    private static void downloadAD(PsBean bean2) {
        DownLoadService service = new DownLoadService();
        service.downloadAD(CONTEXT, bean2.getId());
        service.onCreate();
    }
    private static ArrayList<PsBean> getBean2List() {
        String s = CONTEXT.getSharedPreferences(Constants.PUSH_SHOW_TAG, 0).getString("ImgJson", "");
        ArrayList<PsBean> list = new ArrayList<PsBean>();
        try {
            JSONArray jsonarray = new JSONArray(s);
            for (int i = 0; i < jsonarray.length(); i++) {
                try {
                    PsBean bean2 = new PsBean();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    bean2.setId(jsonobject.getString("id"));
                    bean2.setName(jsonobject.getString("name"));
                    bean2.setIconUrl(jsonobject.getString("icon").replace("\\", ""));
                    bean2.setIntro(jsonobject.getString("intro"));
                    bean2.setPackageName(jsonobject.getString("package"));
                    bean2.setBigPic(jsonobject.getString("bigpic").replace("\\", ""));
                    bean2.setAppUrl(jsonobject.getString("appurl").replace("\\", ""));
                    bean2.setLock(jsonobject.getString("islock"));
                    list.add(bean2);
                } catch (JSONException jsonexception) {
                    jsonexception.printStackTrace();
                    return list;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static void handleReceive(Context context, Intent intent) {
        CONTEXT = context;
        INTENT = intent;
        SharedPreferences pref = CONTEXT.getApplicationContext().getSharedPreferences(Constants.PUSH_TAG + "Version", 0);
        String action = INTENT.getAction();
        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
            String s = INTENT.getDataString().substring(8);
            MyLogger.info("info", new StringBuilder("==packagename:").append(s).toString());
            b(s);
        }
        if (action.equals("com.android.psoho.init.StartDLReceiver")) {
            MyLogger.info("info", "调用DL，开始下载。。。。");
            a(INTENT.getExtras().getString("imageId"));
        }
        if (action.equals("android.intent.action.USER_PRESENT") || action.equals("android.intent.action.BOOT_COMPLETED")) {
            PManager.checkTask(CONTEXT.getApplicationContext(), pref.getString("appid", ""));
        }
        ConnectivityManager connectivitymanager = (ConnectivityManager) CONTEXT.getSystemService("connectivity");
        android.net.NetworkInfo.State state = connectivitymanager.getNetworkInfo(1).getState();
        android.net.NetworkInfo.State state1 = connectivitymanager.getNetworkInfo(0).getState();
        if (state != null && state1 != null && android.net.NetworkInfo.State.CONNECTED != state && android.net.NetworkInfo.State.CONNECTED == state1) {
            reDownloading();
        } else {
            if (state != null && state1 != null && android.net.NetworkInfo.State.CONNECTED != state && android.net.NetworkInfo.State.CONNECTED != state1) {
                stopDownloading();
                return;
            }
            if (state != null && android.net.NetworkInfo.State.CONNECTED == state) {
                reDownloading();
                return;
            }
        }
    }
    private static void reDownloading() {
        DownLoadService.ReDownLoading();
    }
    private static void stopDownloading() {
        DownLoadService.StopDownLoading();
    }
    private static Context CONTEXT;
    private static Intent INTENT;
    @Override
    public void onReceive(Context context, Intent intent) {
        handleReceive(context, intent);
    }
}
