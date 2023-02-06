package com.lucheng.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.Dish;
import com.lucheng.dto.DishDto;

import java.util.List;

/**
* @author lucheng
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-01-07 21:10:16
*/
public interface DishService extends IService<Dish> {

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    public R<String> addDish(DishDto dishDto);

    /**
     * 分页查询菜品信息
     * @param page
     * @param pageSize
     * @return
     */
    public R<Page<DishDto>> queryDishByPage(Integer page, Integer pageSize);

    /**
     * 根据id查询菜品信息和口味信息
     * @param dishId
     * @return
     */
    public R<DishDto> queryDish(Long dishId);

    /**
     * 更新菜品信息和该菜品对应的口味信息
     * @param dishDto
     * @return
     */
    public R<String> updateDish(DishDto dishDto);

    /**
     * 根据条件查询菜品信息
     * @param dish
     * @return
     */
    public R<List<DishDto>> queryDishInfo(DishDto dish);
}
