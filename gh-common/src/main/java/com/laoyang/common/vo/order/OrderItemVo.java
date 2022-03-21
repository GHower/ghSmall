package com.laoyang.common.vo.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ghower
 * @Date 2020-07-14 19:28
 * @Email 1520567597@qq.com
 */
@Data
public class OrderItemVo {

    private Long skuId;

    private Boolean check;

    private String title;

    private String image;

    /**
     * 商品套餐属性
     */
    private List<String> skuAttrValues;

    private BigDecimal price;

    private Integer count;

    private BigDecimal totalPrice;

    /**
     * 减免价格
     */
    private BigDecimal reduce = new BigDecimal("0.00");

    /** 商品重量 **/
    private BigDecimal weight = new BigDecimal("0.085");
}
