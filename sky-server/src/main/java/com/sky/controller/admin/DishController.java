package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api("菜品管理")
@Slf4j

public class DishController {

    @Autowired
    private DishService dishService;


    //添加菜品
    @ApiOperation("添加菜品")
    @PostMapping("")
    public Result save(@RequestBody DishDTO dishDTO){

        log.info("添加菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    //分页条件查询
    @ApiOperation("条件分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){

        log.info("分页条件查询:{}",dishPageQueryDTO);
        PageResult pageResult= dishService.page(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    //根据id查询菜品
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO>getById(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
       DishVO dishVO= dishService.getById(id);
       return Result.success(dishVO);

    }


    //批量删除菜品
    @ApiOperation("批量删除菜品")
    @DeleteMapping("")
    public Result deleteDish(@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}",ids);
        dishService.deleteDish(ids);
        return Result.success();
    }

    //修改菜品
    @ApiOperation("修改菜品")
    @PutMapping("")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);

        return Result.success();
    }

    //菜品起售停售
    @ApiOperation("菜品起售停售")
    @PostMapping("/status/{status}")
    public Result setStatus(@PathVariable Integer status,long id){

        dishService.setStatus(status,id);

        return  Result.success();
    }

}
