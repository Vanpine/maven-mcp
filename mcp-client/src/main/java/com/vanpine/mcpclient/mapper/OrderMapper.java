package com.vanpine.mcpclient.mapper;

import com.vanpine.mcpclient.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT id, user_id, amount, status, product_name, create_time, pay_time FROM `order` ORDER BY create_time DESC")
    List<Order> selectAll();

    @Select("SELECT id, user_id, amount, status, product_name, create_time, pay_time FROM `order` WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT id, user_id, amount, status, product_name, create_time, pay_time FROM `order` WHERE status = #{status} ORDER BY create_time DESC")
    List<Order> selectByStatus(@Param("status") String status);

    @Insert("INSERT INTO `order`(user_id, amount, status, product_name, create_time, pay_time) " +
            "VALUES(#{userId}, #{amount}, #{status}, #{productName}, NOW(), #{payTime})")
    int insert(Order order);

    @Update("UPDATE `order` SET status = #{status} WHERE id = #{id}")
    int updateStatusById(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE `order` SET amount = #{amount} WHERE id = #{id}")
    int updateAmountById(@Param("id") Long id, @Param("amount") java.math.BigDecimal amount);

    @Update("UPDATE `order` SET product_name = #{productName} WHERE id = #{id}")
    int updateProductNameById(@Param("id") Long id, @Param("productName") String productName);

    @Delete("DELETE FROM `order` WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
