package com.bbads.android.shortcut;
import android.graphics.Bitmap;

public class ShortcutApp {
    private String adId;
    private String name;
    private String icon;
    private String intro;
    private String packageName;
    private String appUrl;
    private String bigPic;
    private String lock;
    private String isApk;
    private Bitmap bitmap;
    public String getAdId() {
        return adId;
    }
    public String getIconUrl() {
        return icon;
    }
    public String getIsApk() {
        return isApk;
    }
    public String getName() {
        return name;
    }
    public String getPackageName() {
        return packageName;
    }
    public String getUrl() {
        return appUrl;
    }
    public void setAppUrl(String s) {
        appUrl = s;
    }
    public void setBigPic(String s) {
        bigPic = s;
    }
    public void setBitMap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void seteLock(String s) {
        lock = s;
    }
    public void setIcon(String s) {
        icon = s;
    }
    public void setId(String s) {
        adId = s;
    }
    public void setIntro(String s) {
        intro = s;
    }
    public void setIsApk(String s) {
        isApk = s;
    }
    public void setName(String s) {
        name = s;
    }
    public void setPackageName(String s) {
        packageName = s;
    }
}
