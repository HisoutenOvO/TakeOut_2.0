package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.entity.AddressBook;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    /**
     * 新增
     * @param addressBook
     */
    @AutoFill(OperationType.INSERT)
    void add(AddressBook addressBook);

    /**
     * 查询默认地址
     * @return
     */
    @Select("select * from address_book where is_default = 1")
    AddressBook getDefault();

    /**
     * 将所以地址默认值置0
     */
    @Update("update address_book set is_default = 0")
    void changeAllDefault();
}
