package com.bbpay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.GroupEntity;
import com.bbpay.admin.repository.GroupRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class GroupService extends AbstractService<GroupEntity> {
    private GroupRepository dao;
    @Override
    protected GroupRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(GroupRepository dao) {
        this.dao = dao;
    }
}
