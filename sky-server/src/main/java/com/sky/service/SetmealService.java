package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;


public interface SetmealService {

    void save(SetmealDTO setmealDTO );

    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealDTO getById(long id);

    void update(SetmealDTO setmealDTO);

    void setStatus(Integer status,long id);

    void delete(List<Long> ids);
}
