package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.BlockEntity;
import com.bbpay.admin.repository.BlockRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Service
@Transactional(readOnly = true)
public class FeedbackService extends AbstractService<BlockEntity> {
    private BlockRepository dao;
    @Override
    protected BlockRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(BlockRepository dao) {
        this.dao = dao;
    }
}
