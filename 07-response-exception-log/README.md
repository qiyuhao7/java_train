# 第07期：统一响应、全局异常与日志体系

## 实战内容

1. 统一响应包装 `ApiResponse<T>`
2. 全局异常处理器 `@RestControllerAdvice`（业务/校验/方法不支持/兜底）
3. 自定义业务异常体系（BusinessException / NotFoundException）
4. Logback 日志配置（按环境、按级别分文件、异步）
5. MDC 链路追踪（traceId）
6. MockMvc 测试统一响应与异常处理（ApiResponseTest，6 个用例）

## 运行方式

```bash
# 启动应用
mvn spring-boot:run

# 正常请求（成功响应）
curl http://localhost:9099/api/demo/success

# 业务异常（HTTP 200，body 带错误码 20001）
curl http://localhost:9099/api/demo/biz-error

# 资源不存在（code=10005）
curl http://localhost:9099/api/demo/not-found

# 参数校验失败（HTTP 400，code=10002）
curl "http://localhost:9099/api/demo/validate?name=&age=0"

# 未知异常（HTTP 500，code=10001，通用提示）
curl http://localhost:9099/api/demo/unknown

# 查看响应头中的 traceId
curl -v http://localhost:9099/api/demo/success 2>&1 | grep X-Trace-Id

# 运行测试
mvn test
```

## 日志验证

```bash
# 运行后查看日志目录
ls logs/
# training-app.log          # 全量日志
# training-app-error.log    # 仅 ERROR 级别

# 按 traceId 搜索一次请求的完整链路
grep "traceId值" logs/training-app.log

# 实时跟踪错误
tail -f logs/training-app-error.log
```

## 工程结构

```
src/main/java/com/training/common/
├── CommonApplication.java              # 启动类
├── DemoController.java                 # 触发各类响应的演示接口
├── config/
│   ├── TraceInterceptor.java           # MDC traceId 拦截器
│   └── WebConfig.java                  # 注册拦截器
├── exception/
│   ├── BusinessException.java          # 业务异常基类
│   ├── NotFoundException.java          # 资源不存在
│   └── GlobalExceptionHandler.java     # 全局异常处理器
└── response/
    ├── ApiResponse.java                # 统一响应
    └── ResultCode.java                 # 业务状态码
src/main/resources/
├── application.yml                     # 开启 404 交给异常处理器
└── logback-spring.xml                  # 日志配置
src/test/java/.../ApiResponseTest.java  # 响应与异常测试
```
