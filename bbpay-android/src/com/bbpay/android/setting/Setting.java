package com.bbpay.android.setting;
public class Setting {
    private static String LOCAL_SERVER_URL = "http://192.168.0.105:16001/p/";
    private static String hostName = "localhost";
    //private static String hostName = "hysdk.haowagame.com";
    private static String SERVER_URL = "http://" + hostName + "/xyz";
    public static String getServerUrl() {
        if (hostName.contains("host")) return LOCAL_SERVER_URL;
        return SERVER_URL;
    }
}
