package com.training.auth.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息（存入 ThreadLocal）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String name;
    private List<String> roles;
}
