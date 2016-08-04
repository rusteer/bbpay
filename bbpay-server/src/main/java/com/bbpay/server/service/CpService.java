package com.bbpay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.CpEntity;
import com.bbpay.server.repository.CpRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class CpService extends AbstractService<CpEntity> {
  @Autowired CpRepository dao;
    @Override
    protected MyJpaRepository<CpEntity> getRepository() {
        return dao;
    }}
