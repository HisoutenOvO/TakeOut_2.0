package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.*;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceMapper workspaceMapper;
    private final UserMapper userMapper;
    private final SetmealMapper setmealMapper;
    private final DishMapper dishMapper;
    private final OrderMapper orderMapper;

    /**
     * 查询营业数据
     * @return
     */
    @Override
    public BusinessDataVO businessData() {
        //查询今日新增用户数
        LocalDateTime todayBegin = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        Integer newUsers = workspaceMapper.getNewUserCount(todayBegin, todayEnd);
        //查询今日有效订单数，即已完成的订单
        Integer validOrderCount = workspaceMapper.getCompleteOrderCount(todayBegin,todayEnd);
        //查询营业额，即当天有效订单的总金额
        Double turnover = workspaceMapper.getMoney(todayBegin,todayEnd);
        if(turnover == null){
            turnover = 0.0;
        }
        //查询订单完成率，即当天有效订单数/总订单数
        Integer orderCount = workspaceMapper.getTodayOrderCount(todayBegin,todayEnd);
        Double orderCompletionRate = 0.0;
        if(!orderCount.equals(0)){
            orderCompletionRate = (double) validOrderCount / orderCount;
        }
        //查询平均客单价，即营业额/客户数量
        Long userCount =  userMapper.selectCount(null);
        Double unitPrice = 0.0;
        if(userCount != 0){
            unitPrice = (double) turnover / userCount;
        }
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .newUsers(newUsers)
                .unitPrice(unitPrice)
                .build();
    }

    /**
     * 获取套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverview() {
        Integer discontinued = setmealMapper.getCountByStatus(StatusConstant.DISABLE);
        Integer sold = setmealMapper.getCountByStatus(StatusConstant.ENABLE);
        return SetmealOverViewVO.builder().sold(sold).discontinued(discontinued).build();
    }

    /**
     * 获取菜品总览
     * @return
     */
    @Override
    public DishOverViewVO getDishOverview() {
        Integer discontinued = dishMapper.getCountByStatus(StatusConstant.DISABLE);
        Integer sold = dishMapper.getCountByStatus(StatusConstant.ENABLE);
        return DishOverViewVO.builder().sold(sold).discontinued(discontinued).build();
    }

    /**
     * 获取订单总览
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverview() {
        //查询全部订单
        Long allOrders = orderMapper.selectCount(null);
        //查看取消订单数
        Integer cancelledOrders = orderMapper.getCountByStatus(Orders.CANCELLED);
        //查看完成订单数
        Integer completedOrders = orderMapper.getCountByStatus(Orders.COMPLETED);
        //查看待派送订单数
        Integer deliveredOrders = orderMapper.getCountByStatus(Orders.CONFIRMED);
        //查看待接单订单数
        Integer waitingOrders = orderMapper.getCountByStatus(Orders.TO_BE_CONFIRMED);
        return OrderOverViewVO.builder()
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders.intValue())
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders).build();
    }
}
