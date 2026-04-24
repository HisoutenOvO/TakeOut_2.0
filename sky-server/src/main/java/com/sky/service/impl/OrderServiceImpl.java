package com.sky.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.webSocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    public final OrderMapper orderMapper;
    private final AddressBookMapper addressBookMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WebSocketServer webSocketServer;
    private final ObjectMapper objectMapper;

    /**
     * 订单支付
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种事务异常（如地址为空，购物车为空）
        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
        if(shoppingCartList == null || shoppingCartList.isEmpty()){
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        String addressName = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail();
        //向数据表插入一条订单数据，要求主键回显
        //装填orders对象
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(Orders.UN_PAID);
        orders.setNumber(String.valueOf(System.currentTimeMillis() + orders.getUserId()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressName);
        orders.setDeliveryTime(LocalDateTime.now().plusHours(1));
        orderMapper.insert(orders);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        //向数据表插入n条订单明细记录
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderMapper.insertOrderDetails(orderDetailList);
        //清空购物车
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        //分装成VO对象返回
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return orderSubmitVO;
    }


    /**
     * 模拟订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO){
        //模拟支付成功，跳过微信支付相关业务，直接回调paySuccess
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return new OrderPaymentVO();
    }

    /**
     * 支付成功
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {
        // 根据订单号查询订单
        Orders orders = orderMapper.getByOrderNum(outTradeNo);
        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayMethod(1);
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());
        orderMapper.updateById(orders);
        //通过WebSocket向客户端推送消息
        Map map = new HashMap<>();
        map.put("type", 1);//1表示来单提醒
        map.put("orderId", orders.getId());
        map.put("content", "订单号：" + outTradeNo);
        //将map转为JSON并通过WebSocket发送给客户端
        try {
            webSocketServer.sendToAllClient(objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setUserId(ordersPageQueryDTO.getUserId());
        //分页查询历史订单
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<OrderVO> page = orderMapper.pageQuery(ordersPageQueryDTO);
        //一次性查询所有订单明细（批量查询）
        List<Long> orderIds = new ArrayList<>();
        for (OrderVO orderVO : page) {
            Long id = orderVO.getId();
            orderIds.add(id);
        }
        List<OrderDetail> orderDetailList = new ArrayList<>();
        if(!orderIds.isEmpty()) {
            orderDetailList = orderMapper.getOrderDetailByOrderIds(orderIds);
        }
        //通过stream将订单明细数据封装到对应的订单中
        for (OrderVO orderVO : page) {
            List<OrderDetail> details = orderDetailList.stream()
                    //通过过滤器筛选出当前订单的订单明细
                    .filter(detail -> detail.getOrderId().equals(orderVO.getId()))
                    .collect(Collectors.toList());
            orderVO.setOrderDetailList(details);
            //用stream将菜品名称用逗号拼接并赋给orderVO
            String dishName = details.stream().map(OrderDetail::getName).collect(Collectors.joining(","));
            orderVO.setOrderDishes(dishName);
        }
        //封装返回
        Long total = page.getTotal();
        List<OrderVO> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO getByOrderId(Long id) {
        //查询订单信息
        Orders orders = orderMapper.selectById(id);
        //查询订单明细
        Long orderId = orders.getId();
        List<OrderDetail> orderDetailList = orderMapper.getOrderDetailByOrderIds(List.of(orderId));
        //封装VO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 订单取消
     * @param id
     */
    @Override
    public void cancelById(Long id) {
        Orders orders = orderMapper.selectById(id);
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelTime(LocalDateTime.now());
        orders.setCancelReason(MessageConstant.ORDER_CANCELLED_BY_USER);
        orderMapper.updateById(orders);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void again(Long id) {
        List<OrderDetail> orderDetailList = orderMapper.getOrderDetailByOrderIds(List.of(id));
        List<ShoppingCart> shoppingCartList =new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail,shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCartList.add(shoppingCart);
        }
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 订单催单
     * @param id
     */
    @Override
    public void remind(Long id) {}

    /**
     * 确认订单
     * @param ordersDTO
     */
    @Override
    public void confirm(OrdersDTO ordersDTO) {
        Long orderId = ordersDTO.getId();
        Orders orders = orderMapper.selectById(orderId);
        orders.setStatus(Orders.CONFIRMED);
        orderMapper.updateById(orders);
    }

    /**
     * 订单统计
     * @return OrderStatisticsVO
     */
    @Override
    public OrderStatisticsVO statistics() {
        Integer confirmedCount = orderMapper.getCountByStatus(Orders.CONFIRMED);
        Integer deliveryInProgressCount = orderMapper.getCountByStatus(Orders.DELIVERY_IN_PROGRESS);
        Integer toBeConfirmed = orderMapper.getCountByStatus(Orders.TO_BE_CONFIRMED);
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(confirmedCount);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgressCount);
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        return orderStatisticsVO;
    }

    /**
     * 订单派送
     * @param id
     */
    @Override
    public void delivery(Long id) {
        Orders orders = orderMapper.selectById(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.updateById(orders);
    }

    /**
     * 订单完成
     * @param id
     */
    @Override
    public void complete(Long id) {
        Orders orders = orderMapper.selectById(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setOrderTime(LocalDateTime.now());
        orderMapper.updateById(orders);
    }

    /**
     * 订单拒接
     * @param ordersDTO
     */
    @Override
    public void rejection(OrdersDTO ordersDTO) {
        Orders orders = orderMapper.selectById(ordersDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersDTO.getRejectionReason());
        orderMapper.updateById(orders);
    }

    /**
     * 取消订单
     * @param ordersDTO
     */
    @Override
    public void cancel(OrdersDTO ordersDTO) {
        Orders orders = orderMapper.selectById(ordersDTO.getId());
        Integer status = orders.getStatus();
        //判断是否符合可取消的条件
        if(status != Orders.TO_BE_CONFIRMED && status != Orders.CONFIRMED && status != Orders.DELIVERY_IN_PROGRESS ){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(orders);
    }


}
