package com.bbpay.admin.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_device_stat")
public class DeviceStatEntity extends AbstractStatEntity {
    private long deviceId;
    public long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }
}
