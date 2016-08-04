package com.bbpay.server.controller.client;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.OrderReportForm;
import com.bbpay.server.entity.OrderEntity;
import com.bbpay.server.service.OrderService;

@Component
public class OrderReportController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("orderReport");
    @Autowired
    OrderService orderService;
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        OrderReportForm form = Json.optObj(OrderReportForm.class, requestObj);
        if (form.orderId > 0) {
            OrderEntity entity = orderService.get(form.orderId);
            if (entity != null) {
                entity.setResult(form.result);
                entity.setMessage(form.message);
                entity.setReportTime(new Date());
                entity.setSuccessMoney(form.successMoney);
                orderService.save(entity);
            }
        }
        logger.info("request:" + mapper.writeValueAsString(form));
        return null;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}
