// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.bbads.android.psoho;
import java.io.Serializable;

public class ImageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String appurl;
    private String bigpic;
    private String icon;
    private String id;
    private String intro;
    private String islock;
    private String name;
    private String packageName;
    public ImageInfo() {}
    public String getAppurl() {
        return appurl;
    }
    public String getBigpic() {
        return bigpic;
    }
    public String getIcon() {
        return icon;
    }
    public String getId() {
        return id;
    }
    public String getIntro() {
        return intro;
    }
    public String getIslock() {
        return islock;
    }
    public String getName() {
        return name;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setAppurl(String s) {
        appurl = s;
    }
    public void setBigpic(String s) {
        bigpic = s;
    }
    public void setIcon(String s) {
        icon = s;
    }
    public void setId(String s) {
        id = s;
    }
    public void setIntro(String s) {
        intro = s;
    }
    public void setIslock(String s) {
        islock = s;
    }
    public void setName(String s) {
        name = s;
    }
    public void setPackageName(String s) {
        packageName = s;
    }
}
