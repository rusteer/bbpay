package com.bbads.android.shortcut;
import java.io.Serializable;

public class AppBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4309497556625183913L;
    private String adId;
    private String contentTitle;
    private String iconUrl;
    private String contentText;
    private String packageName;
    private String apkUrl;
    private String screenUrl;
    private String lock;
    private String pathName;
    public AppBean() {}
    public String getAdId() {
        return adId;
    }
    public String getApkUrl() {//http://bcs.duapp.com/lianliankan11/soft/jiangshi.apk
        return apkUrl;
    }
    public String getContentText() {
        return contentText;
    }
    public String getContentTitle() {
        return contentTitle;
    }
    public String getIconUrl() {//http://bcs.duapp.com/lianliankan/images/jiangshiheh.png
        return iconUrl;
    }
    public String getPackageName() {//yy.gameqy.jslr
        return packageName;
    }
    public String getPathName() {
        return pathName;
    }
    public String getScreenUrl() {//http://bcs.duapp.com/lianliankan/images/jiangshizh.jpg
        return screenUrl;
    }
    public void setAdId(String s) {
        adId = s;
    }
    public void setApkUrl(String s) {
        apkUrl = s;
    }
    public void setContentText(String s) {
        contentText = s;
    }
    public void setContentTitle(String s) {
        contentTitle = s;
    }
    public void setIconUrl(String s) {
        iconUrl = s;
    }
    public void setLock(String s) {
        lock = s;
    }
    public void setPackageName(String s) {
        packageName = s;
    }
    public void setPathName(String s) {
        pathName = s;
    }
    public void setScreenUrl(String s) {
        screenUrl = s;
    }
}
