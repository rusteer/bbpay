package com.bbads.android.mixsdk;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import android.content.Context;
import android.content.SharedPreferences;

public class MixPresent {
    private Context context;
    public MixPresent() {}
    private void openUninstallapk() {
        android.content.SharedPreferences.Editor editor;
        HashMap hashmap;
        Iterator iterator;
        long l1;
        int i;
        SharedPreferences sharedpreferences = context.getSharedPreferences("downLoadFileName", 0);
        editor = sharedpreferences.edit();
        hashmap = (HashMap) sharedpreferences.getAll();
        iterator = hashmap.entrySet().iterator();
        long l = sharedpreferences.getLong("preTime", 0L);
        l1 = (System.currentTimeMillis() - l) / 0x36ee80L;
        i = sharedpreferences.getInt("times", 0);
        if (i != 0 && l1 < 4L) return;
        if (hashmap.size() <= 3) return;
        while (iterator.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            String s = (String) entry.getKey();
            if (!s.equals("isFirst") && !s.equals("times") && !s.equals("preTime")) {
                String s1 = (String) entry.getValue();
                File file = new File(new StringBuilder("sdcard/").append(s1).toString());
                if (!Tools.apkExits(context, s) && file.exists()) {
                    editor.putLong("preTime", System.currentTimeMillis());
                    editor.putInt("times", i + 1);
                    editor.commit();
                    Tools.openFile(context, file, s);
                }
            }
        }
        editor.putLong("preTime", System.currentTimeMillis());
        editor.commit();
        return;
    }
    public void present(Context context1) {
        context = context1;
        openUninstallapk();
    }
}
