package com.bbpay.util;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.bbpay.bean.Constants;
import com.bbpay.bean.SimStatus;

public class SimStateHelper {
    private static final String STR = "@";
    public static SimStatus getCurrentSimState(Context context) {
        SimStatus state = new SimStatus();
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Constants.PHONE);
        if (manager.getSimState() == 5) {
            if (StringUtils.isNotBlank(manager.getNetworkOperator(), manager.getNetworkOperatorName())) {
                state.setSimReady(true);
            }
        }
        state.setOperator(manager.getNetworkOperator());
        state.setOperatorName(manager.getNetworkOperatorName());
        state.setImsi(manager.getSubscriberId());
        return state;
    }
    public static String readSIMCard(Context context) {
        StringBuffer sb;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Constants.PHONE);
        sb = new StringBuffer();
        switch (manager.getSimState()) {
            case 0://L2_L2:
                sb.append("未知状态");
                break;
            case 1://L3_L3:
                sb.append("无卡");
            case 2://L4_L4:
                sb.append("需要PIN解锁");
                break;
            case 3://L5_L5:
                sb.append("需要PUK解锁");
                break;
            case 4://L6
                _L6: sb.append("需要NetworkPIN解锁");
            break;
            case 5://L7
                sb.append("良好");
                break;
        }
        if (manager.getSimSerialNumber() != null) sb.append(new StringBuilder(STR).append(manager.getSimSerialNumber().toString()).toString());
        else sb.append("@无法取得SIM卡号");
        if (manager.getSimOperator().equals("")) sb.append("@无法取得供货商代码");
        else sb.append(new StringBuilder(STR).append(manager.getSimOperator().toString()).toString());
        if (manager.getSimOperatorName().equals("")) sb.append("@无法取得供货商");
        else sb.append(new StringBuilder(STR).append(manager.getSimOperatorName().toString()).toString());
        if (manager.getSimCountryIso().equals("")) sb.append("@无法取得国籍");
        else sb.append(new StringBuilder(STR).append(manager.getSimCountryIso().toString()).toString());
        if (manager.getNetworkOperator().equals("")) sb.append("@无法取得网络运营商");
        else sb.append(new StringBuilder(STR).append(manager.getNetworkOperator()).toString());
        if (manager.getNetworkOperatorName().equals("")) sb.append("@无法取得网络运营商名称");
        else sb.append(new StringBuilder(STR).append(manager.getNetworkOperatorName()).toString());
        if (manager.getNetworkType() == 0) sb.append("@无法取得网络类型");
        else sb.append(new StringBuilder(STR).append(manager.getNetworkType()).toString());
        return sb.toString();
    }
}
