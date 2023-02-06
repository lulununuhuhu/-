package com.lucheng.controller;

import com.lucheng.common.R;
import com.lucheng.domain.ShoppingCart;
import com.lucheng.service.ShoppingCartService;
import com.lucheng.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<String> addItem(@RequestBody ShoppingCart shoppingCart){
        if(ObjectUtils.isEmpty(shoppingCart))
            return R.error("添加购物车失败");
        return shoppingCartService.addItem(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        return R.success(shoppingCartService.list());
    }

    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart(){
        Long userId = (Long)ThreadLocalUtils.getCurrentUser();
        return shoppingCartService.cleanShoppingCarts(userId);
    }

    @PostMapping("/sub")
    public R<String> subItemAccount(@RequestBody ShoppingCart shoppingCart){
        if(ObjectUtils.isEmpty(shoppingCart)){
            log.info("减少购物车商品时出错");
        }
        return shoppingCartService.subItemAccount(shoppingCart);
    }
}
