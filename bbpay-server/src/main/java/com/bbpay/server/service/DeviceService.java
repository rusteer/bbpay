package com.bbpay.server.service;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bbpay.server.entity.DeviceEntity;
import com.bbpay.server.repository.DeviceRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class DeviceService extends AbstractService<DeviceEntity> {
	
	public  DeviceEntity  getByImei(String imei){
		List<DeviceEntity> list = dao.getByImei(imei);
		return CollectionUtils.isNotEmpty(list)?list.get(0):null;
	}
	
	
    @Autowired
    private DeviceRepository dao;
    @Transactional(readOnly = false)
    public DeviceEntity loadOrSave(DeviceEntity entity) {
        String imei = entity.getImei();
        String androidId = entity.getAndroidId();
        String serial = entity.getSerial();
        String macAddress = entity.getMacAddress();
        DeviceEntity dbData = null;
        if (StringUtils.isNotBlank(imei)) {
            dbData =  getByImei(imei);
        }
        if (dbData != null) {
            boolean needUpdate = false;
            if (StringUtils.isNotBlank(imei) && StringUtils.isBlank(dbData.getImei())) {
                dbData.setImei(imei);
                needUpdate = true;
            }
            if (StringUtils.isNotBlank(androidId) && StringUtils.isBlank(dbData.getAndroidId())) {
                dbData.setAndroidId(androidId);
                needUpdate = true;
            }
            if (StringUtils.isNotBlank(serial) && StringUtils.isBlank(dbData.getSerial())) {
                dbData.setSerial(serial);
                needUpdate = true;
            }
            if (StringUtils.isNotBlank(macAddress) && StringUtils.isBlank(dbData.getMacAddress())) {
                dbData.setMacAddress(macAddress);
                needUpdate = true;
            }
            if (needUpdate) {
                dbData = save(dbData);
            }
        }
        if (dbData == null) {
            dbData = save(entity);
        }
        return dbData;
    }
    @Override
    protected MyJpaRepository<DeviceEntity> getRepository() {
        return dao;
    }
}
