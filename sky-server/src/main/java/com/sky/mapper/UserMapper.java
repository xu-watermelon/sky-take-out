package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from sky_take_out.user where openid=#{openid}")
  User getByOpenid(String openid);


    void insert(User user);

    @Select("select *from sky_take_out.user where id=#{userId}")
    User getById(Long userId);

    //统计用户数量
    @Select("select count(id) from sky_take_out.user ")
    Integer getUserNum();

    //统计新用户数量
  @Select("select count(id) from sky_take_out.user where create_time between #{beginTime} and #{endTime}")
     Integer getNewUserNum(LocalDateTime beginTime, LocalDateTime endTime);

  Integer countByMap(Map map);
}
