package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.entity.AddressBook;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    /**
     * 新增
     * @param addressBook
     */
    @AutoFill(OperationType.INSERT)
    void add(AddressBook addressBook);
}
