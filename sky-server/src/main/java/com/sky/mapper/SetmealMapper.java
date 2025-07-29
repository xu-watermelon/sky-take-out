package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {


    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    List<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from sky_take_out.setmeal where id=#{id}")
    SetmealDTO getById(long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    void delete(List<Long> ids);
}
