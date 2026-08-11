package com.training.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 容器化部署示例应用
 */
@SpringBootApplication
public class ContainerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContainerApplication.class, args);
    }
}
