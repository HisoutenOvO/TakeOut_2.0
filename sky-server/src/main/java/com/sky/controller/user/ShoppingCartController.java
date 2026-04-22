package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Tag(name = "用户购物车接口")
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查看购物车")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车");
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        return Result.success(shoppingCartList);
    }

    /**
     * 删除购物车中的数据
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @Operation(summary = "删除购物车中的数据")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中的数据：{}",shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }
}
