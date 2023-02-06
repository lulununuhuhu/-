package com.lucheng.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucheng.common.R;
import com.lucheng.domain.Dish;
import com.lucheng.dto.DishDto;
import com.lucheng.service.DishService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;

    @PostMapping()
    public R<String> addDish(@RequestBody DishDto dishDto){
        return dishService.addDish(dishDto);
    }

    @GetMapping("/page")
    public R<Page<DishDto>> queryDishByPage(Integer page, Integer pageSize){
        return dishService.queryDishByPage(page,pageSize);
    }

    @GetMapping("/{id}")
    public R<DishDto> queryDish(@PathVariable("id") Long dishId){
        return dishService.queryDish(dishId);
    }

    @PutMapping()
    public R<String> updateDish(@RequestBody DishDto dishDto){
        if(ObjectUtils.isEmpty(dishDto)){
            return R.error("更新失败");
        }
        return dishService.updateDish(dishDto);
    }

    @GetMapping("/list")
    public R<List<DishDto>> queryDish(DishDto dish){
        if(ObjectUtils.isEmpty(dish)){
            return R.error(null);
        }
        return dishService.queryDishInfo(dish);
    }

}
