package com.bbpay.admin.repository.stat;
import java.util.List;
import com.bbpay.admin.entity.stat.ChannelStatEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface ChannelStatRepository extends MyJpaRepository<ChannelStatEntity> {
    ChannelStatEntity findByStatDateAndAppIdAndChannelId(String statDate, Long appId, int channelId);
    List<ChannelStatEntity> findByStatDateAndAppId(String statDate, Long appId);
    List<ChannelStatEntity> findByStatDate(String statDate);
}
