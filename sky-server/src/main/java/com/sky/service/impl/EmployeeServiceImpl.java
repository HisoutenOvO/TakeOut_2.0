package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.LoginFailedException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        //判断异常情况
        //先用mp查询账号
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        wrapper.eq(Employee::getUsername,username);
        Employee employee = employeeMapper.selectOne(wrapper);

        //判断是否有异常情况，如不存在，密码错误或被锁定
        //先判断不存在和被锁定
        if(employee == null){
            throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(employee.getStatus().equals(StatusConstant.DISABLE)){
            throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //对密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //再验证密码是否正确
        if(!password.equals(employee.getPassword())){
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);
        }

        //若一切无误，则登录成功，生成jwt令牌
        //创建jwt token的载荷数据claims，即存放token的载体
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        //生成token
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(),jwtProperties.getAdminTtl(),claims);
        return EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Long empId = BaseContext.getCurrentId();
        String newPwd = passwordEditDTO.getNewPassword();
        String oldPwd = passwordEditDTO.getOldPassword();
        //进行md5加密
        oldPwd = DigestUtils.md5DigestAsHex(oldPwd.getBytes());
        newPwd = DigestUtils.md5DigestAsHex(newPwd.getBytes());
        //检验老密码是否和数据库中存放的密码一致
        String psw = employeeMapper.selectById(empId).getPassword();
        if(!oldPwd.equals(psw)){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //再检验新老密码是否一致
        if(newPwd.equals(oldPwd)){
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
        //若全部一致，则修改密码

        Employee employee = Employee.builder().id(empId).password(newPwd).build();
        employeeMapper.updateByEmployeeId(employee);
    }
}
