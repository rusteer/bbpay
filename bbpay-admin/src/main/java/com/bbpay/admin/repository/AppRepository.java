package com.bbpay.admin.repository;
import java.util.List;
import com.bbpay.admin.entity.AppEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface AppRepository extends MyJpaRepository<AppEntity> {
    List<AppEntity> findByCpId(Long cpId);
}
