# 第15期：飞云 CI/CD 流水线与容器平台

## 实战内容

1. 流水线配置（.pipeline/pipeline.yml）
2. K8s Deployment 清单
3. Service + Ingress 配置
4. ConfigMap + Secret
5. 滚动更新与回滚
6. 发布后验证清单

## 项目结构

```
├── pom.xml                        # Maven 工程（流水线执行 mvn package 构建）
├── src/main/java/com/training/service/
│   ├── TrainingServiceApplication.java   # 启动类
│   └── controller/HealthController.java  # /api/health 健康检查
├── src/test/java/                 # 单元测试（流水线 junit 报告）
├── .pipeline/
│   └── pipeline.yml               # 飞云流水线配置
└── deploy/
    ├── test/
    │   └── deployment.yaml        # 测试环境部署清单
    └── prod/
        ├── deployment.yaml        # 生产环境部署清单
        ├── service.yaml           # Service
        ├── ingress.yaml           # Ingress
        └── configmap.yaml         # ConfigMap + Secret
```

## 本地验证

```bash
# 1. 构建
mvn clean package -DskipTests

# 2. 本地运行
java -jar target/training-service.jar
curl http://localhost:9099/api/health

# 3. 构建镜像（与流水线相同）
docker build -t registry.company.com/training-service:local .
docker run -p 9099:9099 registry.company.com/training-service:local
curl http://localhost:9099/api/health
```

## 常用 kubectl 命令

```bash
# 部署
kubectl apply -f deploy/prod/

# 查看状态
kubectl get pods -n training-prod
kubectl rollout status deploy/training-app -n training-prod

# 日志
kubectl logs -f deploy/training-app -n training-prod

# 回滚
kubectl rollout undo deploy/training-app -n training-prod

# 扩缩容
kubectl scale deploy/training-app --replicas=5 -n training-prod
```
