package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Select("select * from sky_take_out.setmeal_dish where setmeal_dish.setmeal_id=#{id}")
    List<SetmealDish> getById(Long id);



    void insert(List<SetmealDish> setmealDishes);


    void delete(List<Long> ids);
}
