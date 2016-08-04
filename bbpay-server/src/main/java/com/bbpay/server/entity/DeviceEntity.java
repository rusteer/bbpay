package com.bbpay.server.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_device")
public class DeviceEntity extends IdEntity {
    //unique fields
    private String imei;
    private String serial;
    private String androidId;
    private String macAddress;
    //
    private String manufacturer;
    private String model;
    private int sdkVersion;
    private String brand;
    private Date createTime;
    private int displayWidth;
    private int displayHeight;
    public DeviceEntity() {}
    public DeviceEntity(Long id) {
        this.id = id;
    }
    public String getAndroidId() {
        return androidId;
    }
    public String getBrand() {
        return brand;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public String getImei() {
        return imei;
    }
    public String getMacAddress() {
        return macAddress;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public String getModel() {
        return model;
    }
    public int getSdkVersion() {
        return sdkVersion;
    }
    public String getSerial() {
        return serial;
    }
    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public int getDisplayWidth() {
        return displayWidth;
    }
    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }
    public int getDisplayHeight() {
        return displayHeight;
    }
    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }
}