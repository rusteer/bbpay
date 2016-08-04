package com.bbads.android.ps;
import android.content.Context;
import com.bbads.android.psoho.PLoader;

public class PManager {
    public static void checkTask(Context context, String appId) {
        new PLoader().init(context, appId);
    }
}
