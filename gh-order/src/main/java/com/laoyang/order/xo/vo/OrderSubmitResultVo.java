package com.laoyang.order.xo.vo;

import com.laoyang.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author ghower
 * @Date 2020-07-15 20:30
 * @Email 1520567597@qq.com
 */
@Data
public class OrderSubmitResultVo {

    private Integer code = 200;

    private OrderEntity order;
}
