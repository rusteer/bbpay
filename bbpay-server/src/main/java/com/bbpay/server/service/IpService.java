package com.bbpay.server.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.server.entity.CityEntity;
import com.bbpay.server.entity.IpEntity;
import com.bbpay.server.entity.ProvinceEntity;
import com.bbpay.server.repository.IpRepository;

@Component
public class IpService {
    protected static Logger errorLogger = LoggerFactory.getLogger("error");
    //http://developer.baidu.com/map/index.php?title=webapi/ip-api
    public static JSONObject getLocation(String ip) {
        String url = String.format("%s?ak=%s&ip=%s", BAIDU_IP_API, BAIDU_AK, ip);
        String content = httpGet(url);
        if (StringUtils.isNotBlank(content)) {
            try {
                JSONObject obj = new JSONObject(content);
                return obj;
            } catch (JSONException e) {
                errorLogger.error(e.getMessage(), e);
            }
        }
        return null;
    }
    public static String httpGet(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCookieSpecs().register("easy", new CookieSpecFactory() {
            @Override
            public CookieSpec newInstance(HttpParams params) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {}
                };
            }
        });
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
        InputStream instream = null;
        String result = null;
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                result = sb.toString();
            }
        } catch (Throwable e) {
            errorLogger.error(e.getMessage(), e);
        }
        if (instream != null) {
            try {
                instream.close();
            } catch (IOException e) {
                errorLogger.error(e.getMessage(), e);
            }
        }
        client.getConnectionManager().shutdown();
        return result;
    }
    public static void main(String args[]) {}
    @Autowired
    IpRepository ipDao;
    @Autowired
    CityService cityService;
    @Autowired
    ProvinceService provinceService;
    protected static Logger logger = LoggerFactory.getLogger(IpService.class.getSimpleName());
    private static String BAIDU_AK = "2DkwIZtG7myoHf9A7PwzDTfX";
    private static String BAIDU_IP_API = "http://api.map.baidu.com/location/ip";
    public CityEntity getCity(String ip) {
        if (ip.startsWith("192.168")) {
            CityEntity city = new CityEntity();
            city.setId(147L);
            city.setProvince(new ProvinceEntity(17L));
            return city;
        }
        ip = ip.substring(0, ip.lastIndexOf('.') + 1) + "1";
        IpEntity ipEntity = ipDao.findByIp(ip);
        if (ipEntity != null) return cityService.get(ipEntity.getCityId());
        JSONObject obj = getLocation(ip);
        CityEntity city = null;
        if (obj != null) {
            String address = obj.optString("address");
            if (StringUtils.isNotBlank(address)) {
                String[] parts = address.split("\\|");
                if (parts.length > 2) {
                    String countryCode = parts[0];
                    if ("CN".equals(countryCode)) {
                        String provinceName = parts[1];
                        String cityName = parts[2];
                        Long provinceId = null;
                        for (ProvinceEntity element : provinceService.getAll()) {
                            String name = element.getName();
                            if (name.substring(0, 2).equals(provinceName.substring(0, 2))) {
                                provinceId = element.getId();
                                break;
                            }
                        }
                        if (provinceId != null) {
                            for (CityEntity element : cityService.getListByProvinceId(provinceId)) {
                                String name = element.getName();
                                if (name.startsWith(cityName) || cityName.startsWith(name) || name.contains(cityName)) {
                                    city = element;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (city == null) {
            errorLogger.error(String.format("get-city-by-baiduip-error,ip:%s,info:%s", ip, obj == null ? "null" : obj.toString()));
        }
        if (city != null) {
            ipEntity = new IpEntity();
            ipEntity.setCityId(city.getId());
            ipEntity.setIp(ip);
            ipDao.save(ipEntity);
        }
        return city;
    }
}
