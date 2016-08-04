package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.SpEntity;
import com.bbpay.admin.repository.SpRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class SpService extends AbstractService<SpEntity> {
    private SpRepository dao;
    @Override
    protected SpRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(SpRepository dao) {
        this.dao = dao;
    }
}
