package com.training.basics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 第02期：Java 基础语法练习
 * 每个 @Test 方法对应一个知识点，可单独运行验证
 */
@DisplayName("Java 基础语法")
class JavaBasicsTest {

    // ===== 数据类型与类型转换 =====

    @Test
    @DisplayName("byte 运算提升为 int")
    void byteArithmeticPromotesToInt() {
        byte b1 = 1, b2 = 2;
        // byte b3 = b1 + b2;  // ❌ 编译错误：byte + byte → int
        byte b3 = (byte) (b1 + b2);  // ✅ 强制转换
        assertEquals(3, b3);

        // 复合赋值隐含强转
        byte b = 10;
        b += 5;  // ✅ 等价于 b = (byte)(b + 5)
        assertEquals(15, b);
    }

    @Test
    @DisplayName("双 final 变量运算不提升")
    void finalVariablesNoPromotion() {
        final byte b4 = 4, b5 = 6;
        byte result = b4 + b5;  // ✅ 双 final，编译期确定值
        assertEquals(10, result);

        byte b1 = 1;
        final byte b2 = 2;
        // byte r = b1 + b2;  // ❌ 单 final 仍然提升为 int
    }

    @Test
    @DisplayName("float 赋值规则")
    void floatAssignment() {
        float a = 1;      // ✅ int 自动提升为 float
        float b = 1.0f;   // ✅ 必须加 f
        // float c = 1.0; // ❌ 1.0 是 double，不能隐式窄化
        assertEquals(1.0f, a);
        assertEquals(1.0f, b);
    }

    @Test
    @DisplayName("强制转换截断小数")
    void castTruncates() {
        double d = 3.99;
        int i = (int) d;
        assertEquals(3, i);  // 截断，不是四舍五入

        assertEquals(12, Math.round(11.5));   // 四舍五入
        assertEquals(-11, Math.round(-11.5)); // 向大数方向
    }

    // ===== Integer 缓存 =====

    @Test
    @DisplayName("Integer 缓存 -128~127")
    void integerCache() {
        Integer a = 127, b = 127;
        assertSame(a, b);  // 缓存范围内，同一对象

        Integer c = 128, d = 128;
        assertNotSame(c, d);  // 超出缓存，不同对象
        assertEquals(c, d);   // equals 比较值，仍然相等

        Integer e = new Integer(127);
        Integer f = new Integer(127);
        assertNotSame(e, f);  // new 出来的永远不同
    }

    @Test
    @DisplayName("自动拆箱 NPE")
    void autoUnboxingNpe() {
        Integer nullInt = null;
        assertThrows(NullPointerException.class, () -> {
            int x = nullInt;  // 拆箱时调用 nullInt.intValue() → NPE
        });
    }

    // ===== String 常量池 =====

    @Test
    @DisplayName("String 常量池与 == 比较")
    void stringConstantPool() {
        String s1 = "abc";
        String s2 = "abc";
        assertSame(s1, s2);  // 常量池复用

        String s3 = new String("abc");
        assertNotSame(s1, s3);  // 堆上新对象
        assertEquals(s1, s3);   // 内容相同

        // 编译期常量折叠
        String str1 = "a" + "b" + "c";  // 编译期变成 "abc"
        assertSame(s1, str1);

        // 运行时拼接 → 新对象
        String a = "a";
        String b = "b";
        String str2 = a + b;  // 运行时 StringBuilder
        assertNotSame(s1, str2);

        // intern() 返回常量池引用
        assertSame(s1, str2.intern());
    }

    @Test
    @DisplayName("Java 11 String 新方法")
    void java11StringMethods() {
        assertEquals("hello", "  hello  ".strip());
        assertEquals("hello  ", "  hello  ".stripLeading());
        assertEquals("  hello", "  hello  ".stripTrailing());
        assertTrue("   ".isBlank());
        assertFalse(" ".isEmpty());
        assertEquals("hahaha", "ha".repeat(3));
        assertEquals(2, "line1\nline2".lines().count());
    }

    // ===== 运算符 =====

    @Test
    @DisplayName("整数除法与取余")
    void integerDivisionAndModulo() {
        assertEquals(5, 5 + 1 / 4);      // 1/4 = 0
        assertEquals(3, 7 / 2);           // 截断
        assertEquals(3.5, 7.0 / 2, 0.001);

        // 取余符号跟被除数（左边）
        assertEquals(-2, -12 % 5);
        assertEquals(2, 12 % -5);
        assertEquals(-2, -12 % -5);
    }

