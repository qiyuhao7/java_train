package com.training.ide.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
public class Order {

    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal amount;

    /** 0-待支付 1-已支付 2-已取消 */
    private Integer status;

    private LocalDateTime createTime;
}
