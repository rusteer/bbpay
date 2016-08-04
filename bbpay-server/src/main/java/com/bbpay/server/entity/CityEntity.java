package com.bbpay.server.entity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_city")
public class CityEntity extends IdEntity {
    private ProvinceEntity province;
    private String cityCode;
    private String name;
    private String isCaptial;
    public CityEntity() {}
    public CityEntity(Long id) {
        super.setId(id);
    }
    public String getCityCode() {
        return cityCode;
    }
    public String getIsCaptial() {
        return isCaptial;
    }
    public String getName() {
        return name;
    }
    @ManyToOne
    @JoinColumn(name = "province_id")
    public ProvinceEntity getProvince() {
        return province;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    public void setIsCaptial(String isCaptial) {
        this.isCaptial = isCaptial;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setProvince(ProvinceEntity province) {
        this.province = province;
    }
}
