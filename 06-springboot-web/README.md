# 第06期：Spring Boot 核心机制与 Web 接口

## 实战内容

开发一个待办事项（Todo）RESTful API：
- GET /api/todos — 查询列表（支持 status 过滤）
- GET /api/todos/{id} — 查询详情
- POST /api/todos — 创建
- PUT /api/todos/{id} — 更新
- DELETE /api/todos/{id} — 删除
- PATCH /api/todos/{id}/done — 标记完成

## 技术要点

- 构造器注入
- DTO/VO 分离
- @Valid 参数校验
- @ConfigurationProperties 配置绑定（AppProperties）
- HandlerInterceptor 日志拦截（LogInterceptor 记录请求耗时）
- CORS 跨域配置（允许 Vite 前端 5173 端口）
- MockMvc 接口测试（TodoControllerTest，10 个用例）

## 运行方式

```bash
# 启动应用
mvn spring-boot:run

# 测试接口
curl http://localhost:9099/api/todos
curl -X POST http://localhost:9099/api/todos -H "Content-Type: application/json" -d '{"title":"学习Spring Boot","priority":1}'

# 运行接口测试（MockMvc，10 个用例）
mvn test

# 运行单个测试
mvn test -Dtest=TodoControllerTest#shouldCreateTodo
```

## 工程结构

```
src/main/java/com/training/web/
├── TodoApplication.java      # 启动类
├── config/
│   ├── AppProperties.java    # @ConfigurationProperties 配置绑定
│   ├── LogInterceptor.java   # 日志拦截器（请求耗时）
│   └── WebConfig.java        # 注册拦截器 + CORS
├── controller/TodoController.java
├── dto/                      # CreateTodoDTO / UpdateTodoDTO
├── service/TodoService.java  # 内存实现（不存在时返回 404）
└── vo/TodoVO.java
src/test/java/.../TodoControllerTest.java  # MockMvc 接口测试
```
