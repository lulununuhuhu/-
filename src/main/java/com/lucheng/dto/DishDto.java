package com.lucheng.dto;

import com.lucheng.domain.Dish;
import com.lucheng.domain.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
    private String categoryName;
}
