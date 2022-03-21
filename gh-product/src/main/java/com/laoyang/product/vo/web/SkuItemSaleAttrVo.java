package com.laoyang.product.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ghower
 * @Date 2020-06-30 15:40
 * @Email 1520567597@qq.com
 * @Note
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemSaleAttrVo{
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}