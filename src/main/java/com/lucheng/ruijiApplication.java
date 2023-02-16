package com.lucheng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.lucheng.mapper")
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching
public class ruijiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ruijiApplication.class,args);
    }
}
