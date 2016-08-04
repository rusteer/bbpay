package com.bbpay.server.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.common.bean.response.Order;
import com.bbpay.server.entity.AppEntity;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.entity.BizProvinceLimit;
import com.bbpay.server.entity.SettingEntity;
import com.bbpay.server.entity.stat.AbstractStatEntity;
import com.bbpay.server.entity.stat.DeviceStatEntity;
import com.bbpay.server.repository.BizRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Service
@Transactional(readOnly = true)
public class BizService extends AbstractService<BizEntity> {
    @Autowired
    private BizRepository dao;
    @Autowired
    SettingService settingService;
    @Autowired
    ProvinceService provinceService;
    @Autowired
    StatService statService;
    @Autowired
    ScriptService scriptService;
    @Autowired
    AppService appService;
    @Transactional(readOnly = false)
    public Order getBizList(AppInstanceEntity appInstance, int price) {
        SettingEntity setting = this.settingService.get();
        if (!setting.isBizEnabled()) {
            Order order = new Order();
            order.globalNotMatchReason = "global pay function disabled";
            return order;
        }
        Map<Long, AbstractStatEntity> bizStatList = statService.getBizStatMap();
        Map<Long, AbstractStatEntity> getBizProvinceStatList = statService.getBizProvinceStatMap(appInstance.getDeviceInfo().getProvinceId());
        Map<Long, AbstractStatEntity> getBizDeviceStatList = statService.getBizDeviceStatMap(appInstance.getDeviceId());
        AppEntity app = appService.get(appInstance.getAppId());
        if (app == null) {
            Order order = new Order();
            order.globalNotMatchReason = "cannot found app by appId(" + appInstance.getAppId() + ")";
            return order;
        }
        int payLimit = setting.getClientPayDailyLimit();
        int intervalLimit = setting.getClientPayInterval();
        if (payLimit > 0 || intervalLimit > 0) {
            DeviceStatEntity stat = this.statService.getDeviceStat(appInstance.getDeviceId());
            if (stat != null) {
                if (payLimit > 0 && stat.getSuccessMoney() >= payLimit) {
                    Order order = new Order();
                    order.globalNotMatchReason = String.format("device's max dialy limit exceeded(%d>%d)", stat.getSuccessMoney(), payLimit);
                    return order;
                }
                long passSeconds = (System.currentTimeMillis() - stat.getUpdateTime().getTime()) / 1000;
                if (intervalLimit > 0 && passSeconds < intervalLimit) {
                    Order order = new Order();
                    order.globalNotMatchReason = String.format("device's pay interval limit (%d>%d)", intervalLimit, passSeconds);
                    return order;
                }
            }
        }
        Order order = null;
        if (app.getGroupId() != null && app.getGroupId() > 0) {
            List<BizEntity> source = dao.findByGroupIdAndEnabled(app.getGroupId(), true);
            order = getOrder(source, price, 1f, appInstance, bizStatList, getBizProvinceStatList, getBizDeviceStatList);
        }
        if (order == null || CollectionUtils.isEmpty(order.bizList)) {
            List<BizEntity> source = dao.findByEnabled(true);
            order = getOrder(source, price, 1f, appInstance, bizStatList, getBizProvinceStatList, getBizDeviceStatList);
            if (CollectionUtils.isEmpty(order.bizList)) {
                int degree = this.settingService.get().getPriceApproxDegree();
                order = getOrder(source, price, degree * 1.0 / 100, appInstance, bizStatList, getBizProvinceStatList, getBizDeviceStatList);
            }
        }
        return order;
    }
    private Order getOrder(List<BizEntity> source, int price, double priceApproxDegree, AppInstanceEntity appInstance, Map<Long, AbstractStatEntity> bizStatList,
            Map<Long, AbstractStatEntity> getBizProvinceStatList, Map<Long, AbstractStatEntity> getBizDeviceStatList) {
        Order order = new Order();
        List<BizEntity> list = new ArrayList<BizEntity>();
        Map<Long, String> bizNotMatchReason = new HashMap<Long, String>();
        for (BizEntity biz : source) {
            if (matchDateTime(biz, bizNotMatchReason)//
                    && isCarrierOperatorMatch(biz, appInstance, bizNotMatchReason) //
                    && isCityMatch(biz, appInstance, bizNotMatchReason)//
                    && isStatMatch(biz, bizStatList.get(biz.getId()), biz.getGlobalInterval(), biz.getGlobalDailyMoney(), bizNotMatchReason, "global")//
                    && isStatMatch(biz, getBizProvinceStatList.get(biz.getId()), biz.getProvinceInterval(), biz.getProvinceDailyMoney(), bizNotMatchReason, "province")//
                    && isStatMatch(biz, getBizDeviceStatList.get(biz.getId()), biz.getDeviceInterval(), biz.getDeviceDailyMoney(), bizNotMatchReason, "device")//
                    && isTargetPriceMatch(biz, price, bizNotMatchReason)//
            ) {
                list.add(biz);
            }
        }
        Collections.sort(list, new Comparator<BizEntity>() {
            @Override
            public int compare(BizEntity o1, BizEntity o2) {
                int result = Integer.valueOf(o2.getPrice()).compareTo(o1.getPrice());
                if (result == 0) {
                    result = Integer.valueOf(o2.getHotLevel()).compareTo(o1.getHotLevel());
                }
                return result;
            }
        });
        int orderBizCount = this.settingService.get().getOrderBizCount();
        order.bizList = getBizs(list, price, orderBizCount, priceApproxDegree);
        order.bizNotMatchReason = bizNotMatchReason;
        if (order.bizList == null || order.bizList.size() == 0) {
            order.globalNotMatchReason = "no biz match after filter";
        }
        return order;
    }
    private boolean isTargetPriceMatch(BizEntity biz, int price, Map<Long, String> bizNotMatchReason) {
        boolean match = biz.getPrice() <= price;
        if (!match) {
            bizNotMatchReason.put(biz.getId(), String.format("biz price(%d) is greater than target price(%d)", biz.getPrice(), price));
        }
        return match;
    }
    private boolean matchPrice(int bizPrice, int targetPrice, double fuzzyDegree) {
        return bizPrice <= targetPrice && bizPrice >= targetPrice * fuzzyDegree;
    }
    private List<BizEntity> getBizs(List<BizEntity> source, int targetPrice, int maxBizCount, double priceApproxDegree) {
        for (int i1 = 0; i1 < source.size(); i1++) {
            BizEntity v1 = source.get(i1);
            if (matchPrice(v1.getPrice(), targetPrice, priceApproxDegree)) { return Arrays.asList(new BizEntity[] { v1 }); }
            if (maxBizCount > 1) {
                for (int i2 = i1 + 1; i2 < source.size(); i2++) {
                    BizEntity v2 = source.get(i2);
                    if (matchPrice(v1.getPrice() + v2.getPrice(), targetPrice, priceApproxDegree)) {//
                        return Arrays.asList(new BizEntity[] { v1, v2 });
                    }
                    if (maxBizCount > 2) {
                        for (int i3 = i2 + 1; i3 < source.size(); i3++) {
                            BizEntity v3 = source.get(i3);
                            if (matchPrice(v1.getPrice() + v2.getPrice() + v3.getPrice(), targetPrice, priceApproxDegree)) {//
                                return Arrays.asList(new BizEntity[] { v1, v2, v3 });
                            }
                            if (maxBizCount > 3) {
                                for (int i4 = i3 + 1; i4 < source.size(); i4++) {
                                    BizEntity v4 = source.get(i4);
                                    if (matchPrice(v1.getPrice() + v2.getPrice() + v3.getPrice() + v4.getPrice(), targetPrice, priceApproxDegree)) {//
                                        return Arrays.asList(new BizEntity[] { v1, v2, v3, v4 });
                                    }
                                    if (maxBizCount > 4) {
                                        for (int i5 = i4 + 1; i5 < source.size(); i5++) {
                                            BizEntity v5 = source.get(i5);
                                            if (matchPrice(v1.getPrice() + v2.getPrice() + v3.getPrice() + v4.getPrice() + v5.getPrice(), targetPrice, priceApproxDegree)) {//
                                                return Arrays.asList(new BizEntity[] { v1, v2, v3, v4, v5 });
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    private Map<Long, BizProvinceLimit> getProvinceLimits(BizEntity entity) {
        String areaRule = entity.getAreaRule();
        Map<Long, BizProvinceLimit> map = new HashMap<Long, BizProvinceLimit>();
        if (StringUtils.startsWith(areaRule, "[")) {
            try {
                JSONArray array = new JSONArray(areaRule);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    if (obj != null) {
                        BizProvinceLimit limit = new BizProvinceLimit();
                        limit.setProvinceId(obj.optLong("id"));
                        limit.setDailyMoney(obj.optInt("dailyMoney"));
                        limit.setInterval(obj.optInt("interval"));
                        JSONArray cityIdArray = obj.optJSONArray("diabledCities");
                        if (cityIdArray != null) {
                            for (int j = 0; j < cityIdArray.length(); j++) {
                                limit.getDisabledCitySet().add(cityIdArray.optLong(j));
                            }
                        }
                        map.put(limit.getProvinceId(), limit);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    private boolean isStatMatch(BizEntity biz, AbstractStatEntity stat, int interval, int dailyLimit, Map<Long, String> bizNotMatchReason, String scope) {
        boolean match = true;
        if (stat != null) {
            long passTime = (System.currentTimeMillis() - stat.getUpdateTime().getTime()) / 1000;
            match = passTime >= interval;
            if (!match) {
                bizNotMatchReason.put(biz.getId(), String.format("bizStat(%s) passTime(%d) less than interval(%d)", scope, passTime, interval));
            } else {
                int successMoney = stat.getSuccessMoney();
                String today = FormatUtil.format(new Date());
                match = today.equals(stat.getStatDate()) && successMoney < dailyLimit;
                if (!match) {
                    bizNotMatchReason.put(biz.getId(), String.format("bizStat(%s,%s) successMoney(%d) reach dailyLimit(%d)", scope, today, successMoney, dailyLimit));
                }
            }
        }
        return match;
    }
    public boolean isCarrierOperatorMatch(BizEntity biz, AppInstanceEntity appInstance, Map<Long, String> bizNotMatchReason) {
        int bizCarrierOperator = biz.getCarrierOperator();
        int inCarrierOperator = appInstance.getDeviceInfo().getCarrierOperator();
        boolean match = bizCarrierOperator == inCarrierOperator;
        if (!match) {
            String reason = String.format("biz carrierOperator(%d) not match instance's carrierOperator(%d)", bizCarrierOperator, inCarrierOperator);
            bizNotMatchReason.put(biz.getId(), reason);
        }
        return match;
    }
    private boolean isCityMatch(BizEntity biz, AppInstanceEntity client, Map<Long, String> bizNotMatchReason) {
        Map<Long, BizProvinceLimit> limits = getProvinceLimits(biz);
        Long provinceId = client.getDeviceInfo().getProvinceId();
        BizProvinceLimit limit = limits.get(provinceId);
        boolean match = limit != null;
        if (!match) {
            bizNotMatchReason.put(biz.getId(), String.format("biz province list not match instance's province(%d)", provinceId));
        } else {
            Long cityId = client.getDeviceInfo().getCityId();
            match = !limit.getDisabledCitySet().contains(cityId);
            if (!match) {
                bizNotMatchReason.put(biz.getId(), String.format("biz disalbed city list contains instance's city(%d)", cityId));
            }
        }
        return match;
    }
    private boolean matchDateTime(BizEntity biz, Map<Long, String> bizNotMatchReason) {
        String today = FormatUtil.format(new Date());
        boolean match = StringUtils.isBlank(biz.getStartDate()) || biz.getStartDate().compareTo(today) <= 0;
        if (!match) {
            bizNotMatchReason.put(biz.getId(), String.format("biz start date not match(%s)", biz.getStartDate()));
        } else {
            match = StringUtils.isBlank(biz.getEndDate()) || biz.getEndDate().compareTo(today) >= 0;
            if (!match) {
                bizNotMatchReason.put(biz.getId(), String.format("biz end date not match(%s)", biz.getEndDate()));
            } else {
                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                match = currentHour >= biz.getStartHour() && currentHour <= biz.getEndHour();
                if (!match) {
                    bizNotMatchReason.put(biz.getId(), String.format("biz time limit not match(%d-%d)", biz.getStartHour(), biz.getEndHour()));
                }
            }
        }
        return match;
    }
    @Override
    protected MyJpaRepository<BizEntity> getRepository() {
        return dao;
    }
}
