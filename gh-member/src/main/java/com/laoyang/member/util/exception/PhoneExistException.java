package com.laoyang.member.util.exception;

/**
 * @author ghower
 * @Date 2020-07-03 17:00
 * @Email 1520567597@qq.com
 * @Note
 */
public class PhoneExistException extends RuntimeException {

    public PhoneExistException(){
        super("手机号已存在！");
    }
}
