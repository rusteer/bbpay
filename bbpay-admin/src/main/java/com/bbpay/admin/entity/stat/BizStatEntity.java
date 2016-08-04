package com.bbpay.admin.entity.stat;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bbpay_biz_stat")
public class BizStatEntity extends AbstractStatEntity {
    private Long bizId;
    private int statementMoney;
    public Long getBizId() {
        return bizId;
    }
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
    public int getStatementMoney() {
        return statementMoney;
    }
    public void setStatementMoney(int statementMoney) {
        this.statementMoney = statementMoney;
    }
}
