package com.training.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户 VO（需要序列化存入 Redis）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private Long id;
    private String name;
    private String email;
}
