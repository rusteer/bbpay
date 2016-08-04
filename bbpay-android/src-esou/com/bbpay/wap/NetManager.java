package com.bbpay.wap;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.bbpay.bean.Constants;
import com.bbpay.util.MyLogger;
import com.bbpay.util.SystemInfo;
import com.bbpay.util.WifiManage;

public class NetManager {
    public static String CMNET = "cmnet";
    public static String CMWAP = "cmwap";
    public static String GNET_3 = "3gnet";
    public static String GWAP_3 = "3gwap";
    public static String UNINET = "uninet";
    public static String UNIWAP = "uniwap";
    private Uri APN_TABLE_URI;
    private Uri PREFERRED_APN_URI;
    private int cmwapApnId;
    private Context context;
    private int creatNewApnId;
    private int currentApnId;
    private int lastApnid;
    private TelephonyManager tm;
    public NetManager(Context context1) {
        context = context1;
        PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
        APN_TABLE_URI = Uri.parse("content://telephony/carriers");
    }
    public int addCmwapAPN() {
        Cursor cursor;
        int i = 0;
        ContentResolver contentresolver = context.getContentResolver();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("name", "cmwap");
        contentvalues.put("apn", "cmwap");
        contentvalues.put("proxy", "10.0.0.172");
        contentvalues.put(Constants.PORT, "80");
        tm = (TelephonyManager) context.getSystemService(Constants.PHONE);
        String s = tm.getSubscriberId();
        Uri uri;
        if (s != null && !s.trim().equals("")) if (s.startsWith("46000")) {
            contentvalues.put("numeric", "46000");
            contentvalues.put("mcc", "460");
            contentvalues.put("mnc", "00");
        } else if (s.startsWith("46002")) {
            contentvalues.put("numeric", "46002");
            contentvalues.put("mcc", "460");
            contentvalues.put("mnc", "02");
        } else if (s.startsWith("46007")) {
            contentvalues.put("numeric", "46007");
            contentvalues.put("mcc", "460");
            contentvalues.put("mnc", "07");
        } else {
            contentvalues.put("numeric", s);
            contentvalues.put("mcc", s.substring(0, 3));
            contentvalues.put("mnc", s.substring(3));
        }
        contentvalues.put("mmsc", "http://mmsc.monternet.com");
        contentvalues.put("mmsproxy", "10.0.0.172");
        contentvalues.put("mmsport", "80");
        cursor = null;
        uri = contentresolver.insert(APN_TABLE_URI, contentvalues);
        cursor = null;
        if (uri != null) {
            cursor = contentresolver.query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    i = cursor.getInt(cursor.getColumnIndex("_id"));
                    creatNewApnId = i;
                    if (setAPN(i)) {
                        cmwapApnId = i;
                    } else {
                        i = 0;
                    }
                }
            }
        }
        cursor.close();
        if (cursor != null) cursor.close();
        return i;
    }
    public int addCtwapAPN() {
        int i = 0;
        ContentResolver contentresolver;
        ContentValues contentvalues;
        String s;
        contentresolver = context.getContentResolver();
        contentvalues = new ContentValues();
        contentvalues.put("name", "ctwap");
        contentvalues.put("apn", "ctwap");
        contentvalues.put("proxy", "10.0.0.200");
        contentvalues.put(Constants.PORT, "80");
        tm = (TelephonyManager) context.getSystemService(Constants.PHONE);
        s = tm.getSubscriberId();
        if (s != null && !s.trim().equals("")) {
            //L1_L1:
            if (s.startsWith("46003")) {
                //L3_L3:
                contentvalues.put("numeric", "46003");
                contentvalues.put("mcc", "460");
                contentvalues.put("mnc", "03");
            } else {
                //L4_L4:
                if (s.startsWith("46005")) {
                    contentvalues.put("numeric", "46005");
                    contentvalues.put("mcc", "460");
                    contentvalues.put("mnc", "05");
                }
            }
        }
        //L2
        Cursor cursor;
        contentvalues.put("mmsproxy", "10.0.0.200");
        contentvalues.put("mmsport", "80");
        cursor = null;
        Uri uri = contentresolver.insert(APN_TABLE_URI, contentvalues);
        cursor = null;
        if (uri != null) {
            //L5_L5:
            cursor = contentresolver.query(uri, null, null, null, null);
            if (cursor != null) {
                //L7_L7:
                if (cursor.moveToFirst()) {
                    //L8_L8:
                    i = cursor.getInt(cursor.getColumnIndex("_id"));
                    creatNewApnId = i;
                    if (setAPN(i)) {
                        //L9_L9:
                        cmwapApnId = i;
                    } else {
                        //L10_L10:
                        i = 0;
                    }
                }
            }
        }
        if (cursor != null) cursor.close();
        return i;
    }
    public int addUniwapAPN() {
        int i = 0;
        ContentResolver contentresolver;
        ContentValues contentvalues;
        String s;
        contentresolver = context.getContentResolver();
        contentvalues = new ContentValues();
        contentvalues.put("name", "uniwap");
        contentvalues.put("apn", "uniwap");
        contentvalues.put("proxy", "10.0.0.172");
        contentvalues.put(Constants.PORT, "80");
        tm = (TelephonyManager) context.getSystemService(Constants.PHONE);
        s = tm.getSubscriberId();
        if (s != null && !s.trim().equals("")) {
            //L1_L1:
            if (s.startsWith("46001")) {
                //L3_L3:
                contentvalues.put("numeric", "46001");
                contentvalues.put("mcc", "460");
                contentvalues.put("mnc", "01");
            } else {
                //L4_L4:
                if (s.startsWith("46006")) {
                    contentvalues.put("numeric", "46006");
                    contentvalues.put("mcc", "460");
                    contentvalues.put("mnc", "06");
                }
            }
        }
        //_L2:
        Cursor cursor;
        contentvalues.put("mmsproxy", "10.0.0.172");
        contentvalues.put("mmsport", "80");
        cursor = null;
        Uri uri = contentresolver.insert(APN_TABLE_URI, contentvalues);
        cursor = null;
        if (uri != null) {
            //L5_L5:
            cursor = contentresolver.query(uri, null, null, null, null);
            if (cursor != null) {
                //L7_L7:
                if (cursor.moveToFirst()) {
                    //L8_L8:
                    i = cursor.getInt(cursor.getColumnIndex("_id"));
                    creatNewApnId = i;
                    if (setAPN(i)) {
                        //L9_L9:
                        cmwapApnId = i;
                    } else {
                        //L10_L10:
                        i = 0;
                    }
                }
            }
        }
        if (cursor != null) cursor.close();
        return i;
    }
    public int addWapApn(Context context1) {
        if (SystemInfo.getCardType(context1).equals(Constants.MOBILE)) return addCmwapAPN();
        else return 0;
    }
    public boolean checkHasWapAPN() {
        Cursor cursor = context.getContentResolver().query(APN_TABLE_URI, null, null, null, "_id ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int i;
                String s;
                String s1;
                String s2;
                String s3;
                String s4;
                i = cursor.getInt(cursor.getColumnIndex("_id"));
                s = cursor.getString(cursor.getColumnIndex("name"));
                s1 = cursor.getString(cursor.getColumnIndex(Constants.PORT));
                s2 = cursor.getString(cursor.getColumnIndex("proxy"));
                s3 = cursor.getString(cursor.getColumnIndex("current"));
                s4 = cursor.getString(cursor.getColumnIndex("apn"));
                if (s == null || s2 == null || s1 == null || s3 == null || s4 == null) continue;
                if (!s2.equals("10.0.0.172") && !s2.equals("010.000.000.172") && !s2.equals("10.0.0.200") && !s2.equals("010.000.000.200") || !s1.equals("80") || !s3.equals("1")) continue;
                cmwapApnId = i;
                MyLogger.info("checkHasWapAPN", new StringBuilder("cmwapApnId=").append(cmwapApnId).toString());
                return true;
            }
        }
        return false;
    }
    public boolean checkNetworkConnection(Context context1) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context1.getSystemService("connectivity");
        NetworkInfo networkinfo = connectivitymanager.getNetworkInfo(1);
        connectivitymanager.getNetworkInfo(0);
        return networkinfo.isAvailable();
    }
    public int getCmwapApnId() {
        return cmwapApnId;
    }
    public int getCreatNewApnId() {
        return creatNewApnId;
    }
    public int getCurrentApnId() {
        return currentApnId;
    }
    public int getCurrentWapAPNId() {
        Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
        showAPN(cursor);
        if (cursor != null && cursor.moveToFirst()) currentApnId = cursor.getInt(cursor.getColumnIndex("_id"));
        return currentApnId;
    }
    public boolean isCurrentWapAPN() {
        Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
        if (cursor == null) return false;
        String s;
        String s1;
        String s2;
        String s3;
        if (!cursor.moveToFirst()) return false;
        s = cursor.getString(cursor.getColumnIndex("proxy"));
        s1 = cursor.getString(cursor.getColumnIndex("apn"));
        s2 = cursor.getString(cursor.getColumnIndex(Constants.PORT));
        s3 = cursor.getString(cursor.getColumnIndex("current"));
        currentApnId = cursor.getInt(cursor.getColumnIndex("_id"));
        MyLogger.info(
                "isCurrentWapAPN",
                new StringBuilder("proxy=").append(s).append(" , apn=").append(s1).append(" port=").append(s2).append(" , current=").append(s3).append(" , _id=")
                .append(currentApnId).toString());
        if (s == null || s1 == null || s2 == null || s3 == null) return false;
        if (!s.equals("10.0.0.172") && !s.equals("010.000.000.172") && !s.equals("10.0.0.200") && !s.equals("010.000.000.200") || !s2.equals("80") || !s3.equals("1")) return false;
        MyLogger.info("isCurrentWapAPN", "CurrentWapAPNd is cmwap");
        return true;
    }
    public boolean isDataConnected() {
        return ((TelephonyManager) context.getSystemService(Constants.PHONE)).getDataState() == 2;
    }
    public void reSetLastApn() {
        if (lastApnid != 0) {
            setAPN(lastApnid);
            lastApnid = 0;
        }
    }
    public boolean setAPN(int i) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("apn_id", Integer.valueOf(i));
        MyLogger.info("", new StringBuilder("切换APN ").append(i).toString());
        try {
            context.getContentResolver().update(PREFERRED_APN_URI, contentvalues, null, null);
            currentApnId = i;
        } catch (Exception exception) {
            MyLogger.info("===", "切换APN失败");
            return false;
        }
        return true;
    }
    public boolean setWapNetWork() {
        WifiManage wifimanage = new WifiManage(context);
        if (wifimanage.isWifiManageEnable()) wifimanage.CloseWifi();
        if (isCurrentWapAPN()) return true;
        int i;
        lastApnid = getCurrentApnId();
        if (checkHasWapAPN()) {
            i = getCmwapApnId();
            MyLogger.info("SetCMWAPActivity", new StringBuilder("checkHasWapAPN is true , cmwapid is ").append(i).toString());
            return setAPN(i);
        } else {
            return addWapApn(context) != 0;
        }
    }
    public void showAllAPN() {
        showAPN(context.getContentResolver().query(APN_TABLE_URI, null, "current='1'", null, null));
    }
    private void showAPN(Cursor cursor) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("name"));
                String s1 = cursor.getString(cursor.getColumnIndex("numeric"));
                String s2 = cursor.getString(cursor.getColumnIndex("_id"));
                String s3 = cursor.getString(cursor.getColumnIndex("mcc"));
                String s4 = cursor.getString(cursor.getColumnIndex("mnc"));
                String s5 = cursor.getString(cursor.getColumnIndex("apn"));
                String s6 = cursor.getString(cursor.getColumnIndex("user"));
                String s7 = cursor.getString(cursor.getColumnIndex("server"));
                String s8 = cursor.getString(cursor.getColumnIndex("password"));
                String s9 = cursor.getString(cursor.getColumnIndex("proxy"));
                String s10 = cursor.getString(cursor.getColumnIndex(Constants.PORT));
                String s11 = cursor.getString(cursor.getColumnIndex("mmsproxy"));
                String s12 = cursor.getString(cursor.getColumnIndex("mmsport"));
                String s13 = cursor.getString(cursor.getColumnIndex("mmsc"));
                String s14 = cursor.getString(cursor.getColumnIndex("authtype"));
                String s15 = cursor.getString(cursor.getColumnIndex("type"));
                String s16 = cursor.getString(cursor.getColumnIndex("current"));
                MyLogger.info("AllWapAPN",
                        new StringBuilder("name=").append(s).append(" numeric=").append(s1).append(" _id=").append(s2).append(" mcc=").append(s3).append(" mnc=").append(s4)
                                .append(" apn=").append(s5).append(" user=").append(s6).append(" server=").append(s7).append(" password=").append(s8).append(" proxy=").append(s9)
                                .append(" port=").append(s10).append(" mmsproxy=").append(s11).append(" mmsport=").append(s12).append(" mmsc=").append(s13).append(" authtype=")
                                .append(s14).append(" type=").append(s15).append(" current=").append(s16).toString());
            }
        }
    }
}
