package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
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

    @Delete("delete from shopping_cart where user_id=#{userId}")
    void clean(ShoppingCart shoppingCart);

    /**
     * 根据id删除购物车数据
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

}
