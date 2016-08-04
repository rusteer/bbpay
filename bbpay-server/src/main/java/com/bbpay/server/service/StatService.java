package com.bbpay.server.service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.entity.stat.AbstractStatEntity;
import com.bbpay.server.entity.stat.AppStatEntity;
import com.bbpay.server.entity.stat.BizChannelStatEntity;
import com.bbpay.server.entity.stat.BizDeviceStatEntity;
import com.bbpay.server.entity.stat.BizProvinceStatEntity;
import com.bbpay.server.entity.stat.BizStatEntity;
import com.bbpay.server.entity.stat.ChannelStatEntity;
import com.bbpay.server.entity.stat.DeviceStatEntity;
import com.bbpay.server.repository.stat.AppStatRepository;
import com.bbpay.server.repository.stat.BizChannelStatRepository;
import com.bbpay.server.repository.stat.BizDeviceStatRepository;
import com.bbpay.server.repository.stat.BizProvinceStatRepository;
import com.bbpay.server.repository.stat.BizStatRepository;
import com.bbpay.server.repository.stat.ChannelStatRepository;
import com.bbpay.server.repository.stat.DeviceStatRepository;

@Component
@Transactional(readOnly = true)
public class StatService {
    public static final int STAT_TYPE_ORDER = -1;
    public static final int STAT_TYPE_SUCCESS = 0;
    public static final int STAT_TYPE_CANCEL = 1;
    public static final int STAT_TYPE_FAILURE = 2;
    @Autowired
    AppStatRepository appStatDao;
    @Autowired
    BizDeviceStatRepository bizDeviceStatDao;
    @Autowired
    BizProvinceStatRepository bizProvinceStatDao;
    @Autowired
    BizChannelStatRepository bizChannelStatDao;
    @Autowired
    BizStatRepository bizStatDao;
    @Autowired
    ChannelStatRepository channelStatDao;
    @Autowired
    DeviceStatRepository deviceStatDao;
    public AppStatEntity getAppStat(Long appId) {
        return appStatDao.findByStatDateAndAppId(getToday(), appId);
    }
    //City findByNameAndCountryAllIgnoringCase(String name, String country);
    //Page<City> findByNameContainingAndCountryContainingAllIgnoringCase(String name, String country, Pageable pageable);
    public BizDeviceStatEntity getBizDeviceStat(Long bizId, Long deviceId) {
        return bizDeviceStatDao.findByStatDateAndBizIdAndDeviceId(getToday(), bizId, deviceId);
    }
    private BizChannelStatEntity getBizChannelStat(Long bizId, Long appId, int channelId) {
        return bizChannelStatDao.findByStatDateAndBizIdAndAppIdAndChannelId(getToday(), bizId, appId, channelId);
    }
    public Map<Long, AbstractStatEntity> getBizDeviceStatMap(Long deviceId) {
        Map<Long, AbstractStatEntity> map = new HashMap<Long, AbstractStatEntity>();
        for (BizDeviceStatEntity stat : bizDeviceStatDao.findByStatDateAndDeviceId(getToday(), deviceId)) {
            map.put(stat.getBizId(), stat);
        }
        return map;
    }
    public BizProvinceStatEntity getBizProvinceStat(Long bizId, Long provinceId) {
        return bizProvinceStatDao.findByStatDateAndBizIdAndProvinceId(getToday(), bizId, provinceId);
    }
    public Map<Long, AbstractStatEntity> getBizProvinceStatMap(Long provinceId) {
        Map<Long, AbstractStatEntity> map = new HashMap<Long, AbstractStatEntity>();
        for (BizProvinceStatEntity stat : bizProvinceStatDao.findByStatDateAndProvinceId(getToday(), provinceId)) {
            map.put(stat.getBizId(), stat);
        }
        return map;
    }
    public BizStatEntity getBizStat(Long bizId) {
        return bizStatDao.findByStatDateAndBizId(getToday(), bizId);
    }
    public Map<Long, AbstractStatEntity> getBizStatMap() {
        Map<Long, AbstractStatEntity> map = new HashMap<Long, AbstractStatEntity>();
        for (BizStatEntity stat : bizStatDao.findByStatDate(getToday())) {
            map.put(stat.getBizId(), stat);
        }
        return map;
    }
    public ChannelStatEntity getChanelStat(Long appId, int channelId) {
        return channelStatDao.findByStatDateAndAppIdAndChannelId(getToday(), appId, channelId);
    }
    public DeviceStatEntity getDeviceStat(Long deviceId) {
        return deviceStatDao.findByStatDateAndDeviceId(getToday(), deviceId);
    }
    private String getToday() {
        return FormatUtil.format(new Date());
    }
    public void save(AppStatEntity appStat) {
        appStatDao.save(appStat);
    }
    private <T extends AbstractStatEntity> T updateMoney(T stat, Date time, BizEntity biz, int statType) {
        switch (statType) {
            case STAT_TYPE_ORDER://order
                stat.setOrderMoney(stat.getOrderMoney() + biz.getPrice());
                break;
            case STAT_TYPE_SUCCESS://successReport
                stat.setSuccessMoney(stat.getSuccessMoney() + biz.getPrice());
                break;
            case STAT_TYPE_CANCEL://successReport
                stat.setCancelMoney(stat.getCancelMoney() + biz.getPrice());
                break;
            case STAT_TYPE_FAILURE://successReport
                stat.setFailureMoney(stat.getFailureMoney() + biz.getPrice());
                break;
        }
        stat.setUpdateTime(time);
        return stat;
    }
    @Transactional(readOnly = false)
    public void saveStat(BizEntity biz, AppInstanceEntity instance, int statType) {
        // long bizId, int price, String mobile, long provinceId;
        Date time = new Date();
        String today = getToday();
        {
            AppStatEntity stat = getAppStat(instance.getAppId());
            if (stat == null) {
                stat = new AppStatEntity();
                stat.setAppId(instance.getAppId());
                stat.setStatDate(today);
            }
            appStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
        {
            BizDeviceStatEntity stat = getBizDeviceStat(biz.getId(), instance.getDeviceId());
            if (stat == null) {
                stat = new BizDeviceStatEntity();
                stat.setBizId(biz.getId());
                stat.setDeviceId(instance.getDeviceId());
                stat.setStatDate(today);
            }
            bizDeviceStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
        {
            BizChannelStatEntity stat = getBizChannelStat(biz.getId(), instance.getAppId(), instance.getChannelId());
            if (stat == null) {
                stat = new BizChannelStatEntity();
                stat.setBizId(biz.getId());
                stat.setAppId(instance.getAppId());
                stat.setChannelId(instance.getChannelId());
                stat.setStatDate(today);
            }
            bizChannelStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
        {
            BizProvinceStatEntity stat = getBizProvinceStat(biz.getId(), instance.getDeviceInfo().getProvinceId());
            if (stat == null) {
                stat = new BizProvinceStatEntity();
                stat.setBizId(biz.getId());
                stat.setProvinceId(instance.getDeviceInfo().getProvinceId());
                stat.setStatDate(today);
            }
            bizProvinceStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
        {
            BizStatEntity stat = getBizStat(biz.getId());
            if (stat == null) {
                stat = new BizStatEntity();
                stat.setBizId(biz.getId());
                stat.setStatDate(today);
            }
            bizStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
        {
            ChannelStatEntity stat = getChanelStat(instance.getAppId(), instance.getChannelId());
            if (stat == null) {
                stat = new ChannelStatEntity();
                stat.setAppId(instance.getAppId());
                stat.setChannelId(instance.getChannelId());
                stat.setStatDate(today);
            }
            channelStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
        {
            DeviceStatEntity stat = getDeviceStat(instance.getDeviceId());
            if (stat == null) {
                stat = new DeviceStatEntity();
                stat.setDeviceId(instance.getDeviceId());
                stat.setStatDate(today);
            }
            deviceStatDao.save(this.updateMoney(stat, time, biz, statType));
        }
    }
}
