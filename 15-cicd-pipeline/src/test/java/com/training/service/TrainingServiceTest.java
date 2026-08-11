package com.training.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 流水线单元测试示例
 */
@DisplayName("流水线测试示例")
class TrainingServiceTest {

    @Test
    @DisplayName("示例测试：计算")
    void shouldCalculate() {
        assertEquals(4, 2 + 2);
    }

    @Test
    @DisplayName("示例测试：字符串")
    void shouldConcat() {
        assertEquals("training-service", "training-" + "service");
    }
}
