# 第07期：统一响应、全局异常与日志体系

## 实战内容

在第06期 Todo API 基础上添加：
1. 统一响应包装 `ApiResponse<T>`
2. 全局异常处理器 `@RestControllerAdvice`
3. 自定义业务异常体系
4. Logback 日志配置（按环境、按级别分文件）
5. MDC 链路追踪（traceId）

## 运行方式

```bash
mvn spring-boot:run

# 正常请求
curl http://localhost:9099/api/todos

# 触发 404
curl http://localhost:9099/api/todos/999

# 触发参数校验
curl -X POST http://localhost:9099/api/todos -H "Content-Type: application/json" -d '{"title":""}'

# 查看响应头中的 traceId
curl -v http://localhost:9099/api/todos 2>&1 | grep X-Trace-Id
```
