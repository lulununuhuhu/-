package com.lucheng.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lucheng.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //获取当前线程中存储的访问用户id
        Long userId = (Long) ThreadLocalUtils.getCurrentUser();
        log.info("当前线程id是: {}",Thread.currentThread().getId());
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createUser",userId , metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateUser", userId, metaObject);
        this.setFieldValByName("checkoutTime",new Date(),metaObject);
        this.setFieldValByName("orderTime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = (Long) ThreadLocalUtils.getCurrentUser();
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateUser", userId, metaObject);
    }
}