package com.bbpay.server.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_cp_channel")
public class CpChannelEntity extends IdEntity {
    private Long cpId;
    private int channelId;
    public CpChannelEntity() {}
    public CpChannelEntity(Long id) {
        this.id = id;
    }
    public int getChannelId() {
        return channelId;
    }
    public Long getCpId() {
        return cpId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }
}