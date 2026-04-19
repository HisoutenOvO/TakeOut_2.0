package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.vo.EmployeeLoginVO;

public interface EmployeeService {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);
}
