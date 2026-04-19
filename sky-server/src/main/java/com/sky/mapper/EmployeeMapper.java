package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 根据员工id更新
     */
    @AutoFill(OperationType.UPDATE)
    @Update("update employee set password = #{password},update_time = #{updateTime} where id = #{id}")
    void updateByEmployeeId(Employee employee);

}
