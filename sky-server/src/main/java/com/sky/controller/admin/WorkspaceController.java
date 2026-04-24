package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
@Tag(name = "工作台接口")
@Slf4j
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    /**
     * 查询营业数据
     * @return
     */
    @GetMapping("/businessData")
    @Operation(summary = "营业数据")
    public Result<BusinessDataVO> businessData(){
        log.info("查询今日运营数据");
        BusinessDataVO businessDataVO = workspaceService.businessData();
        return Result.success(businessDataVO);
    }

    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @Operation(summary = "查询套餐总览")
    public Result<SetmealOverViewVO> getSetmealOverview(){
        log.info("查询套餐总览");
        SetmealOverViewVO setmealOverViewVO = workspaceService.getSetmealOverview();
        return Result.success(setmealOverViewVO);
    }

    /**
     * 查询菜品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    @Operation(summary = "查询菜品总览")
    public Result<DishOverViewVO> getDishOverview(){
        log.info("查询菜品总览");
        DishOverViewVO dishOverViewVO = workspaceService.getDishOverview();
        return Result.success(dishOverViewVO);
    }

    /**
     * 查询订单总览
     * @return
     */
    @GetMapping("overviewOrders")
    @Operation(summary = "查询订单总览")
    public Result<OrderOverViewVO> getOrderOverview(){
        log.info("查询订单总览");
        OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverview();
        return Result.success(orderOverViewVO);
    }
}
