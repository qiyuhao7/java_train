# 第05期：Maven 工程构建与单测实战

## 实战内容

1. Maven 项目结构搭建
2. 依赖管理与冲突排查
3. JUnit 5 单元测试
4. Mockito 模拟测试
5. 测试覆盖率

## 运行方式

```bash
# 编译
mvn clean compile

# 运行测试
mvn test

# 打包（跳过测试）
mvn package -DskipTests

# 查看依赖树
mvn dependency:tree
```
