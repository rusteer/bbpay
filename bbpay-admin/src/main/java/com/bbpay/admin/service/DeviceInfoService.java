package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.DeviceInfoEntity;
import com.bbpay.admin.repository.DeviceInfoRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class DeviceInfoService extends AbstractService<DeviceInfoEntity> {
    private DeviceInfoRepository dao;
    @Override
    protected DeviceInfoRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(DeviceInfoRepository dao) {
        this.dao = dao;
    }
}
