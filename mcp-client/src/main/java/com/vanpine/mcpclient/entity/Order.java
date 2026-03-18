package com.vanpine.mcpclient.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 订单表（ai_demo.order）POJO，供 MyBatis 映射 */
@Data
public class Order {

    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private String productName;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
}
