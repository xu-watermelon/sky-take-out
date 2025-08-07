package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    //添加地址
    @Override
    public void save(AddressBook addressBook) {
        Long id = BaseContext.getCurrentId();
        addressBook.setUserId(id);
        addressBook.setIsDefault(0);
        addressBookMapper.save(addressBook);

    }

    //查询当前登录用户的所有地址信息
    @Override
    public List<AddressBook> list() {

        AddressBook addressBook=new AddressBook();
        Long id = BaseContext.getCurrentId();
        addressBook.setUserId(id);
        List<AddressBook>list=addressBookMapper.list(addressBook);

        return list;
    }

    //根据id查询地址
    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook=new AddressBook();
        addressBook.setId(id);
      List< AddressBook> list= addressBookMapper.list(addressBook);
        AddressBook addressBookResult = list.get(0);
        return addressBookResult;
    }

    //查询默认地址
    @Override
    public AddressBook getDefaultAddressBook() {
        AddressBook addressBook=new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        List<AddressBook> list = addressBookMapper.list(addressBook);

        if(list!=null&&list.size()>0){
        AddressBook addressBookResult = list.get(0);
            return addressBookResult;
        }


    return null;
    }

    //根据id修改地址
    @Override
    public void update(AddressBook addressBook) {

        addressBookMapper.update(addressBook);


    }

    //根据id删除地址
    @Override
    public void delete(Long id) {
        AddressBook addressBook=new AddressBook();
        addressBook.setId(id);
        addressBookMapper.delete(addressBook);
    }

    //设置默认地址
    @Transactional
    @Override
    public void setDefault(AddressBook addressBook) {

        AddressBook addressBook1=new AddressBook();
        addressBook1.setUserId(BaseContext.getCurrentId());
        addressBook1.setIsDefault(1);


        //查找数据库中默认地址
        List<AddressBook> list = addressBookMapper.list(addressBook1);
        if(list!=null&&list.size()>0) {
            AddressBook addressBook2 = list.get(0);
            addressBook2.setIsDefault(0);
            addressBookMapper.update(addressBook2);
        }

        addressBook.setIsDefault(1);

        addressBookMapper.update(addressBook);
    }
}
