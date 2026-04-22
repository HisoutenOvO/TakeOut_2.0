package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final SetmealMapper setmealMapper;
    private final DishMapper dishMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //对口味进行序列化
        if(shoppingCart.getDishFlavor() != null){
            shoppingCart.setDishFlavor(normalizeFlavorString(shoppingCartDTO.getDishFlavor()));
        }
        //查重
        ShoppingCart sc = shoppingCartMapper.findRepetition(shoppingCart);
        if(sc != null){
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateById(shoppingCart);
        }else {
            //无重复则新增数据
            //补全剩余信息
            if(shoppingCartDTO.getSetmealId() != null){
                Setmeal setmeal = setmealMapper.selectById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }else{
                Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }
            shoppingCartMapper.insertDishOrSetmeal(shoppingCart);
        }
    }

    /**
     * 对传入的口味数据序列化，避免添加重复数据
     * @param flavors
     * @return
     */
    private String normalizeFlavorString(String flavors) {
        if (flavors == null || flavors.isEmpty()) {
            return flavors;
        }
        // 按逗号分割，排序后再拼接
        return Arrays.stream(flavors.split(","))
                .map(String::trim)  // 去除空格
                .filter(s -> !s.isEmpty())  // 过滤空字符串
                .sorted()  // 字典序排序
                .collect(Collectors.joining(","));
    }
}
