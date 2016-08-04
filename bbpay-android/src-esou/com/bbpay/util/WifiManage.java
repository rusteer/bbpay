package com.bbpay.util;
import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiManage {
    private static final String LOG_TAG = WifiManage.class.getName();
    private List mWifiConfiguration;
    private WifiInfo mWifiInfo;
    private List mWifiList;
    android.net.wifi.WifiManager.WifiLock mWifiLock;
    private WifiManager mWifiManager;
    public WifiManage(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(SystemInfo.NETWORK_TYPE_WIFI);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }
    public static String getBt(Context context) {
        String s = "";
        String s1;
        int i;
        String s2;
        s1 = ((WifiManager) context.getSystemService(SystemInfo.NETWORK_TYPE_WIFI)).getConnectionInfo().getMacAddress();
        i = 0;
        while (i < s1.length()) {
            char c = s1.charAt(i);
            if (c != ':') {
                s2 = new StringBuilder(String.valueOf(s)).append(c).toString();
                s = s2;
                i++;
            }
        }
        return s;
    }
    public void AcquireWifiLock() {
        mWifiLock.acquire();
    }
    public void AddNetwork(WifiConfiguration wificonfiguration) {
        int i = mWifiManager.addNetwork(wificonfiguration);
        mWifiManager.enableNetwork(i, true);
    }
    public void CloseWifi() {
        if (mWifiManager.isWifiEnabled()) mWifiManager.setWifiEnabled(false);
    }
    public void ConnectConfiguration(int i) {
        if (i > mWifiConfiguration.size()) {
            return;
        } else {
            mWifiManager.enableNetwork(((WifiConfiguration) mWifiConfiguration.get(i)).networkId, true);
            return;
        }
    }
    public void CreatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }
    public void DisconnectWifi(int i) {
        mWifiManager.disableNetwork(i);
        mWifiManager.disconnect();
    }
    public String GetBSSID() {
        if (mWifiInfo == null) return "NULL";
        else return mWifiInfo.getBSSID();
    }
    public List GetConfiguration() {
        return mWifiConfiguration;
    }
    public int GetIPAddress() {
        if (mWifiInfo == null) return 0;
        else return mWifiInfo.getIpAddress();
    }
    public String GetMacAddress() {
        if (mWifiInfo == null) return "NULL";
        else return mWifiInfo.getMacAddress();
    }
    public int GetNetworkId() {
        if (mWifiInfo == null) return 0;
        else return mWifiInfo.getNetworkId();
    }
    public String GetWifiInfo() {
        if (mWifiInfo == null) return "NULL";
        else return mWifiInfo.toString();
    }
    public List GetWifiList() {
        return mWifiList;
    }
    public boolean isWifiManageEnable() {
        MyLogger.info(LOG_TAG, new StringBuilder("WIFImanageEnable is ").append(mWifiManager.isWifiEnabled()).toString());
        return mWifiManager.isWifiEnabled();
    }
    public StringBuilder LookUpScan() {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        do {
            if (i >= mWifiList.size()) return stringbuilder;
            stringbuilder.append(new StringBuilder("Index_").append(new Integer(i + 1).toString()).append(":").toString());
            stringbuilder.append(((ScanResult) mWifiList.get(i)).toString());
            stringbuilder.append("\n");
            i++;
        } while (true);
    }
    public void OpenWifi() {
        if (!mWifiManager.isWifiEnabled()) mWifiManager.setWifiEnabled(true);
    }
    public void ReleaseWifiLock() {
        if (mWifiLock.isHeld()) mWifiLock.acquire();
    }
    public void StartScan() {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }
}
