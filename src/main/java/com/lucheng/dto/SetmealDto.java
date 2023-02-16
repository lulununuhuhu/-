package com.lucheng.dto;

import com.lucheng.domain.Setmeal;
import com.lucheng.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
