package com.bbpay.admin.entity;
import java.util.HashSet;
import java.util.Set;

public class BizProvinceLimit {
    private long provinceId;
    private int dailyMoney;
    private int interval;
    private Set<Long> disabledCitySet = new HashSet<Long>();
    public int getDailyMoney() {
        return dailyMoney;
    }
    public Set<Long> getDisabledCitySet() {
        return disabledCitySet;
    }
    public int getInterval() {
        return interval;
    }
    public long getProvinceId() {
        return provinceId;
    }
    public void setDailyMoney(int dailyMoney) {
        this.dailyMoney = dailyMoney;
    }
    public void setDisabledCitySet(Set<Long> disabledCitySet) {
        this.disabledCitySet = disabledCitySet;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
    }
}
