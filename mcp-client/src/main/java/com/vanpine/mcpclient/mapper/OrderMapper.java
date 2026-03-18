package com.vanpine.mcpclient.mapper;

import com.vanpine.mcpclient.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT id, user_id, amount, status, product_name, create_time, pay_time FROM `order` ORDER BY create_time DESC")
    List<Order> selectAll();

    @Select("SELECT id, user_id, amount, status, product_name, create_time, pay_time FROM `order` WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT id, user_id, amount, status, product_name, create_time, pay_time FROM `order` WHERE status = #{status} ORDER BY create_time DESC")
    List<Order> selectByStatus(@Param("status") String status);
}