    @Test
    @DisplayName("i++ 陷阱")
    void incrementTrap() {
        int i = 0;
        i = i++ + i;
        // i++ 返回 0，i 变为 1；然后 + i（此时 1）→ i = 0 + 1 = 1
        assertEquals(1, i);

        // count = count++ 不改变值
        int count = 5;
        count = count++;
        assertEquals(5, count);  // 值不变！
    }

    @Test
    @DisplayName("三元运算符类型提升")
    void ternaryTypePromotion() {
        // 两个分支都是数字类型时，会统一提升
        Object result = true ? Integer.valueOf(1) : Double.valueOf(2.0);
        assertTrue(result instanceof Double);  // Integer 被提升为 Double
        assertEquals(1.0, result);
    }

    @Test
    @DisplayName("字符串拼接顺序")
    void stringConcatenationOrder() {
        assertEquals("结果: 12", "结果: " + 1 + 2);   // 从左到右拼接
        assertEquals("3 结果", 1 + 2 + " 结果");      // 先算加法
    }

    // ===== 控制流 =====

    @Test
    @DisplayName("九九乘法表")
    void multiplicationTable() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                sb.append(String.format("%d×%d=%-4d", j, i, i * j));
            }
            sb.append("\n");
        }
        String table = sb.toString();
        assertTrue(table.contains("1×1=1"));
        assertTrue(table.contains("9×9=81"));
        assertEquals(9, table.strip().lines().count());
    }

    @Test
    @DisplayName("switch 多 case 共享与穿透")
    void switchFallThrough() {
        // 传统 switch 不 break 会穿透
        int day = 3;
        String type;
        switch (day) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                type = "工作日";
                break;
            default:
                type = "周末";
        }
        assertEquals("工作日", type);
    }

    // ===== 数组 =====

    @Test
    @DisplayName("数组默认值与操作")
    void arrayDefaults() {
        int[] ints = new int[3];
        assertEquals(0, ints[0]);  // 默认 0

        boolean[] bools = new boolean[2];
        assertFalse(bools[0]);  // 默认 false

        String[] strs = new String[2];
        assertNull(strs[0]);  // 默认 null

        // 锯齿数组
        int[][] jagged = new int[3][];
        jagged[0] = new int[2];
        jagged[1] = new int[5];
        assertEquals(2, jagged[0].length);
        assertEquals(5, jagged[1].length);
    }

    // ===== Java 11 特性 =====

    @Test
    @DisplayName("var 类型推断")
    void varTypeInference() {
        var list = List.of("a", "b", "c");
        var map = Map.of("key", "value");
        var number = 42;

        assertEquals(3, list.size());
        assertEquals("value", map.get("key"));
        assertEquals(42, number);
    }

    @Test
    @DisplayName("集合工厂不可变")
    void immutableCollections() {
        var list = List.of("a", "b");
        assertThrows(UnsupportedOperationException.class, () -> list.add("c"));

        // 需要可变集合
        var mutable = new java.util.ArrayList<>(list);
        mutable.add("c");
        assertEquals(3, mutable.size());
    }

    @Test
    @DisplayName("Stream takeWhile / dropWhile")
    void streamTakeDropWhile() {
        var data = List.of(1, 2, 3, 4, 1, 2);

        var taken = data.stream().takeWhile(n -> n < 4).collect(Collectors.toList());
        assertEquals(List.of(1, 2, 3), taken);

        var dropped = data.stream().dropWhile(n -> n < 3).collect(Collectors.toList());
        assertEquals(List.of(3, 4, 1, 2), dropped);
    }

    @Test
    @DisplayName("Optional ifPresentOrElse / or / stream")
    void optionalEnhancements() {
        Optional<String> empty = Optional.empty();
        Optional<String> present = Optional.of("hello");

        // or()
        var result = empty.or(() -> Optional.of("默认"));
        assertEquals("默认", result.get());

        // stream()
        var list = Stream.of(present, empty, Optional.of("world"))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
        assertEquals(List.of("hello", "world"), list);
    }

    @Test
    @DisplayName("Stream 综合：过滤+分组+排序")
    void streamComprehensive() {
        var users = List.of("Alice:28", "Bob:35", "Charlie:22", "David:31");

        var result = users.stream()
            .map(s -> s.split(":"))
            .filter(arr -> Integer.parseInt(arr[1]) > 25)
            .map(arr -> arr[0])
            .sorted()
            .collect(Collectors.joining(", "));

        assertEquals("Alice, Bob, David", result);
    }
}
