package com.training.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodoDTO {

    @NotBlank(message = "标题不能为空")
    @Size(min = 1, max = 200, message = "标题长度 1-200")
    private String title;

    @Min(value = 1, message = "优先级最小为1")
    @Max(value = 5, message = "优先级最大为5")
    private Integer priority;
}
