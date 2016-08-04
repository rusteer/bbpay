package com.bbpay.sms.filtersms;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

public class SmsObserver extends ContentObserver {
    public static SmsObserver INSTANCE;
    private Context context;
    private SmsFilter filter;
    private SmsObserver(Handler handler) {
        super(handler);
        filter = new SmsFilter();
    }
    public static SmsObserver getInstanse(Handler handler) {
        if (INSTANCE == null) INSTANCE = new SmsObserver(handler);
        return INSTANCE;
    }
    public SmsFilter getFilter() {
        return filter;
    }
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        try {
            if (context != null && filter != null) {
                filter.checkSmsInbox();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void setContext(Context context) {
        this.context = context;
        filter.setContext(context);
    }
}
