package com.bbpay.server.controller.client;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.OrderForm;
import com.bbpay.common.bean.response.Order;
import com.bbpay.server.controller.WebUtils;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.service.OrderService;
import com.bbpay.server.util.Utils;

@Component
public class PayController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("pay");
    public class LogInfo {
        public OrderForm request;
        public Order order;
        public AppInstanceEntity appInstance;
        public String result;
        public String error;
        LogInfo(OrderForm request, Order order, AppInstanceEntity appInstance, String result, String error) {
            this.request = request;
            this.order = order;
            this.appInstance = appInstance;
            this.result = result;
            this.error = error;
        }
    }
    @Autowired
    OrderService orderService;
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) {
        JSONObject result = null;
        Order order = null;
        AppInstanceEntity appInstance = null;
        Throwable error = null;
        OrderForm form = null;
        try {
            form = Json.optObj(OrderForm.class, requestObj);
            try {
                appInstance = infoService.loadInstance(form);
            } catch (Throwable e) {
                error = e;
            }
            if (appInstance != null) {
                order = orderService.order(appInstance, form.price);
                if (order.bizInstanceList != null) {
                    result = order.toJson();
                }
            } else {
                result = noInstance();
            }
        } catch (Throwable e) {
            error = e;
            errorLogger.error(e.getMessage(), e);
        }
        try {
            LogInfo value = new LogInfo(form, order, appInstance, result == null ? null : result.toString(), Utils.getStatackTrace(error));
            logger.info(WebUtils.getRemoteAddr(request) + ":" + mapper.writeValueAsString(value));
        } catch (Exception e) {
            errorLogger.error(e.getMessage(), e);
        }
        return result;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}
