package com.bbpay.server.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_biz_province_stat")
public class BizProvinceStatEntity extends AbstractStatEntity {
    private Long bizId;
    private Long provinceId;
    public Long getBizId() {
        return bizId;
    }
    public Long getProvinceId() {
        return provinceId;
    }
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }
}
