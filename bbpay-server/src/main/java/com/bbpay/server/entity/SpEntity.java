package com.bbpay.server.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_sp")
public class SpEntity extends IdEntity {
    //unique fields
    private String name;
    private Date createTime;
    public SpEntity() {}
    public SpEntity(Long id) {
        this.id = id;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public String getName() {
        return name;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setName(String name) {
        this.name = name;
    }
}