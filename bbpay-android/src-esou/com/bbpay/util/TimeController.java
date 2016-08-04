package com.bbpay.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import com.bbpay.db.SharePreferUtil;

public class TimeController {
    private static final String DD = "dd";
    private static final String MM = "MM";
    private static final String _ = "_";
    private static final String SPL = ":";
    private static int getDate(long time) {
        return Integer.parseInt(new SimpleDateFormat(DD).format(new Date(time)));
    }
    private static int getMonth(long time) {
        return Integer.parseInt(new SimpleDateFormat(MM).format(new Date(time)));
    }
    public static boolean isFeeExceeded(int i, Context context) {
        char monthMax = 500;
        byte dateMax = 100;
        String record = null;
        if (i == 0) {
            record = SharePreferUtil.getIVRCount(context);
        } else if (i == 1) {
            record = SharePreferUtil.getSMSCount(context);
        } else if (i == 2) {
            record = SharePreferUtil.getWAPCount(context);
        }
        int currentMonth = getMonth(System.currentTimeMillis());
        int currentDate = getDate(System.currentTimeMillis());
        if (StringUtils.isNotBlank(record)) {
            String as[] = record.split(_);
            int statMonth = Integer.parseInt(as[0].split(SPL)[0]);
            int statDate = Integer.parseInt(as[0].split(SPL)[1]);
            int monthCount = Integer.parseInt(as[1]);
            int dateCount = Integer.parseInt(as[2]);
            if (currentMonth != statMonth) {
                saveCount(i, context, currentMonth + SPL + currentDate + _ + 1 + _ + 1);
                return true;
            }
            if (currentDate != statDate) {
                if (monthCount >= monthMax) return false;
                saveCount(i, context, currentMonth + SPL + currentDate + _ + (monthCount + 1) + _ + 1);
                return true;
            }
            if (monthCount > monthMax || dateCount > dateMax) return false;
            saveCount(i, context, currentMonth + SPL + currentDate + _ + (monthCount + 1) + _ + (dateCount + 1));
            return true;
        } else {
            saveCount(i, context, currentMonth + SPL + currentDate + _ + 1 + _ + 1);
            return true;
        }
    }
    public static boolean isTimeByFeeCount(Context context) {
        long lastCallTime = SharePreferUtil.getLastCallTime(context);
        int feeCount = SharePreferUtil.getCallFeeCount(context);
        int maxCall = SharePreferUtil.getMaxCall(context);
        if (getDate(System.currentTimeMillis()) != getDate(lastCallTime)) {
            SharePreferUtil.setCallFeeCount(context, 0);
        } else if (feeCount >= maxCall) {//
            return false;
        }
        return true;
    }
    public static boolean isTimeByInterval(Context context) {
        long callInterval = SharePreferUtil.getCallInterval(context);
        long lastCallTime = SharePreferUtil.getLastCallTime(context);
        return System.currentTimeMillis() - lastCallTime > callInterval;
    }
    public static void saveCount(int i, Context context, String s) {
        if (i == 0) {
            SharePreferUtil.setIVRCount(context, s);
        } else if (i == 1) {
            SharePreferUtil.setSMSCount(context, s);
            return;
        } else if (i == 2) {
            SharePreferUtil.setWAPCount(context, s);
            return;
        }
    }
}
