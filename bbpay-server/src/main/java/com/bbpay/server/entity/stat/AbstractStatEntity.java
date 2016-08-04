package com.bbpay.server.entity.stat;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import com.bbpay.server.entity.framework.IdEntity;

@MappedSuperclass
public class AbstractStatEntity extends IdEntity {
    private String statDate;
    private int orderMoney;
    private int successMoney;
    private int failureMoney;
    private int cancelMoney;
    private Date updateTime;
    public String getStatDate() {
        return statDate;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public int getOrderMoney() {
        return orderMoney;
    }
    public void setOrderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }
    public int getSuccessMoney() {
        return successMoney;
    }
    public void setSuccessMoney(int successMoney) {
        this.successMoney = successMoney;
    }
    public int getFailureMoney() {
        return failureMoney;
    }
    public void setFailureMoney(int failureMoney) {
        this.failureMoney = failureMoney;
    }
    public int getCancelMoney() {
        return cancelMoney;
    }
    public void setCancelMoney(int cancelMoney) {
        this.cancelMoney = cancelMoney;
    }
    
}
