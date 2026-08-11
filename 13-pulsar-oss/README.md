# 第13期：Pulsar 消息中间件与对象存储

## 实战内容

1. Pulsar 生产者（同步/异步发送）
2. Pulsar 消费者（Shared 订阅 + 死信队列）
3. 消费幂等（Redis 去重）
4. MinIO 对象存储（上传/下载/预签名 URL）
5. 文件管理接口

## 运行方式

```bash
# 需要 Pulsar 和 MinIO 服务
mvn spring-boot:run

# 上传文件
curl -X POST http://localhost:9099/api/files/upload -F "file=@test.pdf"

# 下载文件
curl http://localhost:9099/api/files/download?objectName=uploads/xxx_test.pdf -o downloaded.pdf
```
