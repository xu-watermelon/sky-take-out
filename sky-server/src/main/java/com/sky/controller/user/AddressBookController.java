package com.sky.controller.user;


import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api("地址簿相关")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private AddressBookMapper addressBookMapper;

    @ApiOperation("添加地址")
    @PostMapping("")
    public Result save(@RequestBody AddressBook addressBook){

        log.info("添加地址:{}",addressBook);
        addressBookService.save(addressBook);

        return Result.success();

    }
    @ApiOperation("查询当前登录用户的所有地址信息")
    @GetMapping("list")
    public Result<List<AddressBook>> list(){

        List<AddressBook>list=addressBookService.list();
        log.info("查询当前登录用户:{}的所有地址信息",list.get(0).getUserId());
        return Result.success(list);
    }

    @ApiOperation("根据id查询地址")
    @GetMapping("/{id}")
    public Result<AddressBook>getById(@PathVariable Long id){

        log.info("根据id查询地址:{}",id);
        AddressBook addressBook=addressBookService.getById(id);

        return Result.success(addressBook);

    }

    @ApiOperation("/user/addressBook/default")
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddressBook(){

        AddressBook addressBook=addressBookService.getDefaultAddressBook();
        return Result.success(addressBook);
    }

    @ApiOperation("根据id修改地址")
    @PutMapping("")
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }

    @ApiOperation("根据id删除地址")
    @DeleteMapping("")
    public Result delete(Long id){
        addressBookService.delete(id);
        return Result.success();

    }

    @ApiOperation("设置默认地址")
    @PutMapping("default")
    public Result setDefault(@RequestBody  AddressBook addressBook) {

        addressBookService.setDefault(addressBook);

        return Result.success();
    }
}
