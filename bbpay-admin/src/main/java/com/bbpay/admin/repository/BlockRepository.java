package com.bbpay.admin.repository;
import com.bbpay.admin.entity.BlockEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface BlockRepository extends MyJpaRepository<BlockEntity> {
    BlockEntity findByBlockPortAndBlockContentAndReplyPortAndReplyContentAndReplyType(String blockPort, String blockContent, String replyPort, String replyContent, int replyType);
}
