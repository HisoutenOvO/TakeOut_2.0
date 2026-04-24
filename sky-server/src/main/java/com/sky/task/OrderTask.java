package com.sky.task;


import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTask {
    private final OrderMapper orderMapper;

    /**
     * 定时任务，处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOutOrder(){
        log.info("定时处理超时未支付订单:{}",LocalDateTime.now());
        //查找所有状态为待支付且下单时间超过15分钟的订单
        LocalDateTime orderTime = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, orderTime);
        if (!ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason(MessageConstant.ORDER_OVER_TIME);
            }
            orderMapper.updateBatch(ordersList);
        }
    }


    /**
     * 处理一直在派送中的订单
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理一直处于派送中的订单:{}",LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);
        if (!ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
            }
            orderMapper.updateBatch(ordersList);
        }
    }
}
