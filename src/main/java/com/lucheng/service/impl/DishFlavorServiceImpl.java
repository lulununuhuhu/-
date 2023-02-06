package com.lucheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.domain.DishFlavor;
import com.lucheng.service.DishFlavorService;
import com.lucheng.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author lucheng
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-01-07 20:48:00
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
implements DishFlavorService{

}
