package com.bbpay.server.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_app_stat")
public class AppStatEntity extends AbstractStatEntity {
    private Long appId;
    public Long getAppId() {
        return appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
