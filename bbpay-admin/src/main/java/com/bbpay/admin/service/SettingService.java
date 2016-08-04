package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.SettingEntity;
import com.bbpay.admin.repository.SettingRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class SettingService extends AbstractService<SettingEntity> {
    private SettingRepository dao;
    public SettingEntity get() {
        return dao.findOne(1L);
    }
    @Override
    protected SettingRepository getRepository() {
        return dao;
    }
    public boolean isBizEnabled() {
        SettingEntity entity = get();
        return entity != null && entity.isBizEnabled();
    }
    @Autowired
    public void setDao(SettingRepository dao) {
        this.dao = dao;
    }
}
