package com.lucheng.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucheng.common.R;
import com.lucheng.domain.Orders;
import com.lucheng.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<Orders>> queryOrder(Integer page, Integer pageSize){
        return ordersService.queryOrderByPage(page,pageSize);
    }

}
