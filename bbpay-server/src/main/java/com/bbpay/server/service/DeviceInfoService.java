package com.bbpay.server.service;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.DeviceInfoEntity;
import com.bbpay.server.repository.DeviceInfoRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class DeviceInfoService extends AbstractService<DeviceInfoEntity> {
    @Autowired
    private DeviceInfoRepository dao;
    public DeviceInfoEntity getLatestInfo(Long deviceId) {
        Page<DeviceInfoEntity> page = dao.findByDeviceId(deviceId, new PageRequest(0, 1, new Sort(new Order(Direction.DESC, "updateTime"))));
        if (page != null) {
            List<DeviceInfoEntity> list = page.getContent();
            if (list != null && list.size() > 0) return list.get(0);
        }
        return null;
    }
    @Transactional(readOnly = false)
    public DeviceInfoEntity loadOrSave(DeviceInfoEntity temp) {
        //uniq keys
        Long deviceId = temp.getDeviceId();
        String imsi = temp.getImsi();
        Long provinceId = temp.getProvinceId();
        Date time = new Date();
        String today = FormatUtil.format(time);
        DeviceInfoEntity dbData = dao.getByUnique(deviceId, imsi, provinceId);
        if (dbData != null) {
            dbData.setUpdateDate(today);
            dbData.setUpdateTime(time);
            dbData.setCarrierOperator(temp.getCarrierOperator());
            if (StringUtils.isNotEmpty(temp.getSmsc())) dbData.setSmsc(temp.getSmsc());
            if (temp.getCityId() != null) dbData.setCityId(temp.getCityId());
            dao.save(dbData);
        }
        if (dbData == null) {
            temp.setUpdateDate(today);
            temp.setCreateTime(time);
            temp.setUpdateTime(time);
            dbData = dao.save(temp);
        }
        return dbData;
    }
    @Override
    protected MyJpaRepository<DeviceInfoEntity> getRepository() {
        return dao;
    }
}
