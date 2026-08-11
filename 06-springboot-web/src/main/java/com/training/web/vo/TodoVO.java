package com.training.web.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoVO {
    private Long id;
    private String title;
    private Integer priority;
    private String status;       // PENDING / DONE
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
