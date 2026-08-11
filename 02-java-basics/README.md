# 第02期：Java 基础语法与 11 现代特性

## 实战内容

使用 Java 11 特性完成以下练习：

### 测试类（推荐）：`src/test/java/com/training/basics/JavaBasicsTest.java`

18 个独立测试方法，覆盖：
1. 数据类型与类型转换（byte 提升、final 特殊规则、float 赋值、强转截断）
2. Integer 缓存（-128~127）与自动拆箱 NPE
3. String 常量池（== 与 equals、编译期折叠、intern）
4. Java 11 String 新方法（strip, repeat, isBlank）
5. 运算符（整数除法、取余符号、i++ 陷阱、三元提升）
6. 控制流（九九乘法表、switch 穿透）
7. 数组默认值与锯齿数组
8. Java 11 特性（var、集合工厂、takeWhile/dropWhile、Optional 增强）

### main 演示：`Java11Features.java`
完整的 Java 11 特性演示（字符串、集合、Stream、Optional、文件读写、HTTP Client）

## 运行方式

```bash
# 运行所有测试（推荐）
mvn test

# 运行单个测试方法
mvn test -Dtest=JavaBasicsTest#integerCache

# 运行 main 演示
cd src/main/java
javac com/training/basics/Java11Features.java
java com.training.basics.Java11Features
```
