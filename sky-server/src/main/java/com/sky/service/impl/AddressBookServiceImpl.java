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


}
