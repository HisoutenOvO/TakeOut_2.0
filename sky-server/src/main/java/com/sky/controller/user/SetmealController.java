package com.sky.controller.user;

import com.sky.service.SetmealService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Tag(name = "用户套餐接口")
@Slf4j
@RequiredArgsConstructor
public class SetmealController {
    private final SetmealService setmealService;
}
