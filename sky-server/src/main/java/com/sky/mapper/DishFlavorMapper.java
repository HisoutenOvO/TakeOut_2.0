package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    /**
     * 批量插入菜品口味数据
     * @param flavors
     */
    void insertDishFlavor(List<DishFlavor> flavors);

    /**
     * 根据菜品id批量删除口味数据
     * @param dishIds
     */
    void deleteBatchByDishId(List<Long> dishIds);

    /**
     * 根据菜品id查询对应的口味数据
     * @param dishId
     * @return
     */
    List<DishFlavor> getFlavorsByDishId(Long dishId);
}
