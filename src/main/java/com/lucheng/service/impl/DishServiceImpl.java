package com.lucheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.R;
import com.lucheng.constants.SystemConstant;
import com.lucheng.domain.Category;
import com.lucheng.domain.Dish;
import com.lucheng.domain.DishFlavor;
import com.lucheng.dto.DishDto;
import com.lucheng.service.CategoryService;
import com.lucheng.service.DishFlavorService;
import com.lucheng.service.DishService;
import com.lucheng.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author lucheng
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2023-01-07 21:10:16
*/
@Service

public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
implements DishService{
    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    @Override
    @Transactional //涉及到两张表，需要添加事务
    public R<String> addDish(DishDto dishDto) {
        //在Dish表中增加一条记录
        save(dishDto);

        //在DishFlavor表中增加该菜品对应的口味记录
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(flavor->{
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return R.success("添加菜品成功");
    }

    @Override
    public R<Page<DishDto>> queryDishByPage(Integer page, Integer pageSize) {
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        page(dishPage,dishLambdaQueryWrapper);
        List<Dish> dishList = dishPage.getRecords();
        BeanUtils.copyProperties(dishPage,dishDtoPage);
        //获取dishList中每个菜品对应分类的分类姓名
        List<DishDto> dishDtoList = dishList.stream().map(dish -> {
            Long categoryId = dish.getCategoryId();
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto, "flavors");
            LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
            categoryQueryWrapper.eq(Category::getId, categoryId);
            Category category = categoryService.getOne(categoryQueryWrapper);
            if (!ObjectUtils.isEmpty(category)) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    @Override
    public R<DishDto> queryDish(Long dishId) {
        DishDto dishDto = new DishDto();
        //查询菜品信息
        Dish dish = getById(dishId);
        BeanUtils.copyProperties(dish,dishDto);
        //查询口味信息
        LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavorList = dishFlavorService.list(flavorLambdaQueryWrapper);
        dishDto.setFlavors(flavorList);
        return R.success(dishDto);
    }

    @Override
    @Transactional
    public R<String> updateDish(DishDto dishDto) {
        //更新菜品信息
        updateById(dishDto);

        //更新对应的口味信息
        List<DishFlavor> newFlavorList = dishDto.getFlavors();
        newFlavorList = newFlavorList.stream().map(newFlavor->{
            newFlavor.setDishId(dishDto.getId());
            return newFlavor;
        }).collect(Collectors.toList());
        //  1)先删除该菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //  2)然后重新添加更新后的口味信息
        dishFlavorService.saveBatch(newFlavorList);
        return R.success("更新菜品成功");
    }

    @Override
    public R<List<DishDto>> queryDishInfo(DishDto dish) {
        Long dishId = dish.getId();//获取菜品id
        Long categoryId = dish.getCategoryId();//获取菜品分类id
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //根据菜品id和菜品分类id进行查询条件设置
        queryWrapper.eq(!ObjectUtils.isEmpty(dishId),Dish::getId,dishId);
        queryWrapper.eq(!ObjectUtils.isEmpty(categoryId),Dish::getCategoryId,categoryId);
        //设置status为起售状态
        queryWrapper.eq(Dish::getStatus, SystemConstant.STATUS_NORMAL);
        //设置排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = list(queryWrapper);

        List<DishDto> dishDtoList = dishList.stream().map(dish1 -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dish1.getId()));
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
