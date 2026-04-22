package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Tag(name = "用户套餐接口")
@Slf4j
@RequiredArgsConstructor
public class SetmealController {
    private final SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    @Operation(summary = "根据分类id查询套餐")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId){
        log.info("根据分类id:{}查询套餐",categoryId);
        List<Setmeal> setmealList = setmealService.getByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @Operation(summary = "根据套餐id查询包含的菜品")
    public Result<List<SetmealDish>> getDishByCategoryId(@PathVariable Long id){
        log.info("根据套餐id:{}查询包含的菜品",id);
        List<SetmealDish> setmealDishes = setmealService.getDishByCategoryId(id);
        return Result.success(setmealDishes);
    }
}
