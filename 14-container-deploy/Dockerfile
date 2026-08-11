# ===== 多阶段构建 =====

# 阶段1：Maven 构建
FROM maven:3.9-eclipse-temurin-11 AS builder
WORKDIR /build

# 先复制 pom（利用 Docker 层缓存：依赖不变则不重新下载）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 再复制源码构建
COPY src ./src
RUN mvn package -DskipTests -B

# 阶段2：运行（精简 JRE 镜像）
FROM eclipse-temurin:11-jre-alpine
LABEL maintainer="training@company.com"
LABEL description="培训实战应用"

# 创建非 root 用户
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# 从构建阶段复制 jar
COPY --from=builder /build/target/*.jar app.jar

# 设置时区
RUN apk add --no-cache tzdata curl \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 切换用户
USER app

# 暴露端口
EXPOSE 9099

# JVM 参数（容器感知）
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/tmp/heapdump.hprof"

# 健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:9099/api/health || exit 1

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
