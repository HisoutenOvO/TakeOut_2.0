package com.sky.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final SetmealDishMapper setmealDishMapper;

    /**
     * 分页查询
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        Long total = page.getTotal();
        List<DishVO> records = page.getResult();

        return new PageResult(total,records);
    }

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void insertWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //设置默认状态为禁用
        dish.setStatus(StatusConstant.DISABLE);
        dishMapper.insertWithFlavor(dish);
        //向口味表中添加数据
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        //检查是否填写了口味
        if(flavorList != null && !flavorList.isEmpty()){
            //为每一条口味数据添加菜品id
            flavorList.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insertDishFlavor(flavorList);
        }


    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //判断菜品能否被删除-菜品是否停售
        //这些id中但凡有一个在售的，则不能删除
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if(dish.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断菜品是否能被删除-菜品是否关联了套餐
        List<Long> SetmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(SetmealIds != null && !SetmealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品数据
        dishMapper.deleteBatchIds(ids);
        //删除关联的口味数据
        dishFlavorMapper.deleteBatchByDishId(ids);
    }

    /**
     * 根据套餐id获取菜品详情
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        return dishMapper.getListByCategoryId(categoryId);
    }

    /**
     * 根据id查询菜品数据及口味数据
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        //查询菜品数据
        Dish dish = dishMapper.selectById(id);
        //查询对应的口味数据
        List<DishFlavor> flavorList = dishFlavorMapper.getFlavorsByDishId(id);
        //封装进VO中
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavorList);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品信息
        dishMapper.updateDish(dish);
        //先删除所有口味数据
        dishFlavorMapper.deleteBatchByDishId(List.of(dish.getId()));
        //再把前端传过来的口味数据插入
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        if (flavorList != null && !flavorList.isEmpty()) {
            flavorList.forEach(flavor -> flavor.setDishId(dish.getId()));
            dishFlavorMapper.insertDishFlavor(flavorList);
        }
    }

    /**
     * 启售停售
     * @param id
     * @param status
     */
    @Override
    public void status(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.updateDish(dish);
    }
}
