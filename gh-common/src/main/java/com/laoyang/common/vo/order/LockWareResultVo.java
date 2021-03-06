package com.laoyang.common.vo.order;

import lombok.Data;

/**
 * @author ghower
 * @Date 2020-07-16 11:26
 * @Email 1520567597@qq.com
 */
@Data
public class LockWareResultVo {
    private Long skuId;

    private Integer num;

    /**
     * 当前商品是否锁定成功
     */
    private Boolean locked;
}
