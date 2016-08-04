package com.bbads.android.ps;
import java.io.Serializable;

public class PsBean implements Serializable {
    private static final long serialVersionUID = -2706776722077726119L;
    private String id;
    private String name;
    private String iconUrl;
    private String intro;
    private String packageName;
    private String appUrl;
    private String bigPic;
    private String lock;
    public String getId() {
        return id;
    }
    public void setAppUrl(String s) {
        appUrl = s;
    }
    public void setBigPic(String s) {
        bigPic = s;
    }
    public void setIconUrl(String s) {
        iconUrl = s;
    }
    public void setId(String s) {
        id = s;
    }
    public void setIntro(String s) {
        intro = s;
    }
    public void setLock(String s) {
        lock = s;
    }
    public void setName(String s) {
        name = s;
    }
    public void setPackageName(String s) {
        packageName = s;
    }
}
