# 第14期：容器化部署

## 实战内容

1. 编写多阶段 Dockerfile
2. .dockerignore 配置
3. Docker Compose 编排（app + postgres + redis）
4. 容器健康检查
5. 优雅停机验证
6. 日志与监控

## 使用方式

```bash
# 构建并启动
docker compose up -d --build

# 查看状态
docker compose ps
docker compose logs -f app

# 验证
curl http://localhost:9099/api/health

# 优雅停机
docker compose stop app

# 清理
docker compose down -v
```

## 文件说明

- `Dockerfile`：多阶段构建
- `docker-compose.yml`：服务编排
- `.dockerignore`：构建排除
- `deploy/`：K8s 部署清单（第15期用）
- `sql/init.sql`：数据库初始化
