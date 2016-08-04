package com.bbpay.admin.entity;
import java.util.Set;
//ALTER TABLE `plugin`.`t_biz` ADD COLUMN `payment_type` INT NULL  AFTER `require_imsi_imei` , ADD COLUMN `keyword` VARCHAR(145) NULL  AFTER `payment_type` ;
//ALTER TABLE `plugin`.`t_biz` CHANGE COLUMN `payment_type` `payment_type` INT(11) NOT NULL DEFAULT 1  ;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.bbpay.admin.entity.framework.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bbpay_biz")
public class BizEntity extends IdEntity {
    private String name;
    private Long spId;
    private String port;
    private String command;
    private Long groupId;
    //用来做同步用
    private String syncPort;
    private String syncCommand;
    private int carrierOperator;
    private String clientOrder; //In Json Value;
    private String serviceScript;
    private String bizType;
    private boolean reportSuccess;
    private boolean reportFailure;
    private int price;
    private int sharing;
    private String areaRule;
    private boolean enabled;
    private int paymentType;
    private int syncMethod;//1:自动,2:手动
    private int minSdkVersion;
    private int hotLevel;
    private int paymentCycle;//1:周结,2:月结
    private int deviceDailyMoney;
    private int deviceMonthlyMoney;
    private int deviceInterval;
    private int provinceDailyMoney;
    private int provinceInterval;
    private int globalDailyMoney;
    private int globalInterval;
    private String startDate;
    private String endDate;
    private int startHour;
    private int endHour;
    private java.util.Date createTime;
    private java.util.Date updateTime;
    private String blockIds;
    public String getAreaRule() {
        return areaRule;
    }
    public String getBizType() {
        return bizType;
    }
    public int getCarrierOperator() {
        return carrierOperator;
    }
    public String getCommand() {
        return command;
    }
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public int getDeviceDailyMoney() {
        return deviceDailyMoney;
    }
    public int getDeviceInterval() {
        return deviceInterval;
    }
    public int getDeviceMonthlyMoney() {
        return deviceMonthlyMoney;
    }
    public String getEndDate() {
        return endDate;
    }
    public int getEndHour() {
        return endHour;
    }
    public int getGlobalDailyMoney() {
        return globalDailyMoney;
    }
    public int getGlobalInterval() {
        return globalInterval;
    }
    public Long getGroupId() {
        return groupId;
    }
    public int getHotLevel() {
        return hotLevel;
    }
    public int getMinSdkVersion() {
        return minSdkVersion;
    }
    public String getName() {
        return name;
    }
    public int getPaymentCycle() {
        return paymentCycle;
    }
    public int getPaymentType() {
        return paymentType;
    }
    public String getPort() {
        return port;
    }
    public int getPrice() {
        return price;
    }
    public int getProvinceDailyMoney() {
        return provinceDailyMoney;
    }
    public int getProvinceInterval() {
        return provinceInterval;
    }
    public int getSharing() {
        return sharing;
    }
    public Long getSpId() {
        return spId;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getBlockIds() {
        return blockIds;
    }
    public void setBlockIds(String blockIds) {
        this.blockIds = blockIds;
    }
    public int getStartHour() {
        return startHour;
    }
    public String getSyncCommand() {
        return syncCommand;
    }
    public int getSyncMethod() {
        return syncMethod;
    }
    public String getSyncPort() {
        return syncPort;
    }
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isReportFailure() {
        return reportFailure;
    }
    public boolean isReportSuccess() {
        return reportSuccess;
    }
    public void setAreaRule(String areaRule) {
        this.areaRule = areaRule;
    }
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
    public void setCarrierOperator(int carrierOperator) {
        this.carrierOperator = carrierOperator;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    public void setDeviceDailyMoney(int deviceDailyMoney) {
        this.deviceDailyMoney = deviceDailyMoney;
    }
    public void setDeviceInterval(int deviceInterval) {
        this.deviceInterval = deviceInterval;
    }
    public void setDeviceMonthlyMoney(int deviceMonthlyMoney) {
        this.deviceMonthlyMoney = deviceMonthlyMoney;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    public void setGlobalDailyMoney(int globalDailyMoney) {
        this.globalDailyMoney = globalDailyMoney;
    }
    public void setGlobalInterval(int globalInterval) {
        this.globalInterval = globalInterval;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public void setHotLevel(int hotLevel) {
        this.hotLevel = hotLevel;
    }
    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPaymentCycle(int paymentCycle) {
        this.paymentCycle = paymentCycle;
    }
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setProvinceDailyMoney(int provinceDailyMoney) {
        this.provinceDailyMoney = provinceDailyMoney;
    }
    public void setProvinceInterval(int provinceInterval) {
        this.provinceInterval = provinceInterval;
    }
    public void setReportFailure(boolean reportFailure) {
        this.reportFailure = reportFailure;
    }
    public void setReportSuccess(boolean reportSuccess) {
        this.reportSuccess = reportSuccess;
    }
    public void setSharing(int sharing) {
        this.sharing = sharing;
    }
    public void setSpId(Long spId) {
        this.spId = spId;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public void setSyncCommand(String syncCommand) {
        this.syncCommand = syncCommand;
    }
    public void setSyncMethod(int syncMethod) {
        this.syncMethod = syncMethod;
    }
    public void setSyncPort(String syncPort) {
        this.syncPort = syncPort;
    }
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getServiceScript() {
        return serviceScript;
    }
    public void setServiceScript(String serviceScript) {
        this.serviceScript = serviceScript;
    }
    public String getClientOrder() {
        return clientOrder;
    }
    public void setClientOrder(String clientOrder) {
        this.clientOrder = clientOrder;
    }
}
