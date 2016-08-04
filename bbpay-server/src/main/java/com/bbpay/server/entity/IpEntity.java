package com.bbpay.server.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_ip")
public class IpEntity extends IdEntity {
    private String ip;
    private Long cityId;
    public Long getCityId() {
        return cityId;
    }
    public String getIp() {
        return ip;
    }
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
}
