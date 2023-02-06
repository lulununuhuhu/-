package com.lucheng.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.Orders;

import java.util.List;

/**
* @author lucheng
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-01-07 20:48:00
*/
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

    /**
     * 分页查询订单信息
     * @param page
     * @param pageSize
     * @return
     */
    public R<Page<Orders>> queryOrderByPage(Integer page, Integer pageSize);
}
