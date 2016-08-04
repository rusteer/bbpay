package com.bbpay.admin.repository.stat;
import java.util.List;
import com.bbpay.admin.entity.stat.BizStatEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface BizStatRepository extends MyJpaRepository<BizStatEntity> {
    List<BizStatEntity> findByStatDate(String statDate);

    BizStatEntity findByStatDateAndBizId(String statDate, Long bizId);
}
