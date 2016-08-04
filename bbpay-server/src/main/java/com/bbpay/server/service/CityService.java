package com.bbpay.server.service;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.CityEntity;
import com.bbpay.server.repository.CityRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class CityService extends AbstractCacheService<CityEntity> {
    @Autowired  private CityRepository dao;
    @Override
    protected List<CityEntity> fetchListFromDB(Long key) {
        return dao.getListByProvinceId(key);
    }
    public CityEntity findByCityCode(String areaCode) {
        List<CityEntity> list = dao.findByCityCode(areaCode);
        if (list != null && list.size() > 0) return list.get(0);
        return null;
    }
    private CityEntity getCaptial(Long provinceId) {
        return dao.getCaptial(provinceId);
    }
    public String getCaptialCode(Long provinceId) {
        CityEntity city = getCaptial(provinceId);
        String code = null;
        if (city != null) {
            code = city.getCityCode();
            if (code.startsWith("0")) code = StringUtils.right(code, code.length() - 1);
            if (code.length() == 2) code = code + "0";
        }
        return code;
    }
   
    public List<CityEntity> getListByProvinceId(Long provinceId) {
        return getCacheList(provinceId);
    }
    @Override
    protected MyJpaRepository<CityEntity> getRepository() {
        return dao;
    }
    
}
