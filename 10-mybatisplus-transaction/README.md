# 第10期：MyBatis-Plus 持久层与 Spring 事务调优

## 实战内容

1. MyBatis-Plus 集成与基础 CRUD
2. LambdaQueryWrapper 条件构造
3. 分页插件
4. 自动填充（createTime/updateTime）
5. 乐观锁（@Version）
6. Spring 事务传播与失效场景验证

## 运行方式

```bash
# 启动（需要 PostgreSQL/GaussDB）
mvn spring-boot:run

# 运行测试
mvn test
```

## 项目结构

```
src/main/java/com/training/persist/
├── entity/        # 实体类
├── mapper/        # Mapper 接口
├── service/       # 业务服务（含事务）
└── config/        # MyBatis-Plus 配置
```
