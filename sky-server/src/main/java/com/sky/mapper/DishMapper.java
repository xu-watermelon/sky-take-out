package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    List<com.sky.vo.DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    void delete(List<Long> ids);

    @Select("select * from sky_take_out.dish where id=#{id}")
    Dish getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from sky_take_out.dish where category_id=#{categoryId}")
    List<Dish> getBySort(long categoryId);
}
