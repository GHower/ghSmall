package com.laoyang.ware.config.excep;


/**
 * @author ghower
 * @Date 2020-07-15 22:18
 * @Email 1520567597@qq.com
 */
public class NoStockException extends RuntimeException {

    public Integer skuId;


    public NoStockException(Integer skuId) {
        super(skuId.toString()+"号商品库存不足！");
        this.skuId = skuId;
    }
}
