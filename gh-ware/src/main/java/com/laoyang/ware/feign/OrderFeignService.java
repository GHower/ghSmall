package com.laoyang.ware.feign;

import com.laoyang.common.util.R;
import com.laoyang.common.vo.order.OrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ghower
 * @Date 2020-07-18 22:09
 * @Email 1520567597@qq.com
 */
@FeignClient("goop-order")
public interface OrderFeignService {

    @GetMapping("/order/order/info/{orderSn}")
    R<OrderVo> getOneByOrderSn(@PathVariable("orderSn") String orderSn);
}
