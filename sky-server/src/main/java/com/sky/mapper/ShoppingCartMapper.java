package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    //动态条件查询
    List<ShoppingCart>list(ShoppingCart shoppingCart);

    //更新数据
    @Update("update sky_take_out.shopping_cart  set number=#{number} where id=#{id}")
    void update(ShoppingCart shoppingCart);

    //插入数据

    void insert(ShoppingCart shoppingCart);
}
