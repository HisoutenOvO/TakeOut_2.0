package com.sky.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.config.WebSocketConfiguration;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.webSocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

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
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        //分页查询历史订单
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> page = orderMapper.pageQuery(ordersPageQueryDTO);
        //一次性查询所有订单明细（批量查询）
        List<Long> orderIds = new ArrayList<>();
        for (OrderVO orderVO : page) {
            Long id = orderVO.getId();
            orderIds.add(id);
        }
        List<OrderDetail> orderDetailList = orderMapper.getOrderDetailByOrderIds(orderIds);
        //通过stream将订单明细数据封装到对应的订单中
        for (OrderVO orderVO : page) {
            List<OrderDetail> details = orderDetailList.stream()
                    //通过过滤器筛选出当前订单的订单明细
                    .filter(detail -> detail.getOrderId().equals(orderVO.getId()))
                    .collect(Collectors.toList());
            orderVO.setOrderDetailList(details);
        }
        //封装返回
        Long total = page.getTotal();
        List<OrderVO> records = page.getResult();
        return new PageResult(total,records);
    }
}
