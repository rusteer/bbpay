package com.bbads.android.wmf;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.bbads.android.MyLogger;
import com.bbads.android.shortcut.ShortcutUtil;

public class LActivity extends Activity implements TriggerListener {
    public static void b() {
        handler.sendEmptyMessage(1);
    }
    private static Handler handler;
    private void d() {
        ((SlideBar) findViewById(com.bbads.android.AR.id.slideBar)).setOnTriggerListener(this);
        f();
        e();
    }
    private void e() {
        WUtil.a(this, (RelativeLayout) findViewById(com.bbads.android.AR.id.RL));
    }
    private void f() {
        ((KeyguardManager) getSystemService("keyguard")).newKeyguardLock("my_lockscreen").disableKeyguard();
    }
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    default:
                        return;
                    case 1: // '\001'
                        finish();
                        break;
                }
                ((KeyguardManager) getSystemService("keyguard")).newKeyguardLock("my_lockscreen").reenableKeyguard();
            }
        };
    }
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(com.bbads.android.AR.layout.ls);
        d();
        initHandler();
    }
    @Override
    public boolean onKeyDown(int i, KeyEvent keyevent) {
        if (keyevent.getKeyCode() == 3) {
            MyLogger.info("info", "KEYCODE_HOME");
            return true;
        }
        if (keyevent.getKeyCode() == 4) {
            MyLogger.info("info", "KEYCODE_BACK");
            return true;
        } else {
            return super.onKeyDown(i, keyevent);
        }
    }
    @Override
    public void onTrigger() {
        finish();
        openUninstallFile();
    }
    private void openUninstallFile() {
        MyLogger.info("info", "openUninstallFile");
        try {
            String s = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;
            File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(s).toString());
            SharedPreferences sharedpreferences = getSharedPreferences("downLoadFileName", 0);
            android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
            HashMap hashmap = (HashMap) sharedpreferences.getAll();
            Iterator iterator = hashmap.entrySet().iterator();
            long l = sharedpreferences.getLong("preTime", 0L);
            long l1 = Math.abs(System.currentTimeMillis() - l) / 0x36ee80L;
            int i = sharedpreferences.getInt("times", 0);
            if (i != 0 && l1 < 4L) return;
            if (hashmap.size() <= 3) return;
            //_L2:
            while (iterator.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                String s1 = (String) entry.getKey();
                if (!s1.equals("isFirst") && !s1.equals("times") && !s1.equals("preTime")) {
                    String s2 = (String) entry.getValue();
                    File file1 = new File(new StringBuilder().append(file).append("/").append(s2).toString());
                    if (!ShortcutUtil.a(this, s1) && file1.exists()) {
                        editor.putLong("preTime", System.currentTimeMillis());
                        editor.putInt("times", i + 1);
                        editor.commit();
                        ShortcutUtil.a(this, file1, s1);
                    }
                }
            }
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
