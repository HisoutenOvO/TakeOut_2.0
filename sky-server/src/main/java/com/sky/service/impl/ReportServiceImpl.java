package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
   private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final DishMapper dishMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end) {
        //获取日期列表
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //获取营业额列表
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0.0 : turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        //获取日期列表
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> userList =  new ArrayList<>();
        List<Integer> allUserList =  new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            Integer userCount = userMapper.sumByMap(map);
            Integer allUserCount = userMapper.sumByMap(null);
            userList.add(userCount == null ? 0 : userCount);
            allUserList.add(allUserCount == null ? 0 : allUserCount);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(userList,","))
                .totalUserList(StringUtils.join(allUserList,","))
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        //获取日期列表
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer validOrderCount = 0;
        Double orderCompletionRate = 0.0;
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            Integer orderCount = orderMapper.getCountByStatusAndDate(map);
            map.put("status", Orders.COMPLETED);
            Integer validOrder = orderMapper.getCountByStatusAndDate(map);
            orderCountList.add(orderCount.intValue());
            validOrderCountList.add(validOrder);
            totalOrderCount = orderMapper.getCountByStatusAndDate(null);
            validOrderCount = orderMapper.getCountByStatus(Orders.COMPLETED);
        }
        if(totalOrderCount == null){
            totalOrderCount = 0;
        }
        if(validOrderCount == null){
            validOrderCount = 0;
        }
        if(totalOrderCount != 0 && validOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount.doubleValue();
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 销售top10
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO salesTop10Report(LocalDate begin, LocalDate end) {
        List<GoodsSalesDTO> orderDetails = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        Map map =new HashMap<>();
        map.put("begin",beginTime);
        map.put("end",endTime);

        orderDetails = orderMapper.getSalesTop10(map);

        for (GoodsSalesDTO orderDetail : orderDetails) {
            nameList.add(orderDetail.getName());
            numberList.add(orderDetail.getNumber());
        }
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }
}
