package com.sky.controller.user;

import com.sky.service.DishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Tag(name = "用户端ish接口")
@Slf4j
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;


}
