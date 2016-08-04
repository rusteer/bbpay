package com.bbpay.server.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_biz_device_stat")
public class BizDeviceStatEntity extends AbstractStatEntity {
    private Long bizId;
    private Long deviceId;
    public Long getBizId() {
        return bizId;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
