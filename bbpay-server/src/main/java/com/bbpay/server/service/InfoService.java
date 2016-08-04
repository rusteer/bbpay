package com.bbpay.server.service;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.request.AbstractForm;
import com.bbpay.common.bean.request.InitForm;
import com.bbpay.common.bean.request.Methods;
import com.bbpay.common.bean.response.AbstractResponse;
import com.bbpay.server.entity.AppEntity;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.CityEntity;
import com.bbpay.server.entity.CpChannelEntity;
import com.bbpay.server.entity.DeviceEntity;
import com.bbpay.server.entity.DeviceInfoEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InfoService {
    protected ObjectMapper mapper = new ObjectMapper();
    // {name: "未知", value: 0},
    //{name: "移动", value: 1},
    //{name: "联通", value: 2},
    //{name: "电信", value: 3}
    public static int getCardType(String imsi) {
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) return 1;
            if (imsi.startsWith("46001")) return 2;
            if (imsi.startsWith("46003")) return 3;
        }
        throw new PayException("cannot get carrierOperator by imsi " + imsi);
    }
    private static String getCityCodeBySmsc(String smsc) {
        String result = null;
        //对于中国移动的短信服务中心号是+861380xxxx500,其中xxxx是你所在的长途电话区号，不足4位就补0，比如我所在的武汉是027，补0后是0270，就应该+8613800270500,上海是021，补0后是0210，就应该填+8613800210500。
        String temp = StringUtils.substring(smsc, 4, 8);
        if (StringUtils.length(temp) == 4) {
            if (temp.endsWith("0") && (temp.startsWith("01") || temp.startsWith("02"))) {
                temp = temp.substring(0, 3);
            }
            result = temp;
        }
        return result;
    }
    public static void main(String args[]) {
        System.out.println(getCityCodeBySmsc("13800270500"));
    }
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    IpService ipRangeService;
    @Autowired
    CityService cityService;
    @Autowired
    ProvinceService provinceService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    AppService appService;
    @Autowired
    AppInstanceService appInstanceService;
    @Autowired
    DeviceInfoService deviceInfoService;
    @Autowired
    CpChannelService cpChannelService;
    private CityEntity getBySmsc(String smsc) {
        if (smsc.startsWith("+86")) {
            smsc = StringUtils.substring(smsc, 3, smsc.length());
        } else if (smsc.startsWith("0086")) {
            smsc = StringUtils.substring(smsc, 4, smsc.length());
        }
        if (StringUtils.startsWith(smsc, "1380")) {//中国移动
            String cityCode = getCityCodeBySmsc(smsc);
            if (StringUtils.isNotBlank(cityCode)) { return cityService.findByCityCode(cityCode); }
        }
        return null;
    }
    public void handleResponse(AbstractForm form, AppInstanceEntity instance, AbstractResponse response) {
        if (instance == null) {
            response.clearData = true;
        } else {
            if (form.appInstanceId <= 0) {
                response.appInstanceId = instance.getId();
            }
            if (form.deviceId <= 0) {
                response.deviceId = instance.getDeviceId();
            }
        }
    }
    public AppInstanceEntity loadInstance(AbstractForm form) throws Exception {
        AppInstanceEntity instance = null;
        long instanceId = form.appInstanceId;
        if (instanceId > 0) {
            instance = appInstanceService.get(instanceId);
            if (instance == null) { throw new PayException("can't get appInsant from id" + instanceId); }
            instance.setDevice(deviceService.get(instance.getDeviceId()));
            if (!isUniqKeysMatch(instance, form)) throw new PayException("instance unique keys changed in client side");
        } else {
            long deviceId = form.deviceId;
            DeviceEntity device = null;
            if (deviceId > 0) {
                device = deviceService.get(deviceId);
                if (device == null) throw new PayException("can't get Device from id:" + deviceId);
            } else if (form.device != null) {
                DeviceEntity temp = new DeviceEntity();
                temp.setAndroidId(form.device.androidId);
                temp.setBrand(form.device.brand);
                temp.setCreateTime(new Date());
                temp.setImei(form.device.imei);
                temp.setMacAddress(form.device.macAddress);
                temp.setManufacturer(form.device.manufacturer);
                temp.setModel(form.device.model);
                temp.setSdkVersion(form.device.sdkVersion);
                temp.setSerial(form.device.serial);
                temp.setDisplayWidth(form.device.displayWidth);
                temp.setDisplayHeight(form.device.displayHeight);
                device = deviceService.loadOrSave(temp);
            }
            if (device == null) { throw new PayException("no device info can be found or provided"); }
            if (form.appId == 0) throw new PayException("no appId provided");
            if (form.versionCode == 0) throw new PayException("no appId provided");
            if (StringUtils.isBlank(form.packageName)) throw new PayException("no packageName provided");
            {
                AppInstanceEntity temp = new AppInstanceEntity();
                temp.setAppId(form.appId);
                temp.setChannelId(form.channelId);
                temp.setCreateTime(new Date());
                temp.setDeviceId(device.getId());
                temp.setPackageName(StringUtils.substring(form.packageName, 0, 100));
                temp.setVersionCode(form.versionCode);
                instance = appInstanceService.loadOrSave(temp);
                AppEntity app = appService.get(form.appId);
                if (app != null) {
                    CpChannelEntity cpChannel = cpChannelService.findByUnique(app.getCpId(), form.channelId);
                    if (cpChannel == null) {
                        cpChannel = new CpChannelEntity();
                        cpChannel.setChannelId(form.channelId);
                        cpChannel.setCpId(app.getCpId());
                        cpChannelService.save(cpChannel);
                    }
                }
                //
            }
            instance.setDevice(device);
        }
        if (form.method == Methods.INIT) {
            InitForm initForm = (InitForm) form;
            DeviceInfoEntity temp = new DeviceInfoEntity();
            temp.setDeviceId(instance.getDeviceId());
            temp.setCellLocation(initForm.cellLocation);
            temp.setImsi(initForm.imsi);
            temp.setSmsc(initForm.smsc);
            temp.setIp(initForm.ip);
            temp.setCreateTime(new Date());
            setLocationInfo(temp);
            DeviceInfoEntity info = deviceInfoService.loadOrSave(temp);
            instance.setDeviceInfo(info);
        } else {
            instance.setDeviceInfo(deviceInfoService.getLatestInfo(instance.getDeviceId()));
        }
        if (instance.getDeviceInfo() == null) throw new PayException("no deviceInfo found or provided");
        return instance;
    }
    private boolean isUniqKeysMatch(AppInstanceEntity instance, AbstractForm form) {
        return instance.getAppId() == form.appId //
                && instance.getDeviceId() == form.deviceId//
                && instance.getPackageName().equals(form.packageName) //
                && instance.getVersionCode() == form.versionCode//
                && instance.getChannelId() == form.channelId;
    }
    private void setLocationInfo(DeviceInfoEntity temp) throws Exception {
        if (StringUtils.isBlank(temp.getImsi())) throw new PayException("imsi is empty");
        CityEntity city = getBySmsc(temp.getSmsc());
        if (city == null) {
            city = ipRangeService.getCity(temp.getIp());
        }
        if (city == null) throw new PayException("cannot get City by given info:" + mapper.writeValueAsString(temp));
        temp.setCityId(city.getId());
        temp.setProvinceId(city.getProvince().getId());
        temp.setCarrierOperator(getCardType(temp.getImsi()));
    }
}
