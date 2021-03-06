package com.laoyang.common.vo.product;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ghower
 * @Date 2020-07-16 10:04
 * @Email 1520567597@qq.com
 */
@Data
public class SpuInfoVo {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 商品描述
     */
    private String spuDescription;
    /**
     * 所属分类id
     */
    private Long catalogId;
    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 品牌名
     */
    private String brandName;
    /**
     *
     */
    private BigDecimal weight;
    /**
     * 上架状态[0 - 新建，1 - 上架，2-下架]
     */
    private Integer publishStatus;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;
}
