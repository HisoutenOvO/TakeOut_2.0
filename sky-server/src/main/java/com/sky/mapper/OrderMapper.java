package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
    /**
     * 批量插入订单明细数据
     * @param orderDetailList
     */
    void insertOrderDetails(List<OrderDetail> orderDetailList);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    Orders getByOrderNum(String orderNumber);
}
