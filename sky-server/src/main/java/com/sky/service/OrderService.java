package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 订单提交
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     *  支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单详情
     * @param id
     * @return
     */
    OrderVO getByOrderId(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancelById(Long id);

    /**
     * 再来一单
     * @param id
     */
    void again(Long id);

    /**
     * 订单催单
     * @param id
     */
    void remind(Long id);

    /**
     * 确认订单
     * @param ordersDTO
     */
    void confirm(OrdersDTO ordersDTO);

    /**
     * 统计订单数据
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 订单派送
     * @param id
     */
    void delivery(Long id);

    /**
     * 订单完成
     * @param id
     */
    void complete(Long id);

    /**
     * 订单拒绝
     * @param ordersDTO
     */
    void rejection(OrdersDTO ordersDTO);

    /**
     * 管理端取消订单
     * @param ordersDTO
     */
    void cancel(OrdersDTO ordersDTO);
}
