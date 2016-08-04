package com.bbpay.server.repository;
import java.util.List;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface BizRepository extends MyJpaRepository<BizEntity> {
    List<BizEntity> findByEnabled(boolean enabled);
    List<BizEntity> findByGroupIdAndEnabled(Long groupId, boolean enabled);
}
