package com.lucheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.R;
import com.lucheng.domain.ShoppingCart;
import com.lucheng.service.ShoppingCartService;
import com.lucheng.mapper.ShoppingCartMapper;
import com.lucheng.utils.ThreadLocalUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
* @author lucheng
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-01-10 21:53:38
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
implements ShoppingCartService{

    @Override
    public R<String> addItem(ShoppingCart shoppingCart) {
        Long userId = (Long) ThreadLocalUtils.getCurrentUser();
        shoppingCart.setUserId(userId);

        //判断待加入菜品信息在该用户的购物车表中是否已有
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.eq(!ObjectUtils.isEmpty(shoppingCart.getDishId()),ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(!ObjectUtils.isEmpty(shoppingCart.getSetmealId()),ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart one = getOne(queryWrapper);

        if(one != null){
            //菜品信息在购物表中已有,将数量加一
            Integer number = one.getNumber();
            one.setNumber(number+1);
            updateById(one);
        }
        else{
            //菜品信息在购物车中还没有,添加到购物车表中,数量为1
            shoppingCart.setNumber(1);
            save(shoppingCart);
        }
        return R.success("添加菜品到购物车成功");
    }

    @Override
    public R<String> cleanShoppingCarts(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        remove(queryWrapper);
        return R.success("清空购物车成功");
    }

    @Override
    public R<String> subItemAccount(ShoppingCart shoppingCart) {
        Long userId = (Long) ThreadLocalUtils.getCurrentUser();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.eq(!ObjectUtils.isEmpty(shoppingCart.getSetmealId()),ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(!ObjectUtils.isEmpty(shoppingCart.getDishId()),ShoppingCart::getDishId,shoppingCart.getDishId());
        ShoppingCart one = getOne(queryWrapper);
        one.setNumber(one.getNumber()-1);
        updateById(one);
        return R.success("减少数量成功");
    }
}
