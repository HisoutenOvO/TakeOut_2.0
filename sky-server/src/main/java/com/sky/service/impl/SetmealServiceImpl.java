package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SetmealServiceImpl implements SetmealService {
    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final DishMapper dishMapper;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);

        Long total = page.getTotal();
        List<SetmealVO> records = page.getResult();

        return new PageResult(total,records);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //新增一条套餐数据
        setmealMapper.insertSetmeal(setmeal);
        //新增多条菜品数据
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        if(dishes != null && !dishes.isEmpty()){
            dishes.forEach(dish->dish.setSetmealId(setmeal.getId()));
            setmealDishMapper.insertBySetmealId(dishes);
        }
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        //获取套餐信息
        Setmeal setmeal = setmealMapper.selectById(id);
        //获取套餐关联的菜品信息
        List<SetmealDish> dishes = setmealDishMapper.getDishesBySetmealId(id);
        //封装成SetmealVO返回
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        //先更新套餐数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.updateById(setmeal);
        //删除套餐关联的菜品数据
        setmealDishMapper.deleteBatchBySetmealId(List.of(setmeal.getId()));
        //插入新的菜品数据
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        if(dishes != null && !dishes.isEmpty()){
            dishes.forEach(dish->dish.setSetmealId(setmeal.getId()));
            setmealDishMapper.insertBySetmealId(dishes);
        }
    }

    /**
     * 套餐起售停售
     * @param id
     * @param status
     */
    @Override
    public void status(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        //判断能否起售
        List<Integer> statusList = dishMapper.getStatusBySetmealId(id);
        for (Integer dishStatus : statusList) {
            if(dishStatus == StatusConstant.DISABLE){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        setmealMapper.updateById(setmeal);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //判断能否被删除-套餐是否起售
        List<Setmeal> setmealList = setmealMapper.getListByIds(ids);
        for (Setmeal setmeal : setmealList) {
            if(setmeal.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.deleteBatchIds(ids);
        //删除套餐关联的菜品数据
        for (Long id : ids) {
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Override
    public List<Setmeal> getByCategoryId(Long categoryId) {
        return setmealMapper.getListByCategoryId(categoryId);
    }


}
