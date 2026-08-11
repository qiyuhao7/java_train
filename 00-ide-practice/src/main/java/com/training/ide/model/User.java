package com.training.ide.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体
 * 练习：Alt+Insert 生成代码、Alt+Enter 修复、跳转
 */
@Data
public class User {

    private Long id;

    private String username;

    private String email;

    private Integer age;

    /** 账户余额 */
    private BigDecimal balance;

    private Boolean vip;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
