package com.lucheng.redis;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class testRedis {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 测试能否ping通远程云服务器的redis客户端
     */
    @Test
    public void pingRedis(){
        redisTemplate.opsForValue().set("name","lucheng");
    }
}
