package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    /**
     * 添加菜品
     * @param shoppingCart
     */
    void insertDishOrSetmeal(ShoppingCart shoppingCart);

    /**
     * 根据菜品id和口味查询
     * @return
     */
    ShoppingCart findRepetition(ShoppingCart shoppingCart);
}
