package com.lucheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.ShoppingCart;

/**
* @author lucheng
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2023-01-10 21:53:38
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加菜品或套餐到购物车
     * @param shoppingCart
     * @return
     */
    public R<String> addItem(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    public R<String> cleanShoppingCarts(Long userId);

    /**
     * 减少购物车中指定商品的数量
     * @param shoppingCart
     * @return
     */
    public R<String> subItemAccount(ShoppingCart shoppingCart);
}
