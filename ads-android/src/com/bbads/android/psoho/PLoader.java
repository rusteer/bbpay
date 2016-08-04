package com.bbads.android.psoho;
import java.util.Date;
import android.content.Context;
import android.content.SharedPreferences;
import com.bbads.android.CS;

public class PLoader {
    public void init(Context context, String s) {
        CS.context = context;
        android.content.SharedPreferences.Editor editor;
        SharedPreferences sharedpreferences;
        android.content.SharedPreferences.Editor editor1;
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException classnotfoundexception) {
            classnotfoundexception.printStackTrace();
        }
        editor = context.getSharedPreferences("PushVersion", 0).edit();
        editor.remove("version");
        editor.putString("version", CS.version);
        editor.commit();
        sharedpreferences = context.getSharedPreferences("pushShow", 0);
        editor1 = sharedpreferences.edit();
        if (sharedpreferences.getBoolean("isFirst", true)) {
            Tools.sendDataToService(new StringBuilder(CS.newIMEI+"&app_id=").append(s).append("uuid=").append(Tools.getIMEI(context))
                    .append("&ad_type=3").toString());
            editor1.putLong("preTime", System.currentTimeMillis());
            editor1.putBoolean("isFirst", false);
            editor1.putInt("times", 0);
            editor1.remove("appid");
            editor1.putString("appid", s);
            editor1.putInt("installMonth", 1 + new Date().getMonth());
            editor1.putInt("installDay", new Date().getDate());
            editor1.commit();
        }
        new DownLoadService().onCreate();
        new PushShow().onStart(context);
    }
}
