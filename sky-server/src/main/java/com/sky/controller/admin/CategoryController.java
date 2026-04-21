package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "分类管理")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult> page( CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询");
        PageResult categoryList = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(categoryList);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类");
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "修改分类状态")
    public Result status(@PathVariable Integer status,@RequestParam Long id){
        log.info("修改员工{}状态：{}",id,status);
        categoryService.status(id,status);
        return Result.success();
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "添加分类")
    public Result insert(@RequestBody CategoryDTO categoryDTO){
        log.info("添加分类");
        categoryService.insert(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除分类")
    public Result delete(@RequestParam Long id){
        log.info("删除分类");
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "根据类型查询分类")
    public Result<List<Category>> list(@RequestParam Integer type){
        log.info("根据类型查询分类");
        List<Category> categoryList = categoryService.list(type);
        return Result.success(categoryList);
    }
}
