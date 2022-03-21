package com.laoyang.common.to.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

/**
 * @author ghower
 * @Date 2020-07-18 21:04
 * @Email 1520567597@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLockedTo {

    /** 库存工作单的id **/
    private Long id;

    /** 工作单详情的所有信息 **/
    private StockDetailTo detailTo;
}
