package com.bbpay.admin.repository.stat;
import java.util.List;
import com.bbpay.admin.entity.stat.BizChannelStatEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface BizChannelStatRepository extends MyJpaRepository<BizChannelStatEntity> {
    List<BizChannelStatEntity> findByStatDateAndBizId(String statDate, Long bizId);
    List<BizChannelStatEntity> findByStatDate(String statDate);
    List<BizChannelStatEntity> findByStatDateAndAppId(String statDate,Long appId);
}
