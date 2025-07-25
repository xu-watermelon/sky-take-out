package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "分类相关接口")
@RequestMapping("admin/category")
public class CategoryController {
    @Autowired
   private CategoryService categoryService;




    //新增分类
    @ApiOperation("新增分类")
    @PostMapping("")
    public Result save(@RequestBody CategoryDTO categoryDTO){

        categoryService.save(categoryDTO);
        return Result.success();
    }

    //根据id删除分类
    @ApiOperation("根据id删除分类")
    @DeleteMapping("")
    public Result deleteById(long id){

        categoryService.deleteById(id);


        return Result.success();
    }

    //分类分页查询
    @ApiOperation("分类分页查询")
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页条件查询,名称:{},分类:{},页码:{},每页记录数:{}",
                categoryPageQueryDTO.getName(),categoryPageQueryDTO.getType(),
                categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
       PageResult pageResult= categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    //启用禁用状态
    @ApiOperation("启用禁用状态")
    @PostMapping("/status/{status}")
    public Result setStatus(@PathVariable Integer status,long id){

        categoryService.setStatus(status,id);
        return Result.success();
    }

    //修改分类
    @ApiOperation("修改分类")
    @PutMapping("")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    //根据类型查询分类
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List> getByType(long type){
        log.info("根据类型查询分类:{}",type);
       List<Category> list = categoryService.getByType(type);

        return Result.success(list);
    }


}
