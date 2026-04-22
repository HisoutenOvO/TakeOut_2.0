package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

    /**
     * 查询地址
     * @return
     */
    List<AddressBook> list();
}
