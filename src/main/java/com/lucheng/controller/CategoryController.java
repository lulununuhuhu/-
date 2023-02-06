package com.lucheng.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucheng.common.R;
import com.lucheng.domain.Category;
import com.lucheng.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        log.info("category: {}",category);
        boolean res = categoryService.save(category);
        if(res == true)
            return R.success("添加分类成功");
        else
            return R.error("添加分类失败");
    }

    @GetMapping("/page")
    public R<Page>  queryCategoryByPage(Integer page,Integer pageSize){
        log.info("分页查询.页号:{} 页大小:{}",page,pageSize);
        return categoryService.queryCategory(page,pageSize);
    }

    @DeleteMapping()
    public R<String> deleteCategory(Long ids){
        log.info("待删除分类id是:{}",ids);
        return categoryService.deleteCategoryById(ids);
    }

    @PutMapping()
    public R<Category> updateCategory(@RequestBody Category category){
        log.info("修改分类id:{}",category.getId());
        categoryService.updateById(category);
        return R.success(category);
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        return categoryService.listCategory(category);
    }
}
