package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Tag(name = "统计接口")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;

    /**
     * 营业额统计
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @Operation(summary = "营业额统计")
    public Result<TurnoverReportVO> turnoverReport(@RequestParam LocalDate begin,@RequestParam LocalDate end){
        log.info("营业额统计");
        TurnoverReportVO turnoverReportVO = reportService.turnoverReport(begin,end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 用户统计
     * @return
     */
    @GetMapping("/userStatistics")
    @Operation(summary = "用户统计")
    public Result<UserReportVO> userReport(@RequestParam LocalDate begin,@RequestParam LocalDate end){
        log.info("用户统计");
        UserReportVO userReportVO = reportService.userReport(begin,end);
        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @Operation(summary = "订单统计")
    public Result<OrderReportVO> orderReport(@RequestParam LocalDate begin,@RequestParam LocalDate end){
        log.info("订单统计");
        OrderReportVO orderReportVO = reportService.orderReport(begin,end);
        return Result.success(orderReportVO);
    }
}
