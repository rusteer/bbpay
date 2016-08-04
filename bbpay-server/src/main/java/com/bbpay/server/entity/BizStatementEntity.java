package com.bbpay.server.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

/**
 * 代码对账单
 * @author Administrator
 *
 */
@Entity
@Table(name = "bbpay_biz_statement")
public class BizStatementEntity extends IdEntity {
    private String statDate;
    private Long bizId;
    private int money;
    public Long getBizId() {
        return bizId;
    }
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
    public String getStatDate() {
        return statDate;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
}