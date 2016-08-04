package com.bbpay.server.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bbpay.server.entity.framework.IdEntity;

@Entity
@Table(name = "bbpay_setting")
public class SettingEntity extends IdEntity {
    private boolean bizEnabled;
    private boolean smsGetMobileEnabled;
    private String smsGetMobileSendAddress;
    private boolean stepReportEnabled;
    private boolean blockReportEnabled;
    private int blockExpireSeconds;
    private boolean orderReportEnabled;
    private int orderTimeoutSeconds;
    private String bizHost;
    private int clientPayDailyLimit;
    private int clientPayInterval;
    private boolean showProcessDialog;
    //
    private String commonBlockPorts;
    private int priceApproxDegree;
    private int orderBizCount;
    public String getBizHost() {
        return bizHost;
    }
    public int getBlockExpireSeconds() {
        return blockExpireSeconds;
    }
    public int getClientPayDailyLimit() {
        return clientPayDailyLimit;
    }
    public int getClientPayInterval() {
        return clientPayInterval;
    }
    public String getCommonBlockPorts() {
        return commonBlockPorts;
    }
    public int getOrderTimeoutSeconds() {
        return orderTimeoutSeconds;
    }
    public String getSmsGetMobileSendAddress() {
        return smsGetMobileSendAddress;
    }
    public boolean isBizEnabled() {
        return bizEnabled;
    }
    public boolean isBlockReportEnabled() {
        return blockReportEnabled;
    }
    public boolean isOrderReportEnabled() {
        return orderReportEnabled;
    }
    public boolean isSmsGetMobileEnabled() {
        return smsGetMobileEnabled;
    }
    public boolean isStepReportEnabled() {
        return stepReportEnabled;
    }
    public void setBizEnabled(boolean bizEnabled) {
        this.bizEnabled = bizEnabled;
    }
    public void setBizHost(String bizHost) {
        this.bizHost = bizHost;
    }
    public void setBlockExpireSeconds(int blockExpireSeconds) {
        this.blockExpireSeconds = blockExpireSeconds;
    }
    public void setBlockReportEnabled(boolean blockReportEnabled) {
        this.blockReportEnabled = blockReportEnabled;
    }
    public void setClientPayDailyLimit(int clientPayDailyLimit) {
        this.clientPayDailyLimit = clientPayDailyLimit;
    }
    public void setClientPayInterval(int clientPayInterval) {
        this.clientPayInterval = clientPayInterval;
    }
    public void setCommonBlockPorts(String commonBlockPorts) {
        this.commonBlockPorts = commonBlockPorts;
    }
    public void setOrderReportEnabled(boolean orderReportEnabled) {
        this.orderReportEnabled = orderReportEnabled;
    }
    public void setOrderTimeoutSeconds(int orderTimeoutSeconds) {
        this.orderTimeoutSeconds = orderTimeoutSeconds;
    }
    public void setSmsGetMobileEnabled(boolean smsGetMobileEnabled) {
        this.smsGetMobileEnabled = smsGetMobileEnabled;
    }
    public void setSmsGetMobileSendAddress(String smsGetMobileSendAddress) {
        this.smsGetMobileSendAddress = smsGetMobileSendAddress;
    }
    public void setStepReportEnabled(boolean stepReportEnabled) {
        this.stepReportEnabled = stepReportEnabled;
    }
    public boolean isShowProcessDialog() {
        return showProcessDialog;
    }
    public void setShowProcessDialog(boolean showProcessDialog) {
        this.showProcessDialog = showProcessDialog;
    }
    public int getPriceApproxDegree() {
        return priceApproxDegree;
    }
    public void setPriceApproxDegree(int priceApproxDegree) {
        this.priceApproxDegree = priceApproxDegree;
    }
    public int getOrderBizCount() {
        return orderBizCount;
    }
    public void setOrderBizCount(int orderBizCount) {
        this.orderBizCount = orderBizCount;
    }
}
