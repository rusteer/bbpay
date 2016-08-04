package com.bbpay.server.service;
import java.util.HashMap;
import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.service.biz.comic.Comic;
import com.bbpay.server.util.HttpUtils;
import com.bbpay.server.util.base64.Base64;

@Service
public class ScriptService {
    protected Logger errorLogger = LoggerFactory.getLogger("error");
    protected final Logger LOGGER = LoggerFactory.getLogger("script");
  /*  public static void main(String args[]) {
        System.out.println(new ScriptService().getRdoInfo("1000000"));
    }*/
    public String getComicDynamicCmd(String staticCmd, String key) {
        return Comic.getDynamicCmd(staticCmd, key);
    }
   /* public JSONObject getRdoInfo(String feeCode) {
        long orderno = System.currentTimeMillis();
        String reqtime = WebUtil.formatTime(orderno, "yyyyMMddHHmmss");
        String sorderno = "rdotest" + orderno;
        LOGGER.info("getRdoInfo(\"" + feeCode + "\") start");
        RdoCommitBack rb = RDOManager.rdoprerdocommit(sorderno, feeCode, reqtime);
        LOGGER.info("getRdoInfo(\"" + feeCode + "\") end, rb=" + rb);
        JSONObject obj = new JSONObject();
        if (rb != null) {
            try {
                obj.put("confirmUrl", rb.getConfirmurl());
                obj.put("text", rb.getMo());
                obj.put("port", rb.getSpnumber());
            } catch (JSONException e) {
                errorLogger.error(e.getMessage(), e);
            }
        }
        return obj;
    }
    public String rdoConfirm(String confirmUrl) {
        return RDOManager.rdoconfirm(confirmUrl);
    }*/
    
    public  JSONObject string2Json(String s){
    	try{
    	return new  JSONObject(s);
    	}catch(Throwable e){
    		errorLogger.error(e.getMessage(), e);
    	}
    	return null;
    }
    public JSONObject xml2Json(String xml) {
        JSONObject result = new JSONObject();
        try {
            result = XML.toJSONObject(xml);
        } catch (JSONException e) {
            errorLogger.error(e.getMessage(), e);
        }
        LOGGER.info(result.toString());
        return result;
    }
    public String base64Encode(String s) {
        return Base64.encode(s);
    }
    public String base64Decode(String s) {
        return Base64.decode(s);
    }
    public String get(String url) {
        return HttpUtils.get(url);
    }
    public String post(String url, String param) {
        return HttpUtils.post(url, param);
    }
    public static String postXml(String url, String xml, String charsetName) {
        return HttpUtils.postXml(url, xml, charsetName);
    }
    public Map<String, String> executeScript(BizEntity entity, AppInstanceEntity bi, Map<String, String> inputParams) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByExtension("js");
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("service", this);
        bindings.put("biz", entity);
        bindings.put("appInstance", bi);
        bindings.put("input", inputParams);
        bindings.put("logger", LOGGER);
        Map<String, String> output = new HashMap<String, String>();
        bindings.put("output", output);
        String javascript = entity.getServiceScript();
        //LOGGER.info(javascript);
        try {
            engine.eval(javascript, bindings);
        } catch (Throwable e) {
            errorLogger.error(e.getMessage(), e);
        }
        return output;
    }
}
