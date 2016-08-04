package com.bbpay.admin.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.AppEntity;
import com.bbpay.admin.repository.AppRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class AppService extends AbstractService<AppEntity> {
    private AppRepository dao;
    public List<AppEntity> getAppList(Long cpId) {
        return dao.findByCpId(cpId);
    }
    @Override
    protected AppRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(AppRepository dao) {
        this.dao = dao;
    }
}
