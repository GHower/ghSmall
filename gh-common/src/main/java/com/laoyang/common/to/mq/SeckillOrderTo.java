package com.laoyang.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ghower
 * @Date 2020-07-24 18:18
 * @Email 1520567597@qq.com
 */
@Data
public class SeckillOrderTo {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 会员ID
     */
    private Long memberId;

}
