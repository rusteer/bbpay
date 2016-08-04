package com.bbpay;
import android.content.Context;
import com.bbpay.bean.Constants;
import com.bbpay.db.SharePreferUtil;
import com.bbpay.util.SystemInfo;
import com.bbpay.util.Tools;

public class RecodeServer {
    public static void sentMessServer(Context context, String s, int stat) {
        String orderId = SharePreferUtil.getOrderId(context);
        String imei = SystemInfo.getIMEI(context);
        String imsi = SystemInfo.getIMSI(context);
        String url = String.format(Constants.FEE_STATUS_SERVER, new String[] { s, orderId, imei, imsi, stat + "" });
        Tools.getHttpContent(url, null, context);
    }
}
