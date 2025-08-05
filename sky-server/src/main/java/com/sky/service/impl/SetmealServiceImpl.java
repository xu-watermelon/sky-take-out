package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    //添加套餐
    @Override
    public void save(SetmealDTO setmealDTO) {

        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //添加套餐信息
        setmealMapper.insert(setmeal);

        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmeal.getId());
        }

        //添加套餐中包含的菜品信息
        setmealDishMapper.insert(setmealDTO.getSetmealDishes());

    }

    //分页查询
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

       Page<SetmealVO>page=(Page<SetmealVO>) setmealMapper.page(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());

    }

    //根据id查询套餐
    @Override
    public SetmealDTO getById(long id) {

       SetmealDTO setmealDTO= setmealMapper.getById(id);

        List<SetmealDish> list = setmealDishMapper.getById(id);
        setmealDTO.setSetmealDishes(list);
        return setmealDTO;
    }

    //修改套餐
    @Override
    public void update(SetmealDTO setmealDTO) {

        //修改套餐
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //修改套餐菜品
            //先删除
          setmealDishMapper.delete(Collections.singletonList(setmealDTO.getId()));
           //再添加
        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealDTO.getId());
        }
          setmealDishMapper.insert(setmealDTO.getSetmealDishes());


    }

    //套餐起售停售
    @Override
    public void setStatus(Integer status,long id) {
        Setmeal setmeal=new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.update(setmeal);
    }

    //批量删除套餐
    @Override
    public void delete(List<Long> ids) {

        //删除套餐
        setmealMapper.delete(ids);
        //删除套餐菜品对应表
        setmealDishMapper.delete(ids);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
