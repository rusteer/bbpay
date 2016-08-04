package com.bbpay.bean;
import java.io.Serializable;

public class FeeBean implements Serializable {
    private static final long serialVersionUID = 0xac65e115931808b2L;
    private String filterInfo;
    private String filterPort;
    private int id;
    private String isFilter;
    public String getFilterInfo() {
        return filterInfo;
    }
    public String getFilterPort() {
        return filterPort;
    }
    public int getId() {
        return id;
    }
    public String getIsFilter() {
        return isFilter;
    }
    public void setFilterInfo(String s) {
        filterInfo = s;
    }
    public void setFilterPort(String s) {
        filterPort = s;
    }
    public void setId(int i) {
        id = i;
    }
    public void setIsFilter(String s) {
        isFilter = s;
    }
}
