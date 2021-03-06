package com.laoyang.order.feign;

import com.laoyang.common.vo.order.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ghower
 * @Date 2020-07-14 20:20
 * @Email 1520567597@qq.com
 */
@FeignClient("goop-cart")
public interface CartFeignService {


    /**
     * 获取已选中的最新的购物项集合
     * @return
     */
    @GetMapping("/checkedItems")
    List<OrderItemVo> getCurrentCartItems();
}
