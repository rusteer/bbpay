package com.bbpay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_order")
public class OrderEntity extends IdEntity {
    private Long appInstanceId;
    private int price;
    private Date orderTime;
    private String orderDate;
    private int result;
    private int successMoney;
    private Date reportTime;
    private String message;
    //dump keys begin
    private Long deviceId;
    private Long provinceId;
    private Long cityId;
    private Integer channelId;
    private Long appId;
    private Long cpId;
    private Integer carrierOperator;
    private String packageName;
    private Integer versionCode;
    public Long getAppId() {
        return appId;
    }
    //dump keys end
    public Long getAppInstanceId() {
        return appInstanceId;
    }
    public Integer getCarrierOperator() {
        return carrierOperator;
    }
    public Integer getChannelId() {
        return channelId;
    }
    public Long getCityId() {
        return cityId;
    }
    public Long getCpId() {
        return cpId;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    public String getMessage() {
        return message;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public Date getOrderTime() {
        return orderTime;
    }
    public String getPackageName() {
        return packageName;
    }
    public int getPrice() {
        return price;
    }
    public Long getProvinceId() {
        return provinceId;
    }
    public Date getReportTime() {
        return reportTime;
    }
    public int getResult() {
        return result;
    }
    public int getSuccessMoney() {
        return successMoney;
    }
    public Integer getVersionCode() {
        return versionCode;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    public void setAppInstanceId(Long appInstanceId) {
        this.appInstanceId = appInstanceId;
    }
    public void setCarrierOperator(Integer carrierOperator) {
        this.carrierOperator = carrierOperator;
    }
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public void setSuccessMoney(int successMoney) {
        this.successMoney = successMoney;
    }
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }
}
