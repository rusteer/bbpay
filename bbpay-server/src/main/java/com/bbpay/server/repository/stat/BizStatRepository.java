package com.bbpay.server.repository.stat;
import java.util.List;
import com.bbpay.server.entity.stat.BizStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface BizStatRepository extends MyJpaRepository<BizStatEntity> {
    List<BizStatEntity> findByStatDate(String today);
    BizStatEntity findByStatDateAndBizId(String today, Long bizId);
}
