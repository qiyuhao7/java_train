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
- @ConfigurationProperties 配置绑定
- HandlerInterceptor 日志拦截

## 运行方式

```bash
mvn spring-boot:run
# 测试接口
curl http://localhost:9099/api/todos
curl -X POST http://localhost:9099/api/todos -H "Content-Type: application/json" -d '{"title":"学习Spring Boot","priority":1}'
```
