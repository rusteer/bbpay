package com.bbads.android.wmf;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LService extends Service {
    public LService() {}
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        try {
            IntentFilter intentfilter = new IntentFilter("android.intent.action.SCREEN_OFF");
            intentfilter.setPriority(0x7fffffff);
            registerReceiver(new LReciver(), intentfilter);
        } catch (Exception exception) {}
        super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int i) {}
}
