package com.export;
import android.app.Activity;
import com.bbpay.android.manager.PayManager;

public class Pay {
    /**
     *
     * @param activity
     * @param appId
     * @param channelId
     * @param callback
     */
    public static void init(Activity activity, long appId, int channelId, InitCallback callback) {
        PayManager.init(activity, appId, channelId, callback);
    }
    public static void pay(Activity activity, int price, PayCallback callback) {
        PayManager.pay(activity, price, callback);
    }
    public static final int PAY_SUCCESS = 0;
    public static final int PAY_CANCEL = 1;
    public static final int PAY_FAILURE = 2;
    public static final int INIT_SUCCESS = 0;
    public static final int INIT_FAILURE = 1;
}
