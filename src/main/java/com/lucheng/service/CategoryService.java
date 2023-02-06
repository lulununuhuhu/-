package com.lucheng.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.Category;

import java.util.List;

/**
* @author lucheng
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-01-07 20:48:00
*/
public interface CategoryService extends IService<Category> {

    public R<Page> queryCategory(Integer page, Integer pageSize);

    /**
     * 根据id进行分类删除
     * @param ids
     * @return
     */
    public R<String> deleteCategoryById(Long ids);

    /**
     * 根据条件查询菜品分类数据
     * @param category
     * @return
     */
    public R<List<Category>> listCategory(Category category);
}
