package com.bbpay.admin.repository;
import com.bbpay.admin.entity.UserEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface UserRepository extends MyJpaRepository<UserEntity> {
    UserEntity findByLoginName(String loginName);
}
