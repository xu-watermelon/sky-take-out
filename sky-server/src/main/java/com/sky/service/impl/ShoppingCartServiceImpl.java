package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {


    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前所要添加的商品是否在购物车中已存在

        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果不为空,number加一
        if (list!=null&&list.size()>0){
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            //更新数据
            shoppingCartMapper.update(cart);
        }else {
            //如果为空的话,就像数据库中插入一条数据
                //判断是套餐还是菜品
            Long dishId = shoppingCart.getDishId();
            if(dishId!=null){
                //菜品
                Dish dish = dishMapper.getById(shoppingCart.getDishId());

                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }else {
                //套餐
                SetmealVO setmealVO = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmealVO.getName());
                shoppingCart.setAmount(setmealVO.getPrice());
                shoppingCart.setImage(setmealVO.getImage());


            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }



    }

    //查看购物车
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart=new ShoppingCart();
        Long id = BaseContext.getCurrentId();
        shoppingCart.setId(id);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }
}
