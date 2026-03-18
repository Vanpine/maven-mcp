package com.vanpine.mcpclient.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 用户表（ai_demo.user）POJO，供 MyBatis 映射 */
@Data
public class User {

    private Long id;
    private String userName;
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
