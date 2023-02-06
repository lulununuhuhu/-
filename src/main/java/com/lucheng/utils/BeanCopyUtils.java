package com.lucheng.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 不用bean的相同属性拷贝
 */
public class BeanCopyUtils {
    //构造函数私有化
    private BeanCopyUtils(){}

    /**
     * Bean字段的复制
     * @param source bean源对象
     * @param clazz bean的复制对象的类字节码
     * @return
     */
    public static <T> T copyBean(Object source,Class<T> clazz){
        T instance = null;
        try {
            instance = clazz.newInstance();
            //实现bean拷贝的封装
            BeanUtils.copyProperties(source,instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 将Bean字段复制后以List的形式返回
     * @param beanList
     * @param clazz
     * @return
     * @param <T>
     */
    public static <V,T> List<T> copyBeanList(List<V> beanList, Class<T> clazz){
        return beanList.stream().map(o -> copyBean(o, clazz)).collect(Collectors.toList());
    }

}
