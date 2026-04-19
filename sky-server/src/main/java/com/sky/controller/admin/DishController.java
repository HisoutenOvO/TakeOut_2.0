package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dish")
@RequiredArgsConstructor
@Slf4j
public class DishController {
    private final DishService dishService;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询");
        PageResult dishList = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(dishList);
    }

    /**
     * 新增菜品
     * @return
     */
    @PostMapping
    @Operation(summary = "新增菜品")
    public Result insertWithFlavor(@RequestBody DishDTO dishDTO){
        log.info("新增菜品");
        dishService.insertWithFlavor(dishDTO);
        return Result.success();
    }


}
