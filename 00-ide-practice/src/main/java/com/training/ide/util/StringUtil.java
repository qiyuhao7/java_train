package com.training.ide.util;

import java.util.ArrayList;      // ⚠️ 练习点：这个 import 没用到，用 Alt+Enter 或 Ctrl+Alt+O 移除
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具类
 * 练习点：
 * - 未使用的 import（第4行 ArrayList）→ Alt+Enter 修复
 * - 方法返回值类型 → 用 Ctrl+B / Ctrl+Alt+B 跳转
 * - 方法名拼写 → Shift+F6 重命名（或 Alt+Enter 建议）
 */
public class StringUtil {

    /**
     * 拼接列表
     */
    public static String join(List<String> items, String separator) {
        return items.stream().collect(Collectors.joining(separator));
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * 计算折扣金额（练习提取常量/变量）
     */
    public static double discount(double price) {
        // 练习点：0.95 是魔法数字 → Ctrl+Alt+C 提取常量
        // 练习点：整个表达式 → Ctrl+Alt+V 提取变量
        double result = price * 0.95;
        return Math.round(result * 100) / 100.0;
    }
}
