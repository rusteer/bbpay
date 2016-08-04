package com.bbpay.admin.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.ProvinceEntity;
import com.bbpay.admin.repository.ProvinceRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class ProvinceService extends AbstractService<ProvinceEntity> {
    private ProvinceRepository dao;
    @Override
    public List<ProvinceEntity> getAll() {
        return dao.getAll();
    }
    @Override
    protected ProvinceRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(ProvinceRepository dao) {
        this.dao = dao;
    }
}
