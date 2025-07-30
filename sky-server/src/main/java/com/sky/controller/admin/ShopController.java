package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api
public class ShopController {

    public static final String  KEYS="SHOP_STATUS";

    @Autowired
   private RedisTemplate redisTemplate;

    //设置营业状态
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setStatus(@PathVariable Integer status){

        log.info("设置店铺状态为:{}",status==1?"营业中":"打烊中");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEYS,status);


        return Result.success();
    }

    //获取营业状态
    @ApiOperation("获取营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(KEYS);
        log.info("店铺营业状态为:{}",status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}
