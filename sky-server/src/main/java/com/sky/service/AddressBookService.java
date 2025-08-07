package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    //添加地址
    void save(AddressBook addressBook);

    //查询当前登录用户的所有地址信息
    List<AddressBook> list();

    //根据id查询地址
    AddressBook getById(Long id);
    //查询默认地址
    AddressBook getDefaultAddressBook();
    //根据id修改地址
    void update(AddressBook addressBook);

    //根据id删除地址
    void delete(Long id);

    //设置默认地址
    void setDefault(AddressBook addressBook);
}
