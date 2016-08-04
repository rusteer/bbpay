package com.bbpay.server.repository;
import com.bbpay.server.entity.UserEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface UserRepository extends MyJpaRepository<UserEntity> {
    UserEntity findByLoginName(String loginName);
}
