package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.vo.EmployeeLoginVO;

public interface EmployeeService {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);

    /**
     * 编辑员工状态
     * @param status
     */
    void setStatus(Long id,Integer status);

    /**
     * 员工的分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void insert(EmployeeDTO employeeDTO);
}
