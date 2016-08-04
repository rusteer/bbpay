package com.bbpay.admin.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_channel_stat")
public class ChannelStatEntity extends AbstractStatEntity {
    private Long appId;
    private int channelId;
    private int statementMoney;
    public Long getAppId() {
        return appId;
    }
    public int getChannelId() {
        return channelId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
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
