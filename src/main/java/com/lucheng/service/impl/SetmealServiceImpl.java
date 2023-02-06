package com.lucheng.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.CustomException;
import com.lucheng.common.R;
import com.lucheng.constants.SystemConstant;
import com.lucheng.domain.*;
import com.lucheng.dto.SetmealDto;
import com.lucheng.service.CategoryService;
import com.lucheng.service.DishService;
import com.lucheng.service.SetmealDishService;
import com.lucheng.service.SetmealService;
import com.lucheng.mapper.SetmealMapper;
import java.lang.String;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author lucheng
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2023-01-07 21:10:16
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
implements SetmealService{

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CategoryService categoryService;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private DishService dishService;
    @Override
    @Transactional
    public R<String> addSetmealWithDish(SetmealDto setmealDto) {
        //先在套餐表添加套餐信息
        //  根据categoryId获取到套餐对应的分类名
        Category category = categoryService.getById(setmealDto.getCategoryId());
        setmealDto.setCategoryName(category.getName());
        save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //再在套餐菜品关系表中添加信息
        setmealDishes = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId().toString());
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("添加套餐成功");
    }

    @Override
    public R<Page<SetmealDto>> querySetmealByPage(Integer page, Integer pageSize, String name) {
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.like(StringUtils.hasText(name),Setmeal::getName,name);
        setmealQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        page(setmealPage,setmealQueryWrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> setmealList = setmealPage.getRecords();
        //在List<Setmeal>每个元素赋值给对应的SetmealDto元素，并根据id查询对应的分类
        List<SetmealDto> setmealDtoList = setmealList.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @Override
    @Transactional
    public R<String> deleteSetmealWithDish(List<Long> ids) {
        //先判断待删除套餐是否处于在售状态，如处于在售状态则抛出异常
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,SystemConstant.STATUS_NORMAL);
        int count = count(queryWrapper);
        if(count > 0){
            throw new CustomException("有套餐正处于在售状态，无法删除");
        }
        //删除对应套餐
        removeByIds(ids);
        //删除套餐对应的菜品
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getSetmealId,ids));
        return R.success("删除套餐成功");
    }

    @Override
    public R<List<SetmealDto>> querySetmealInfo(SetmealDto setmealDto) {
        Long setmealDtoId = setmealDto.getId();//获取套餐id
        Long categoryId = setmealDto.getCategoryId();//获取菜品分类id
        List<SetmealDto> setmealDtoList = null;
        //查询redis中是否有对应信息
        setmealDtoList  = (List<SetmealDto>) redisTemplate.opsForValue().get("dish" + categoryId);
        if(!ObjectUtils.isEmpty(setmealDtoList)){
            return R.success(setmealDtoList);
        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //根据套餐id和菜品分类id进行查询条件设置
        queryWrapper.eq(!ObjectUtils.isEmpty(setmealDtoId),Setmeal::getId,setmealDtoId);
        queryWrapper.eq(!ObjectUtils.isEmpty(categoryId),Setmeal::getCategoryId,categoryId);
        //设置status为起售状态
        queryWrapper.eq(Setmeal::getStatus, SystemConstant.STATUS_NORMAL);
        //设置排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //获取到所有套餐基本信息
        List<Setmeal> setmealList = list(queryWrapper);

        //获取所有套餐的具体菜品信息
        setmealDtoList = setmealList.stream().map(setmeal -> {
            SetmealDto setmealDto1 = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto1);
            Long setmealId = setmeal.getId();
            LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishQueryWrapper.eq(SetmealDish::getSetmealId, setmealId);
            List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishQueryWrapper);
            setmealDto1.setSetmealDishes(setmealDishes);
            return setmealDto1;
        }).collect(Collectors.toList());

        //存入redis
        redisTemplate.opsForValue().set("dish"+categoryId,setmealDtoList,60L, TimeUnit.MINUTES);
        return R.success(setmealDtoList);
    }

    @Override
    public R<List<Dish>> querySetmealById(Long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        //根据setmealDish获取对应的菜品的详细信息
        List<Dish> dishList = setmealDishes.stream().map(setmealDish -> {
            Long dishId = Long.valueOf(setmealDish.getDishId());
            Dish dish = dishService.getById(dishId);
            return dish;
        }).collect(Collectors.toList());
        return R.success(dishList);
    }

}
