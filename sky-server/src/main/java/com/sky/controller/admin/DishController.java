package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "菜品管理接口")
public class DishController {
    private final DishService dishService;
    private final RedisTemplate redisTemplate;

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
        //清理缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 删除菜品
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品");
        dishService.delete(ids);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品");
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @return
     */
    @PutMapping
    @Operation(summary = "修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品");
        dishService.update(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 修改菜品状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status,@RequestParam Long id){
        log.info("修改菜品{}状态：{}",id,status);
        dishService.status(id,status);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id获取菜品详情
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品")
    public Result<List<Dish>> list(@RequestParam Long categoryId){
        log.info("根据分类id:{}查询菜品",categoryId);
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }



    //私有方法，清理缓存
    private void cleanCache(String pattern){
        redisTemplate.delete(redisTemplate.keys(pattern));
    }
}
