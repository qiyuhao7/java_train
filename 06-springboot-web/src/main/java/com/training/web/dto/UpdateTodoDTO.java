package com.training.web.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class UpdateTodoDTO {

    @Size(min = 1, max = 200, message = "标题长度 1-200")
    private String title;

    @Min(value = 1, message = "优先级最小为1")
    @Max(value = 5, message = "优先级最大为5")
    private Integer priority;
}
