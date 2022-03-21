package com.laoyang.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author ghower
 * @Date 2020-07-09 19:06
 * @Email 1520567597@qq.com
 */
@EnableFeignClients
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class CartMain {

    public static void main(String[] args) {
        SpringApplication.run(CartMain.class,args);
        System.out.println("\n\n\n--CART SUCCESS----\n\n\n");
    }
}
