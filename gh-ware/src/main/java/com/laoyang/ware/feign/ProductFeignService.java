package com.laoyang.ware.feign;

import com.laoyang.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ghower
 * @Date 2020-07-15 15:05
 * @Email 1520567597@qq.com
 */
@FeignClient("goop-product")
public interface ProductFeignService {


    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

}
