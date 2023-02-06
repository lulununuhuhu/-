package com.lucheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.CustomException;
import com.lucheng.common.R;
import com.lucheng.domain.Category;
import com.lucheng.domain.Dish;
import com.lucheng.domain.Setmeal;
import com.lucheng.service.CategoryService;
import com.lucheng.mapper.CategoryMapper;
import com.lucheng.service.DishService;
import com.lucheng.service.SetmealService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
* @author lucheng
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-01-07 20:48:00
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
implements CategoryService{

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;
    @Override
    public R<Page> queryCategory(Integer page, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        Page<Category> categoryPage = new Page<>(page,pageSize);
        Page<Category> categoryPageRes = page(categoryPage, queryWrapper);
        return R.success(categoryPageRes);
    }

    @Override
    public R<String> deleteCategoryById(Long ids) {
        //在删除分类id之前，判断是否该分类有和菜品或套餐关联
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 > 0){
            //抛出业务异常:该分类和菜品关联，无法删除
            throw new CustomException("该分类与菜品关联，无法删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            throw new CustomException("该分类与套餐关联，无法删除");
        }
        boolean res = removeById(ids);
        if(res == true)
            return R.success("删除分类成功");
        else
            return R.error("删除分类失败");
    }

    @Override
    public R<List<Category>> listCategory(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(category.getType()),Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = list(queryWrapper);
        return R.success(list);
    }
}
