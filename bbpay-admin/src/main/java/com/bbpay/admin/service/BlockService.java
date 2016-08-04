package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.BlockEntity;
import com.bbpay.admin.repository.BlockRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Service
@Transactional(readOnly = true)
public class BlockService extends AbstractService<BlockEntity> {
    @Autowired
    private BlockRepository dao;
    @Override
    protected BlockRepository getRepository() {
        return dao;
    }
    @Transactional(readOnly = false)
    public BlockEntity save(BlockEntity entity) {
        BlockEntity dbEntity = this.dao.findByBlockPortAndBlockContentAndReplyPortAndReplyContentAndReplyType(entity.getBlockPort(), entity.getBlockContent(),
                entity.getReplyPort(), entity.getReplyContent(), entity.getReplyType());
        if (dbEntity != null) return dbEntity;
        return dao.save(entity);
    }
}
