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

    /**
     * 查看全部地址
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查看地址")
    public Result<List<AddressBook>> list(){
        log.info("查看地址");
        List<AddressBook> addressBookList = addressBookService.list();
        return Result.success(addressBookList);
    }


    /**
     * 查看默认地址
     * @return
     */
    @GetMapping("/default")
    @Operation(summary = "查看默认地址")
    public Result<AddressBook> getDefault(){
        log.info("查看默认地址");
        AddressBook defaultAddress = addressBookService.getDefault();
        return Result.success(defaultAddress);
    }

    /**
     * 查看地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "查看地址")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("查看地址：{}",id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    @Operation(summary = "修改地址")
    public Result update(@RequestBody AddressBook addressBook){
        log.info("修改地址：{}",addressBook);
        addressBookService.update(addressBook);
        return  Result.success();
    }

    /**
     * 修改默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @Operation(summary = "修改默认地址")
    public Result updateDefault(@RequestBody AddressBook addressBook){
        log.info("修改默认地址：{}",addressBook.getId());
        addressBookService.updateDefault(addressBook.getId());
        return Result.success();
    }

    /**
     * 删除地址
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除地址")
    public Result delete(@RequestParam Long id){
        log.info("删除地址");
        addressBookService.delete(id);
        return Result.success();
    }
}
