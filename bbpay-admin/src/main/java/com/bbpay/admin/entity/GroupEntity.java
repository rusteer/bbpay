package com.bbpay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_group")
public class GroupEntity extends IdEntity {
    //unique fields
    private String name;
    private Date createTime;
    public GroupEntity() {}
    public GroupEntity(Long id) {
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