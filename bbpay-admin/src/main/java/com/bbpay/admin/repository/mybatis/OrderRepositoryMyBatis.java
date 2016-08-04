package com.bbpay.admin.repository.mybatis;
import java.util.List;
import java.util.Map;
import com.bbpay.admin.entity.report.cp.DateStat;
import com.bbpay.admin.entity.report.cp.PriceStat;
import com.bbpay.admin.repository.framework.MyBatisRepository;

@MyBatisRepository
public interface OrderRepositoryMyBatis {
    List<DateStat> getDateStat(Map<String, Object> parameters);
    List<PriceStat> getPriceStat(Map<String, Object> parameters);
}
