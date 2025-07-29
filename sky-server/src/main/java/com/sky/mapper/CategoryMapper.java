package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper

public interface CategoryMapper {
    //添加分类
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into sky_take_out.category ( type, name, sort, status, create_time, update_time, create_user, update_user) values (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
      void save(Category category) ;
    //删除分类
    @Delete("delete  from sky_take_out.category where id=#{id}")
    void deleteById(Category category);

    //分页条件查询
    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    //修改分类
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);
    @Select("select * from sky_take_out.category where type=#{type}")
    List<Category> getByType(long type);
}
