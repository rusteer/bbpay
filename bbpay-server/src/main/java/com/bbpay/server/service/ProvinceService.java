package com.bbpay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.ProvinceEntity;
import com.bbpay.server.repository.ProvinceRepository;

@Component
@Transactional(readOnly = true)
public class ProvinceService extends AbstractService<ProvinceEntity> {
    @Autowired
    private ProvinceRepository dao;
    @Override
    protected ProvinceRepository getRepository() {
        return dao;
    }
}
