package com.bbpay.admin.repository.mybatis;
import java.util.List;
import java.util.Map;
import com.bbpay.admin.entity.report.sp.BizStat;
import com.bbpay.admin.entity.report.sp.DateStat;
import com.bbpay.admin.entity.report.sp.PriceStat;
import com.bbpay.admin.entity.report.sp.ProvinceStat;
import com.bbpay.admin.repository.framework.MyBatisRepository;

@MyBatisRepository
public interface BizInstanceRepositoryMyBatis {
    List<BizStat> getBizStat(Map<String, Object> parameters);
    List<DateStat> getDateStat(Map<String, Object> parameters);
    List<PriceStat> getPriceStat(Map<String, Object> parameters);
    List<ProvinceStat> getProvinceStat(Map<String, Object> parameters);
}
