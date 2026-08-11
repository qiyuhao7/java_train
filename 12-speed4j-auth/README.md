# 第12期：Speed4j 框架与统一认证对接

## 实战内容

1. JWT Token 生成与验证
2. 认证过滤器实现
3. 用户上下文（ThreadLocal）
4. 基于注解的权限控制（@RequiresPermission）
5. SSO 回调接口模拟
6. Token 刷新与登出

## 运行方式

```bash
mvn spring-boot:run

# 模拟登录获取 Token
curl -X POST http://localhost:9099/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"123456"}'

# 带 Token 访问
curl http://localhost:9099/api/profile -H "Authorization: Bearer <token>"

# 无 Token 访问（401）
curl http://localhost:9099/api/profile
```
