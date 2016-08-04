package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.CpEntity;
import com.bbpay.admin.repository.CpRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class CpService extends AbstractService<CpEntity> {
    private CpRepository dao;
    @Override
    protected CpRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(CpRepository dao) {
        this.dao = dao;
    }
}
