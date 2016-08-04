package com.bbpay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bbpay.admin.entity.framework.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bbpay_app_instance")
public class AppInstanceEntity extends IdEntity {
    //unique fields
    private Long appId;
    private Long deviceId;
    private String packageName;
    private int versionCode;
    //
    private int channelId;
    private Date createTime;
    //ignore in db
    private DeviceEntity device;
    private DeviceInfoEntity deviceInfo;
    public AppInstanceEntity() {}
    public AppInstanceEntity(Long id) {
        this.id = id;
    }
    public Long getAppId() {
        return appId;
    }
    public int getChannelId() {
        return channelId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    @Transient
    @JsonIgnore
    public DeviceEntity getDevice() {
        return device;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    @Transient
    @JsonIgnore
    public DeviceInfoEntity getDeviceInfo() {
        return deviceInfo;
    }
    public String getPackageName() {
        return packageName;
    }
    public int getVersionCode() {
        return versionCode;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setDevice(DeviceEntity device) {
        this.device = device;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public void setDeviceInfo(DeviceInfoEntity deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}