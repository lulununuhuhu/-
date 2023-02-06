package com.lucheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.domain.OrderDetail;
import com.lucheng.service.OrderDetailService;
import com.lucheng.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author lucheng
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-01-07 20:48:00
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
implements OrderDetailService{

}
