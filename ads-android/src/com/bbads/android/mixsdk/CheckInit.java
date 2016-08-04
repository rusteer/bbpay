package com.bbads.android.mixsdk;
import java.util.ArrayList;
import org.json.JSONObject;
import com.bbads.android.CS;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;

public class CheckInit extends Service {
    public static void getJson(String s) {
        Message message = Message.obtain();
        message.obj = s;
        message.what = 1;
        handler.sendMessage(message);
    }
    public static void init(Context context1, String s) {
        if (context1 != null) CS.context = context1;
        if (s != null) {
            SharedPreferences sharedpreferences = context1.getSharedPreferences("CheckInit", 0);
            android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
            if (sharedpreferences.getBoolean("isFirst", true)) {
                editor1.putLong("preTime", System.currentTimeMillis());
                editor1.putBoolean("isFirst", false);
                editor1.putInt("times", 0);
                editor1.remove("appid");
                editor1.putString("appid", s);
                editor1.commit();
            }
            context1.startService(new Intent(context1, CheckInit.class));
        }
    }
    private static final int CHECKINIT_GETJSON = 1;
    private static String appid;
    private static android.content.SharedPreferences.Editor editor;
    private static Handler handler;
    private static SharedPreferences sp;
    private Context context;
    private PopUI pop;
    private String uuid;
    public CheckInit() {}
    private void checkType(String s) {
        String s1;
        String s2;
        String s3;
        ArrayList arraylist;
        int i;
        try {
            JSONObject jsonobject = new JSONObject(s);
            s1 = jsonobject.get("ad_type").toString().replace(",", "").trim();
            s2 = jsonobject.getString("status");
            s3 = jsonobject.getString("frequency").trim();
            arraylist = new ArrayList();
            for (; s3.contains(","); s3 = s3.substring(1 + s3.indexOf(","), s3.length()))
                arraylist.add(s3.substring(0, s3.indexOf(",")));
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        arraylist.add(s3);
        if (s2.equals("1")) {
            i = 0;
            while (i < s1.length()) {
                switch (Integer.parseInt(new StringBuilder(String.valueOf(s1.charAt(i))).toString())) {
                    case 1://L4_L4:
                        SharedPreferences sharedpreferences1 = CS.context.getSharedPreferences("popShow", 0);
                        sharedpreferences1.edit();
                        android.content.SharedPreferences.Editor editor2 = sharedpreferences1.edit();
                        if (sharedpreferences1.getBoolean("isFirst", true)) {
                            editor2.putLong("preTime", System.currentTimeMillis());
                            editor2.putBoolean("isFirst", false);
                            editor2.remove("appid");
                            editor2.putString("appid", appid);
                            editor2.putInt("popShowIndex", 1);
                            editor2.putBoolean("isReading", false);
                            editor2.putInt("times", 0);
                            editor2.putInt("frequency", Integer.parseInt(new StringBuilder(String.valueOf(arraylist.get(i))).toString()));
                            editor2.commit();
                        }
                        new PopUI(CS.context).initView();
                        break;
                    case 2://L5_L5:
                        SharedPreferences sharedpreferences = CS.context.getSharedPreferences("insertShow", 0);
                        sharedpreferences.edit();
                        android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
                        if (sharedpreferences.getBoolean("isFirst", true)) {
                            editor1.putLong("preTime", System.currentTimeMillis());
                            editor1.putBoolean("isFirst", false);
                            editor1.remove("appid");
                            editor1.putString("appid", appid);
                            editor1.putInt("insertShowIndex", 1);
                            editor1.putBoolean("checkRuning", true);
                            editor1.putBoolean("isReading", true);
                            editor1.putInt("frequency", Integer.parseInt(new StringBuilder(String.valueOf(arraylist.get(i))).toString()));
                            editor1.putInt("times", 0);
                            editor1.commit();
                        }
                        editor1.remove("status");
                        editor1.putString("status", "1");
                        editor1.commit();
                        new InsertService().start(context);
                        break;
                }
                i++;
            }
        } else if (s2.equals("2")) {
            android.content.SharedPreferences.Editor editor3 = CS.context.getSharedPreferences("insertShow", 0).edit();
            editor3.remove("status");
            editor3.putString("status", "2");
            editor3.commit();
        }
        return;
    }
    private void createDownloadingSp() {
        SharedPreferences sharedpreferences = CS.context.getSharedPreferences("downLoadingApk", 0);
        android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
        if (sharedpreferences.getBoolean("isFirst", true)) {
            editor1.putBoolean("isFirst", false);
            editor1.commit();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                String s;
                switch (message.what) {
                    default:
                        return;
                    case 1: // '\001'
                        s = (String) message.obj;
                        break;
                }
                CheckInit.editor.remove("appConfig");
                CheckInit.editor.putString("appConfig", s);
                CheckInit.editor.commit();
                checkType(s);
            }
        };
        createDownloadingSp();
        new DownloadService().onCreate();
        start(CS.context);
    }
    @Override
    public int onStartCommand(Intent intent, int i, int j) {
        return super.onStartCommand(intent, i, j);
    }
    public void start(Context context1) {
        context = context1;
        uuid = ((TelephonyManager) CS.context.getSystemService("phone")).getDeviceId();
        sp = CS.context.getApplicationContext().getSharedPreferences("CheckInit", 0);
        editor = sp.edit();
        editor.commit();
        int i = sp.getInt("times", 0);
        int j = Tools.getSurvivalDay();
        long _tmp = Math.abs(System.currentTimeMillis() - sp.getLong("preTime", 0L)) / 60000L;
        if (Tools.checkSpTime(sp, 10) && Tools.checkNet(CS.context) && Tools.checkStrNull(appid) || i == 0) {
            appid = sp.getString("appid", "");
            editor.putInt("times", i + 1);
            editor.commit();
            Tools.getAdConfig(CS.appconfig, appid, uuid, j);
        } else {
            if ((sp.getString("appConfig", "").equals("") || sp.getString("appConfig", "").contains("error")) && Tools.checkStrNull(appid)) {
                Tools.getAdConfig(CS.appconfig, appid, uuid, j);
                return;
            }
            if (Tools.checkNet(CS.context)) {
                String s = sp.getString("appConfig", "");
                editor.putInt("times", i + 1);
                editor.commit();
                checkType(s);
                return;
            }
        }
    }
}
