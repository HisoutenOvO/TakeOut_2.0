package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    /**
     * 通过菜品id群查找套餐id群
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐和菜品的关联数据
     * @param dishes
     */
    void insertBySetmealId(List<SetmealDish> dishes);

    /**
     * 通过套餐id查询套餐和菜品的关联数据
     * @param SetmealId
     * @return
     */
    List<SetmealDish> getDishesBySetmealId(Long SetmealId);

    /**
     * 批量删除套餐和菜品的关联数据
     * @param setmealIds
     */
    void deleteBatchBySetmealId(List<Long> setmealIds);

    /**
     * 通过套餐id删除套餐和菜品的关联数据
     * @param id
     */
    void deleteBySetmealId(Long id);

    /**
     * 通过菜品id查询关联的套餐id
     * @param id
     * @return
     */
    List<Long> getSetmealIdsByDishId(Long id);

    /**
     * 通过套餐id查询套餐状态
     * @param id
     * @return
     */
    Integer getSetmealStatusBySetmealId(Long id);
}
