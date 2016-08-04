package com.bbpay.admin.repository;
import java.util.List;
import com.bbpay.admin.entity.CpChannelEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface CpChannelRepository extends MyJpaRepository<CpChannelEntity> {
    List<CpChannelEntity> findByCpId(long cpId);
}
