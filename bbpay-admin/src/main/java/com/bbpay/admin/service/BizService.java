package com.bbpay.admin.service;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.BizEntity;
import com.bbpay.admin.entity.BizProvinceLimit;
import com.bbpay.admin.repository.BizRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Service
@Transactional(readOnly = true)
public class BizService extends AbstractService<BizEntity> {
    private BizRepository dao;
    public Map<Long, BizProvinceLimit> getProvinceLimits(BizEntity entity) {
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
    @Override
    protected BizRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(BizRepository dao) {
        this.dao = dao;
    }
}
