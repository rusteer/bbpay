package com.bbpay.server.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_biz_instance")
public class BizInstanceEntity extends IdEntity {
    private Long orderId;
    private Long bizId;
    private Long appInstanceId;
    private String orderDate;
    private Date orderTime;
    private Date reportTime;
    private int result;
    private String errorMessage;
    //dumpkeys
    private int price;
    private int sharing;
    private Long spId;
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
    public Long getAppInstanceId() {
        return appInstanceId;
    }
    public Long getBizId() {
        return bizId;
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
    public String getErrorMessage() {
        return errorMessage;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public Long getOrderId() {
        return orderId;
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
    public int getSharing() {
        return sharing;
    }
    public Long getSpId() {
        return spId;
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
    public void setBizId(Long bizId) {
        this.bizId = bizId;
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
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
    public void setSharing(int sharing) {
        this.sharing = sharing;
    }
    public void setSpId(Long spId) {
        this.spId = spId;
    }
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }
}
