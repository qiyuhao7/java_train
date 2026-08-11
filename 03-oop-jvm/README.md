# 第03期：面向对象 OOP 与 JVM 介绍

## 实战内容

### 测试类（推荐）：`src/test/java/com/training/oop/OopJvmTest.java`

10 个独立测试方法，覆盖：
1. 类初始化顺序（静态→实例→构造，父先子后）
2. 静态部分只执行一次
3. 多态（编译看左边，运行看右边）
4. 重载静态分派 vs 重写动态分派
5. 成员内部类 / 静态内部类 / 匿名内部类
6. 值传递（引用副本）
7. final 变量与引用
8. instanceof 与 null

### main 演示：`ShapeDemo.java`（图形系统）、`StrategyDemo.java`（策略模式）
设计模式演示：接口、抽象类、多态的实际应用

### JVM 观察
- `jmap -heap <PID>` 查看堆内存
- `jstat -gcutil <PID> 1000 10` 观察 GC
- `java -verbose:class` 查看类加载

## 运行方式

```bash
# 运行所有测试（推荐）
mvn test

# 运行单个测试方法
mvn test -Dtest=OopJvmTest#initializationOrder

# 运行 main 演示
cd src/main/java
javac com/training/oop/*.java
java com.training.oop.ShapeDemo
java com.training.oop.StrategyDemo
java -verbose:class com.training.oop.ShapeDemo 2>&1 | head -20
```
