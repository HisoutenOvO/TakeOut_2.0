package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    /**
     * 分页查询菜品数据
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insertWithFlavor(Dish dish);

    /**
     * 根据分类id查询菜品数据
     * @param dish
     * @return
     */
    List<Dish> getListByCategoryId(Dish dish);

    /**
     * 修改菜品数据
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * 根据分类id查询菜品数量
     */
    Integer getCountByCategoryId(Long categoryId);

    /**
     * 查询指定套餐下的菜品状态
     * @param setmealId
     * @return
     */
    List<Integer> getStatusBySetmealId(Long setmealId);
}
