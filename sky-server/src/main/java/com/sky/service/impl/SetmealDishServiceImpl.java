package com.sky.service.impl;

import com.sky.mapper.SetmealDishMapper;
import com.sky.service.SetmealDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetmealDishServiceImpl implements SetmealDishService {
    private final SetmealDishMapper setmealDishMapper;
}
