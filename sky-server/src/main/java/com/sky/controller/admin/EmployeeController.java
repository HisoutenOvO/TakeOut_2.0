package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/employee")
@Tag(name = "员工管理",description = "员工管理接口")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工{}正在登录",employeeLoginDTO.getUsername());
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
    @Operation(summary = "修改密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改员工id:{}的密码", BaseContext.getCurrentId());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    /**
     * 修改员工状态
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "员工状态")
    public Result status(@PathVariable Integer status,@RequestParam Long id){
        log.info("修改员工{}状态：{}",id,status);
        employeeService.setStatus(id,status);
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询");
        PageResult employeeList = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(employeeList);
    }

    /**
     * 根据id进行员工查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id进行员工信息查询")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息：{}",id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "员工信息修改")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工信息修改：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "员工信息添加")
    public Result insert(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工信息添加：{}",employeeDTO);
        employeeService.insert(employeeDTO);
        return Result.success();
    }
}
