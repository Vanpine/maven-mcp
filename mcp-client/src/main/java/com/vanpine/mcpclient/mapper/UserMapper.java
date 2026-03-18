package com.vanpine.mcpclient.mapper;

import com.vanpine.mcpclient.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT id, user_name, phone, create_time, update_time FROM user ORDER BY id")
    List<User> selectAll();
}
