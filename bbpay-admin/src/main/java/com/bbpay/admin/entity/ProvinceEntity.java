package com.bbpay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_province")
public class ProvinceEntity extends IdEntity {
    private int priority;
    private String name;
    private String code;
    public ProvinceEntity() {}
    public ProvinceEntity(Long id) {
        super.setId(id);
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public int getPriority() {
        return priority;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
