package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.DeviceEntity;
import com.bbpay.admin.repository.DeviceRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class DeviceService extends AbstractService<DeviceEntity> {
    private DeviceRepository dao;
    @Override
    protected DeviceRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(DeviceRepository dao) {
        this.dao = dao;
    }
}
