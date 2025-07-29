package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.ArrayList;
import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);

    DishVO getById(Long id);

    void updateDish(DishDTO dishDTO);

    void setStatus(Integer status, long id);

    List<Dish> getBySort(long categoryId);
}
