package com.bbpay.server.repository.stat;
import com.bbpay.server.entity.stat.BizChannelStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface BizChannelStatRepository extends MyJpaRepository<BizChannelStatEntity> {
    BizChannelStatEntity findByStatDateAndBizIdAndAppIdAndChannelId(String today, Long bizId, Long appId, int channelId);
}
