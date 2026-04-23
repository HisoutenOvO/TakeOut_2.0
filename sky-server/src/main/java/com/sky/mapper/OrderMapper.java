package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.GetMapping;

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

//    /**
//     * 根据订单id查询菜品名称
//     * @param orderIds
//     * @return
//     */
//    List<String> getDishNameByOrderIds(List<Long> orderIds);
}
