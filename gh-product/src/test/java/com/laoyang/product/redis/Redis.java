package com.laoyang.product.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Redis {


    @Autowired(required = false)
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient client;


    /**
     *
     */
    @Test
    public void redis(){
        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        forValue.set("hello","world"+ UUID.randomUUID());
        String hello = forValue.get("hello");
        System.out.println(hello);
    }


    @Test
    public void client(){
        System.out.println(client);
    }




}
