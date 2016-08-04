package com.bbpay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_app")
public class AppEntity extends IdEntity {
    //unique fields
    private String name;
    private String description;
    private Long cpId;
    private Date createTime;
    private Long groupId;
    public AppEntity() {}
    public AppEntity(Long id) {
        this.id = id;
    }
    public Long getCpId() {
        return cpId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public String getDescription() {
        return description;
    }
    public Long getGroupId() {
        return groupId;
    }
    public String getName() {
        return name;
    }
    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public void setName(String name) {
        this.name = name;
    }
}