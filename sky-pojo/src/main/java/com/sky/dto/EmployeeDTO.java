package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//用来封装新增员工时的数据传输参数
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
