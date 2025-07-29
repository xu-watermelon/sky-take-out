package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    //添加套餐
    @ApiOperation("添加套餐")
    @PostMapping("")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("添加套餐:{}",setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }


    //分页查询
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询:{}",setmealPageQueryDTO);

       PageResult pageResult= setmealService.page(setmealPageQueryDTO);
       return Result.success(pageResult);

    }


    //根据id查询套餐
    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealDTO>getById(@PathVariable long id){

        log.info("跟觉id查询套餐:{}",id);
       SetmealDTO setmealDTO= setmealService.getById(id);


        return Result.success(setmealDTO);
    }

    //套餐起售停售
    @ApiOperation("套餐起售停售")
    @PostMapping("/status/{status}")
    public Result setStatus(@PathVariable Integer status ,long id){

        log.info("套餐起售停售:{},{}",status,id);
        setmealService.setStatus(status,id);

        return Result.success();
    }


    //修改套餐
    @ApiOperation("修改套餐")
    @PutMapping("")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);
        setmealService.update(setmealDTO);

        return Result.success();

    }

    //批量删除套餐
    @ApiOperation("批量删除套餐")
    @DeleteMapping("")
    public Result delete(@RequestParam List<Long>ids){


        setmealService.delete(ids);

        return Result.success();
    }

}
