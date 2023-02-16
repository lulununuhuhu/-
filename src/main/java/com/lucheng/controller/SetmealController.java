package com.lucheng.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucheng.common.R;
import com.lucheng.domain.Dish;
import com.lucheng.domain.SetmealDish;
import com.lucheng.dto.SetmealDto;
import com.lucheng.service.SetmealDishService;
import com.lucheng.service.SetmealService;
import java.lang.String;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 套餐管理相关接口
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    @Resource
    private SetmealDishService setmealDishService;

    @PostMapping()
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("套餐数据传输对象信息: {}",setmealDto);
        return setmealService.addSetmealWithDish(setmealDto);
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> querySetmealByPage(Integer page, Integer pageSize, String name){
        log.info("分页查询套餐信息. 页号:{} 页大小:{} 套餐名:{}",page,pageSize,name);
        return setmealService.querySetmealByPage(page,pageSize,name);
    }

    @DeleteMapping()
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        if(ObjectUtils.isEmpty(ids)){
            return R.error("删除套餐失败");
        }
        log.info("待删除套餐id是: {}",ids);
        return setmealService.deleteSetmealWithDish(ids);
    }

    @GetMapping("/list")
    public R<List<SetmealDto>> querySetmeal(SetmealDto setmealDto){
        if(ObjectUtils.isEmpty(setmealDto)){
            return R.error(null);
        }
        return setmealService.querySetmealInfo(setmealDto);
    }

    @GetMapping("/dish/{id}")
    public R<List<Dish>> querySetmealById(@PathVariable("id") Long id){
        return setmealService.querySetmealById(id);
    }

    /**
     * 更改套餐售卖状态
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable("status") Integer status,@RequestParam List<Long> ids){//接收集合类型参数时，一定要加@RequestParam注解
        log.info("待修改状态套餐id是: {},修改套餐状态为: {}",ids,status);
        return setmealService.changeStatus(status,ids);
    }
}
