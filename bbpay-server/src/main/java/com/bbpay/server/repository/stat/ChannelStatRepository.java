package com.bbpay.server.repository.stat;
import com.bbpay.server.entity.stat.ChannelStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface ChannelStatRepository extends MyJpaRepository<ChannelStatEntity> {
    ChannelStatEntity findByStatDateAndAppIdAndChannelId(String today, Long appId, int channelId);
}
