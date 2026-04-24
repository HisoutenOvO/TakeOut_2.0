package com.sky.mapper;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface WorkspaceMapper {
    /**
     * 查询当天新增用户
     * @param todayBegin
     * @param todayEnd
     */
    @Select("select count(0) from user where create_time between #{todayBegin} and #{todayEnd}")
    Integer getNewUserCount(LocalDateTime todayBegin, LocalDateTime todayEnd);

    /**
     * 查询当天有效订单数
     * @param todayBegin
     * @param todayEnd
     */
    @Select("select count(0) from orders where status = 5 and order_time between #{todayBegin} and #{todayEnd}")
    Integer getCompleteOrderCount(LocalDateTime todayBegin, LocalDateTime todayEnd);

    /**
     * 查询当天营业额
     * @param todayBegin
     * @param todayEnd
     * @return
     */
    @Select("select sum(amount) from orders where status = 5 and order_time between #{todayBegin} and #{todayEnd}")
    Double getMoney(LocalDateTime todayBegin, LocalDateTime todayEnd);

    /**
     * 查询当天订单数
     * @param todayBegin
     * @param todayEnd
     * @return
     */
    @Select("select count(0) from orders where order_time between #{todayBegin} and #{todayEnd}")
    Integer getTodayOrderCount(LocalDateTime todayBegin, LocalDateTime todayEnd);
}
