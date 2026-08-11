# 第04期：集合框架、Stream 流与并发基础

## 实战内容

1. 集合选型对比（ArrayList vs LinkedList, HashMap vs TreeMap）
2. HashMap / ConcurrentHashMap 行为验证
3. Stream 数据处理（分组、聚合、flatmap）
4. 遍历删除三种方式
5. 线程池使用与参数调优
6. volatile 非原子性验证
7. CompletableFuture 异步编排
8. CountDownLatch / Semaphore 并发工具

## 运行方式

```bash
# 运行所有练习（JUnit 测试类）
mvn test

# 运行单个测试方法
mvn test -Dtest=CollectionsConcurrentTest#hashMapNullSupport

# 单独运行 main 演示（线程池/并发工具）
cd src/main/java
javac com/training/collections/ConcurrentPractice.java
java com.training.collections.ConcurrentPractice
```
