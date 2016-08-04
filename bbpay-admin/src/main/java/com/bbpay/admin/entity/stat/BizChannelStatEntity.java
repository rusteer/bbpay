package com.bbpay.admin.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_biz_channel_stat")
public class BizChannelStatEntity extends AbstractStatEntity {
    private Long bizId;
    private Long appId;
    private int channelId;
    private int statementMoney;
    public Long getBizId() {
        return bizId;
    }
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
    public Long getAppId() {
        return appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    public int getChannelId() {
        return channelId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
    public int getStatementMoney() {
        return statementMoney;
    }
    public void setStatementMoney(int statementMoney) {
        this.statementMoney = statementMoney;
    }
     
}
