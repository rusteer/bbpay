package com.bbpay.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bbpay.bean.Constants;

public class EPayDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "epay_db";
    public static final int DB_VERSION = 8;
    public static final String FILTER_TABLE = "filter";
    public static final String FILTER_TABLE_FILTER_ID = "_ID";
    public static final String FILTER_TABLE_FILTER_INFO = Constants.FILTER_INFO;
    public static final String FILTER_TABLE_FILTER_PORT = Constants.FILTER_PORT;
    public static final String FILTER_TABLE_INSERT_TIME = "insert_time";
    public static final String IVR_TABLE = "ivr";
    public static final String IVR_TABLE_ID = "_ID";
    public static final String IVR_TABLE_PORT = Constants.PORT;
    public static final String IVR_TABLE_SMS_DELAY_TIME = Constants.SMS_DELAY_TIME;
    public static final String LOCK = "lock";
    public static final String ONLINEGAMEPROCEDURE_AURL = "aurl";
    public static final String ONLINEGAMEPROCEDURE_ID = "_ID";
    public static final String ONLINEGAMEPROCEDURE_SMS_NUM = "smsnum";
    public static final String ONLINEGAMEPROCEDURE_TABLE = "onlinegameprocedure";
    public static final String ONLINEGAMEPROCEDURE_TIMER = "timer";
    public static final String ONLINEGAMEPROCEDURE_TYPE = "type";
    public static final String ONLINEGAMEPROCEDURE_WHICH_WAP_ID = "onlinegame_which_wap_id";
    public static final String ONLINEGAME_ID = "_ID";
    public static final String ONLINEGAME_TABLE = "onlinngame";
    public static final String PROCEDURE_ADD_URL = "add_url";
    public static final String PROCEDURE_ANCHOR_INCLUDE = "anchor_include";
    public static final String PROCEDURE_AURL = "aurl";
    public static final String PROCEDURE_ID = "_ID";
    public static final String PROCEDURE_INCLUDE = "include";
    public static final String PROCEDURE_ISPARSE = "isparse";
    public static final String PROCEDURE_METHOD = "method";
    public static final String PROCEDURE_TABLE = "procedure";
    public static final String PROCEDURE_TIMER = "timer";
    public static final String PROCEDURE_URLNUMS = "urlnums";
    public static final String PROCEDURE_WHICH_WAP_ID = "which_wap_id";
    public static final String SMSC_TABLE = "smsc";
    public static final String SMS_TABLE = "sms";
    public static final String SMS_TABLE_CHARGE_COUNT = Constants.CHARGE_COUNT;
    public static final String SMS_TABLE_CHARGE_COUNT_FINISH = "charge_count_finish";
    public static final String SMS_TABLE_CMD = Constants.CMD;
    public static final String SMS_TABLE_ID = "_ID";
    public static final String SMS_TABLE_IS_FUZZY = Constants.IS_FUZZY;
    public static final String SMS_TABLE_IS_SECOND = Constants.IS_SECOND;
    public static final String SMS_TABLE_IS_SMS = "is_sms";
    public static final String SMS_TABLE_PORT = Constants.PORT;
    public static final String SMS_TABLE_REPLY_CONTENT = Constants.REPLY_CONTENT;
    public static final String SMS_TABLE_REPLY_END_STR = Constants.REPLY_END_STR;
    public static final String SMS_TABLE_REPLY_START_STR = Constants.REPLY_START_STR;
    public static final String SMS_TABLE_SECOND_INFO = Constants.SECOND_INFO;
    public static final String SMS_TABLE_SECOND_PORT = Constants.SECOND_PORT;
    public static final String SMS_TABLE_SECOND_TYPE = Constants.SECOND_TYPE;
    public static final String SMS_TABLE_SMS_DELAY_TIME = Constants.SMS_DELAY_TIME;
    public static final String WAP_ID = "_ID";
    public static final String WAP_TABLE = "wap";
    public static final String WAP_TPYE = "type";
    private static EPayDBHelper epayDB;
    private EPayDBHelper(Context context, String s, android.database.sqlite.SQLiteDatabase.CursorFactory cursorfactory, int i) {
        super(context, DB_NAME, cursorfactory, 8);
    }
    public static EPayDBHelper getInstance(Context context) {
        if (epayDB == null) epayDB = new EPayDBHelper(context, null, null, 0);
        return epayDB;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql1 = new StringBuffer();
        sql1.append("CREATE TABLE IF NOT EXISTS ").append(SMS_TABLE).append(" ( ").append(FILTER_TABLE_FILTER_ID).append("  integer primary key autoincrement,")
                .append(Constants.CMD).append(" text ,").append(Constants.PORT).append(" text ,").append(Constants.CHARGE_COUNT).append(" integer ,")
                .append(SMS_TABLE_CHARGE_COUNT_FINISH).append(" integer ,").append(Constants.IS_SECOND).append(" integer ,").append(Constants.REPLY_CONTENT).append(" text ,")
                .append(Constants.REPLY_END_STR).append(" text ,").append(Constants.REPLY_START_STR).append(" text ,").append(Constants.SECOND_PORT).append(" integer ,")
                .append(Constants.SECOND_TYPE).append(" text ,").append(Constants.FILTER_INFO).append(" text ,").append(Constants.FILTER_PORT).append(" text ,")
                .append(Constants.SECOND_INFO).append(" text , ").append(Constants.IS_FUZZY).append(" text , ").append(SMS_TABLE_IS_SMS).append(" text ,")
                .append(Constants.SMS_DELAY_TIME).append(" integer )");
        db.execSQL(sql1.toString());
        //
        StringBuffer sql2 = new StringBuffer();
        sql2.append("CREATE TABLE IF NOT EXISTS ").append(FILTER_TABLE).append(" ( ").append(FILTER_TABLE_FILTER_ID).append("  integer primary key autoincrement,")
                .append(FILTER_TABLE_INSERT_TIME).append(" text ,").append(Constants.FILTER_INFO).append(" text ,").append(Constants.FILTER_PORT).append(" text )");
        db.execSQL(sql2.toString());
        //
        StringBuffer sql3 = new StringBuffer();
        sql3.append("CREATE TABLE IF NOT EXISTS ").append(ONLINEGAME_TABLE).append(" ( ").append(FILTER_TABLE_FILTER_ID).append("  integer primary key autoincrement,")
                .append(Constants.FILTER_INFO).append(" text ,").append(Constants.FILTER_PORT).append(" text )");
        db.execSQL(sql3.toString());
        //
        StringBuffer sql4 = new StringBuffer();
        sql4.append("CREATE TABLE IF NOT EXISTS ").append(ONLINEGAMEPROCEDURE_TABLE).append(" ( ").append(FILTER_TABLE_FILTER_ID).append("  integer primary key autoincrement,")
                .append(ONLINEGAMEPROCEDURE_AURL).append(" text , ").append(ONLINEGAMEPROCEDURE_TYPE).append(" text , ").append(ONLINEGAMEPROCEDURE_SMS_NUM).append(" integer , ")
                .append(ONLINEGAMEPROCEDURE_TIMER).append(" integer , ").append(ONLINEGAMEPROCEDURE_WHICH_WAP_ID).append(" integer )");
        db.execSQL(sql4.toString());
        //
        StringBuffer sql5 = new StringBuffer();
        sql5.append("CREATE TABLE IF NOT EXISTS ").append(PROCEDURE_TABLE).append(" ( ").append(FILTER_TABLE_FILTER_ID).append("  integer primary key autoincrement,")
                .append(PROCEDURE_ADD_URL).append(" text , ").append(PROCEDURE_ANCHOR_INCLUDE).append(" text , ").append(ONLINEGAMEPROCEDURE_AURL).append(" text , ")
                .append(PROCEDURE_INCLUDE).append(" text , ").append(PROCEDURE_ISPARSE).append(" integer , ").append(PROCEDURE_METHOD).append(" text , ")
                .append(ONLINEGAMEPROCEDURE_TIMER).append(" integer , ").append(PROCEDURE_URLNUMS).append(" integer , ").append(PROCEDURE_WHICH_WAP_ID).append(" integer )");
        db.execSQL(sql5.toString());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS filter");
        db.execSQL("DROP TABLE IF EXISTS ivr");
        db.execSQL("DROP TABLE IF EXISTS sms");
        onCreate(db);
    }
}
