package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.CityEntity;
import com.bbpay.admin.repository.CityRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class CityService extends AbstractService<CityEntity> {
    @Autowired
    private CityRepository dao;
    @Override
    protected CityRepository getRepository() {
        return dao;
    }
}
