package com.sky.controller.admin;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Tag(name = "订单接口")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @Operation(summary = "订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.getHistoryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @Operation(summary = "查询订单详情")
    public Result<OrderVO> getDetail(@PathVariable Long id){
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.getByOrderId(id);
        return Result.success(orderVO);
    }

    /**
     * 确认订单
     * @param ordersDTO
     * @return
     */
    @PutMapping("/confirm")
    @Operation(summary = "确认订单")
    public Result confirm(@RequestBody OrdersDTO ordersDTO){
        log.info("确认订单：{}", ordersDTO);
        orderService.confirm(ordersDTO);
        return Result.success();
    }

    /**
     * 订单统计
     * @return
     */
    @GetMapping("/statistics")
    @Operation(summary = "订单统计")
    public Result<OrderStatisticsVO> statistics(){
        log.info("订单统计");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单派送
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @Operation(summary = "订单配送")
    public Result delivery(@PathVariable Long id){
        log.info("订单配送：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 订单完成
     * @return
     */
    @PutMapping("/complete/{id}")
    @Operation(summary = "订单完成")
    public Result complete(@PathVariable Long id){
        log.info("订单完成：{}", id);
        orderService.complete(id);
        return Result.success();
    }

    /**
     * 拒单
     * @param ordersDTO
     * @return
     */
    @PutMapping("/rejection")
    @Operation(summary = "订单拒绝")
    public Result rejection(@RequestBody OrdersDTO ordersDTO){
        log.info("订单拒绝：{}", ordersDTO);
        orderService.rejection(ordersDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消订单")
    public Result cancel(@RequestBody OrdersDTO ordersDTO){
        log.info("取消订单：{}", ordersDTO);
        orderService.cancel(ordersDTO);
        return Result.success();
    }
}
