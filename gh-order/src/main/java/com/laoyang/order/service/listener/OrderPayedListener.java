package com.laoyang.order.service.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.laoyang.order.config.pay.AlipayTemplate;
import com.laoyang.order.config.pay.PayAsyncVo;
import com.laoyang.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ghower
 * @Date 2020-07-24 20:31
 * @Email 1520567597@qq.com
 */
@RestController
public class OrderPayedListener {

    @Resource
    private OrderService orderService;

    @Resource
    private AlipayTemplate alipayTemplate;

    @PostMapping(value = "/payed/notify")
    public String handleAlipayed(PayAsyncVo asyncVo, HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        // 只要收到支付宝的异步通知，返回 success 支付宝便不再通知
        // 获取支付宝POST过来反馈信息
        //TODO 需要验签
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type()); //调用SDK验证签名

        if (signVerified) {
            System.out.println("签名验证成功...");
            //去修改订单状态
            String result = orderService.handlePayResult(asyncVo);
            return result;
        } else {
            System.out.println("签名验证失败...");
            return "error";
        }
    }

}