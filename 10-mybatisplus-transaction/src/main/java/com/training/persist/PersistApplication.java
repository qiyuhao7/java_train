package com.training.persist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatis-Plus 与事务演示应用
 */
@SpringBootApplication
@MapperScan("com.training.persist.mapper")
public class PersistApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersistApplication.class, args);
    }
}
