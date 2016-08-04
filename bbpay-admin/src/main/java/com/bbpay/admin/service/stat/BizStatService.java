package com.bbpay.admin.service.stat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.stat.BizChannelStatEntity;
import com.bbpay.admin.entity.stat.BizStatEntity;
import com.bbpay.admin.repository.stat.BizStatRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class BizStatService extends AbstractService<BizStatEntity> {
    @Autowired
    private BizStatRepository dao;
    @Autowired
    private BizChannelStatService bizChannelStatService;
    @Override
    protected BizStatRepository getRepository() {
        return dao;
    }
    public List<BizStatEntity> findByStatDate(String statDate) {
        return dao.findByStatDate(statDate);
    }
    public BizStatEntity getByUnique(String statDate, Long bizId) {
        return dao.findByStatDateAndBizId(statDate, bizId);
    }
    @Transactional(readOnly = false)
    public BizStatEntity save(BizStatEntity stat) {
        BizStatEntity result = super.save(stat);
        int sum = 0;
        List<BizChannelStatEntity> bizChannelStatList = this.bizChannelStatService.findByStatDateAndBizId(stat.getStatDate(), stat.getBizId());
        for (BizChannelStatEntity entity : bizChannelStatList) {
            sum += entity.getSuccessMoney();
        }
        //按比例分配每一个渠道的账单数据
        if (sum > 0) {
            for (BizChannelStatEntity entity : bizChannelStatList) {
                entity.setStatementMoney(stat.getStatementMoney() * entity.getSuccessMoney() / sum);
                bizChannelStatService.save(entity);
            }
        }
        return result;
    }
}
