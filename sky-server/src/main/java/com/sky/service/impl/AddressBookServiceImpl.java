package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressBookServiceImpl implements AddressBookService {
    private final AddressBookMapper addressBookMapper;
    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.add(addressBook);
    }

    /**
     * 获取全部地址
     * @return
     */
    @Override
    public List<AddressBook> list() {
        return addressBookMapper.selectList(null);
    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public AddressBook getDefault() {
        return addressBookMapper.getDefault();
    }

    /**
     * 获取指定id地址
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.selectById(id);
    }

    /**
     * 修改地址
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    /**
     * 设置默认地址
     * @param id
     */
    @Override
    public void updateDefault(Long id) {
        //把所有地址默认值改成0
        addressBookMapper.changeAllDefault();
        //把目标地址改成1
        AddressBook addressBook = addressBookMapper.selectById(id);
        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);
    }

    /**
     * 删除地址
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressBookMapper.deleteById(id);
    }


}
