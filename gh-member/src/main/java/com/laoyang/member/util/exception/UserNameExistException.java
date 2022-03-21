package com.laoyang.member.util.exception;

import java.util.concurrent.Executors;

/**
 * @author ghower
 * @Date 2020-07-03 16:59
 * @Email 1520567597@qq.com
 * @Note
 */
public class UserNameExistException extends RuntimeException {

    public UserNameExistException(){
        super("用户名已存在！");
    }
}
