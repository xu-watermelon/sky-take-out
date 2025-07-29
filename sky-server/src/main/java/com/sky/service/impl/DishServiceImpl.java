package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;

import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //添加菜品
    @AutoFill(OperationType.INSERT)
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //菜品
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);

        Long id = dish.getId();

        //菜品口味

        List<DishFlavor> flavors = dishDTO.getFlavors();
        //判断口味是否为空
        if(flavors!=null && flavors.size()>0){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(id);
            }
            dishFlavorMapper.insert(flavors);

        }

    }

    //分页条件查询
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO>page =(Page<DishVO>) dishMapper.page(dishPageQueryDTO);




        return new PageResult(page.getTotal(),page.getResult());


    }
    //批量删除菜品
    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {
        //判断是不是起售状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            Integer status = dish.getStatus();
            if (status.equals(StatusConstant.ENABLE)){
                //抛出删除异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);

            }

        }

        //判断菜品是否关联套餐
        for (Long id : ids) {
            List<SetmealDish> list = setmealDishMapper.getById(id);
            if(list!=null&& !list.isEmpty()){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }

        }

        dishMapper.delete(ids);


        dishFlavorMapper.delete(ids);
    }

    //根据id查询菜品
    @Override
    public DishVO getById(Long id) {

        Dish dish = dishMapper.getById(id);

        List<DishFlavor> flavors = dishFlavorMapper.getById(id);

        DishVO dishVo =new DishVO();

        BeanUtils.copyProperties(dish,dishVo);

        dishVo.setFlavors(flavors);

        return dishVo;




    }

    //修改菜品
    @Transactional

    @Override
    public void updateDish(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();


        //修改菜品信息
        dishMapper.update(dish);
        //修改菜品口味信息
        //先删除口味信息
        dishFlavorMapper.delete(Collections.singletonList(dish.getId()));
        //再添加口味信息
        //判断口味是否为空
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insert(flavors);


        }
    }

//    菜品起售停售
    @Override
    public void setStatus(Integer status, long id) {
        Dish dish=new Dish();

        dish.setStatus(status);
        dish.setId(id);

       dishMapper.update(dish);
    }

    //根据分类id 查询菜品
    @Override
    public List<Dish> getBySort(long categoryId) {


       List<Dish>list= dishMapper.getBySort(categoryId);
       return list;

    }
}
