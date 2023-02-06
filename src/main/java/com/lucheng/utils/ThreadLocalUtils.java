package com.lucheng.utils;


/**
 * 基于ThreadLocal封装的一个工具类
 */
public class ThreadLocalUtils {
    private static ThreadLocal<Object> threadLocal = new ThreadLocal<>();
    /**
     * 获取当前执行线程中存储的局部变量
     * @return
     */
    public static Object getCurrentUser(){
        return threadLocal.get();
    }

    /**
     * 设置当前线程中的局部变量
     * @param object
     */
    public static  void setCurrentUser(Object object){
        threadLocal.set(object);
    }
}
