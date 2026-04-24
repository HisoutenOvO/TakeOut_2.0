package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据订单id查询订单详情
     * @param orderIds
     * @return
     */
    List<OrderDetail> getOrderDetailByOrderIds(List<Long> orderIds);

    /**
     * 分页查询历史订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询各状态订单总数
     * @return
     */
    @Select("select count(0) from orders where status = #{status}")
    Integer getCountByStatus(Integer status);

    /**
     * 根据状态和下单时间查询订单
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * 批量更新订单
     * @param ordersList
     */
    void updateBatch(List<Orders> ordersList);

    /**
     * 根据动态条件查询数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);
}
