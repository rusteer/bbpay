package com.bbpay.admin.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.BizEntity;
import com.bbpay.admin.entity.ProvinceEntity;
import com.bbpay.admin.entity.report.sp.AbstractStat;
import com.bbpay.admin.entity.report.sp.BizStat;
import com.bbpay.admin.entity.report.sp.DateStat;
import com.bbpay.admin.entity.report.sp.PriceStat;
import com.bbpay.admin.entity.report.sp.ProvinceStat;
import com.bbpay.admin.entity.report.sp.SpReport;
import com.bbpay.admin.repository.mybatis.BizInstanceRepositoryMyBatis;

@Component
@Transactional(readOnly = true)
public class BizInstanceService {
    @Autowired
    ProvinceService provinceService;
    @Autowired
    BizService bizService;
    @Autowired
    BizInstanceRepositoryMyBatis repository;
    private void convert(AbstractStat stat, SpReport report) {
        switch (stat.getResult()) {
            case 0:
                report.setSuccessCount(stat.getPayCount());
                report.setSuccessSum(stat.getPaySum());
                break;
            case 1:
                report.setCancelCount(stat.getPayCount());
                report.setCancelSum(stat.getPaySum());
                break;
            case 2:
                report.setFailureCount(stat.getPayCount());
                report.setFailureSum(stat.getPaySum());
                break;
            default:
                report.setUnknownCount(stat.getPayCount());
                report.setUnknownSum(stat.getPaySum());
                break;
        }
    }
    private <T extends AbstractStat> List<SpReport> convert(List<T> stats) {
        Map<String, SpReport> map = new HashMap<String, SpReport>();
        boolean isDate = false;
        for (AbstractStat stat : stats) {
            if (stat instanceof DateStat) isDate = true;;
            String groupName = getGroupName(stat);
            SpReport report = map.get(groupName);
            if (report == null) {
                report = new SpReport();
                report.setGroup(groupName);
                map.put(groupName, report);
            }
            convert(stat, report);
        }
        final boolean orderByGroupName = isDate;
        List<SpReport> list = new ArrayList<SpReport>(map.values());
        Collections.sort(list, new Comparator<SpReport>() {
            @Override
            public int compare(SpReport o1, SpReport o2) {
                return orderByGroupName ? o2.getGroup().compareTo(o1.getGroup()) : Integer.valueOf(o2.getSuccessSum()).compareTo(o1.getSuccessSum());
            }
        });
        return list;
    }
    public List<SpReport> getBizReport(Map<String, Object> params) {
        Map<String, String> map = new HashMap<String, String>();
        for (BizEntity element : bizService.getAll()) {
            map.put(element.getId() + "", element.getName());
        }
        List<SpReport> result = convert(repository.getBizStat(params));
        for (SpReport element : result) {
            element.setGroup(map.get(element.getGroup()));
        }
        return result;
    }
    public List<SpReport> getDateReport(Map<String, Object> params) {
        return convert(repository.getDateStat(params));
    }
    private <T> String getGroupName(T t) {
        if (t instanceof DateStat) { return ((DateStat) t).getStatDate(); }
        if (t instanceof PriceStat) { return ((PriceStat) t).getPrice() + ""; }
        if (t instanceof ProvinceStat) { return ((ProvinceStat) t).getProvinceId() + ""; }
        if (t instanceof BizStat) { return ((BizStat) t).getBizId() + ""; }
        return null;
    }
    public List<SpReport> getPriceReport(Map<String, Object> params) {
        return convert(repository.getPriceStat(params));
    }
    public List<SpReport> getProvinceReport(Map<String, Object> params) {
        Map<String, String> map = new HashMap<String, String>();
        for (ProvinceEntity element : provinceService.getAll()) {
            map.put(element.getId() + "", element.getName());
        }
        List<SpReport> result = convert(repository.getProvinceStat(params));
        for (SpReport element : result) {
            element.setGroup(map.get(element.getGroup()));
        }
        return result;
    }
}
