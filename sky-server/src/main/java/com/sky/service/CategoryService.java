package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;


public interface CategoryService {
    void save(CategoryDTO categoryDTO);

    void deleteById(long id);

    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    void setStatus(Integer status, long id);

    void update(CategoryDTO categoryDTO);

    List<Category> getByType(Integer type);
}
