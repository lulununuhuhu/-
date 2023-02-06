package com.lucheng.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.Dish;
import com.lucheng.domain.Setmeal;
import com.lucheng.domain.SetmealDish;
import com.lucheng.dto.SetmealDto;
import java.lang.String;
import java.util.List;

/**
* @author lucheng
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-01-07 21:10:16
*/
public interface SetmealService extends IService<Setmeal> {

    /**
     * 添加套餐信息并更新套餐菜品关系表
     * @param setmealDto
     * @return
     */
    public R<String> addSetmealWithDish(SetmealDto setmealDto);

    /**
     * 分页查询套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    public R<Page<SetmealDto>> querySetmealByPage(Integer page, Integer pageSize, String name);

    /**
     * 删除套餐和对应的套餐菜品关系
     * @param ids
     * @return
     */
    public R<String> deleteSetmealWithDish(List<Long> ids);

    /**
     * 根据套餐分类查询套餐信息
     * @param setmealDto
     * @return
     */
    public R<List<SetmealDto>> querySetmealInfo(SetmealDto setmealDto);

    /**
     * 根据id查询套餐具体菜品信息
     *
     * @param id
     * @return
     */
    public R<List<Dish>> querySetmealById(Long id);
}
