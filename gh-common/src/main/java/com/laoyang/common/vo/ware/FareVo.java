package com.laoyang.common.vo.ware;

import com.laoyang.common.vo.member.MemberAddressVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ghower
 * @Date 2020-07-15 15:02
 * @Email 1520567597@qq.com
 */
@Data
public class FareVo {


    private MemberAddressVo address;

    private BigDecimal fare;
}

