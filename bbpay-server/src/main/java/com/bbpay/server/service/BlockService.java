package com.bbpay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.BlockEntity;
import com.bbpay.server.repository.BlockRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class BlockService extends AbstractService<BlockEntity> {
    @Autowired
    BlockRepository dao;
    @Override
    protected MyJpaRepository<BlockEntity> getRepository() {
        return dao;
    }
}
