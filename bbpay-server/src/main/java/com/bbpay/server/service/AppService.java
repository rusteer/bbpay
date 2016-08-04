package com.bbpay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.AppEntity;
import com.bbpay.server.repository.AppRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class AppService extends AbstractService<AppEntity> {
    @Autowired
    AppRepository dao;
    @Override
    protected MyJpaRepository<AppEntity> getRepository() {
        return dao;
    }
}
