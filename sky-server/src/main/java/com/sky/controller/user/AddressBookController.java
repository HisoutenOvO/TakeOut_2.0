package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Tag(name = "用户地址管理接口")
@RequiredArgsConstructor
@Slf4j
public class AddressBookController {
    private final AddressBookService addressBookService;

    /**
     *  添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @Operation(summary = "添加地址")
    public Result add(@RequestBody AddressBook addressBook){
        log.info("添加地址：{}",addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }


    @GetMapping("/list")
    @Operation(summary = "查看地址")
    public Result<List<AddressBook>> list(){
        log.info("查看地址");
        List<AddressBook> addressBookList = addressBookService.list();
        return Result.success(addressBookList);
    }
}
