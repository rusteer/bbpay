package com.bbpay.admin.service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.report.cp.DateStat;
import com.bbpay.admin.entity.report.cp.PriceStat;
import com.bbpay.admin.repository.mybatis.OrderRepositoryMyBatis;

@Component
@Transactional(readOnly = true)
public class OrderService {
    @Autowired
    OrderRepositoryMyBatis repository;
    public List<DateStat> getDateStat(Map<String, Object> params) {
        List<DateStat> list = repository.getDateStat(params);
        Collections.sort(list, new java.util.Comparator<DateStat>() {
            @Override
            public int compare(DateStat o1, DateStat o2) {
                int result = o2.getStatDate().compareTo(o1.getStatDate());
                return result;
            }
        });
        for (DateStat stat : list) {
            stat.setGroup(stat.getStatDate());
        }
        return list;
    }
    public List<PriceStat> getPriceStat(Map<String, Object> params) {
        List<PriceStat> list = repository.getPriceStat(params);
        Collections.sort(list, new java.util.Comparator<PriceStat>() {
            @Override
            public int compare(PriceStat o1, PriceStat o2) {
                int result = Integer.valueOf(o2.getSuccessMoney()).compareTo(o1.getSuccessMoney());
                return result;
            }
        });
        for (PriceStat stat : list) {
            stat.setGroup(stat.getPrice() + "");
        }
        return list;
    }
}
