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

    /**
     * 获取默认地址
     *
     * @return
     */
     AddressBook getDefault();

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param id
     */
    void updateDefault(Long id);

    /**
     * 删除地址
     * @param id
     */
    void delete(Long id);
}
