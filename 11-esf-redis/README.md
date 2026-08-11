# 第11期：ESF 微服务治理与 Redis 高性能缓存

## 实战内容

1. Redis 缓存集成（RedisTemplate）
2. 缓存优先查询模式（Cache Aside）
3. 缓存空值防穿透
4. 互斥锁防击穿
5. 随机 TTL 防雪崩
6. 分布式锁（SET NX EX + Lua 释放）
7. 滑动窗口限流器

## 运行方式

```bash
# 需要 Redis 服务
mvn spring-boot:run

# 测试缓存
curl http://localhost:9099/api/users/1
curl http://localhost:9099/api/users/1  # 第二次走缓存
```
