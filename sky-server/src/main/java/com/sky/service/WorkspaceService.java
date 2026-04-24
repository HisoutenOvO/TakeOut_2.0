package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkspaceService {
    /**
     * 查询营业数据
     * @return
     */
    BusinessDataVO businessData();

    /**
     * 套餐总览
     * @return
     */
    SetmealOverViewVO getSetmealOverview();

    /**
     * 菜品总览
     * @return
     */
    DishOverViewVO getDishOverview();

    /**
     * 订单总览
     * @return
     */
    OrderOverViewVO getOrderOverview();
}
