package com.bbpay.server.repository;
import com.bbpay.server.entity.CpChannelEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface CpChannelRepository extends MyJpaRepository<CpChannelEntity> {
    CpChannelEntity findByCpIdAndChannelId(Long cpId, int channelId);
}
