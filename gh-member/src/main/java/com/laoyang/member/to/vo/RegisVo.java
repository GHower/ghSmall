package com.laoyang.member.to.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author ghower
 * @Date 2020-07-03 16:41
 * @Email 1520567597@qq.com
 * @Note
 */
@Data
public class RegisVo {
    private String username;

    private String password;

    private String phone;
}
