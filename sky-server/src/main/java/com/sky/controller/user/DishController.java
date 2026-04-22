package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Tag(name = "用户端ish接口")
@Slf4j
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;
    private final RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询当前分类下的菜品")
    @Cacheable(cacheNames = "dish" , key = "#categoryId")
    public Result<List<DishVO>> list(@RequestParam Long categoryId){
        log.info("查询当前分类下的菜品");
        List<DishVO> dishVOList = dishService.getByCategoryId(categoryId);
        return Result.success(dishVOList);
    }
}
