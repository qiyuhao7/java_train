# 第05期：Maven 工程构建与单测实战

## 实战内容

1. Maven 项目结构搭建
2. 依赖管理与冲突排查
3. 仓库体系：本地仓库、中央仓库、私服
4. settings.xml 配置（镜像、本地仓库、profile）
5. JUnit 5 单元测试
6. Mockito 模拟测试
7. 测试覆盖率（JaCoCo）

## 文件说明

| 文件 | 说明 |
|------|------|
| `pom.xml` | Maven 配置（JUnit 5、Mockito、JaCoCo 覆盖率） |
| `settings.xml.example` | settings.xml 示例（复制到 `~/.m2/settings.xml` 生效） |
| `src/main/java/.../AccountService.java` | 被测代码（转账服务） |
| `src/test/java/.../AccountServiceTest.java` | 单元测试（覆盖正常/异常/边界场景） |

## 运行方式

```bash
# 编译
mvn clean compile

# 运行测试
mvn test

# 打包（跳过测试）
mvn package -DskipTests

# 查看依赖树
mvn dependency:tree

# 查看特定依赖引入路径
mvn dependency:tree -Dincludes=org.junit

# 查看测试覆盖率（生成 target/site/jacoco/index.html）
mvn test
open target/site/jacoco/index.html    # macOS
xdg-open target/site/jacoco/index.html # Linux
```

## 仓库与镜像练习

```bash
# 1. 配置阿里云镜像
cp settings.xml.example ~/.m2/settings.xml

# 2. 验证镜像生效
mvn help:effective-settings | grep -A 5 "mirror"

# 3. 查看本地仓库位置
mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout

# 4. 查看本地仓库中的 JUnit
ls ~/.m2/repository/org/junit/jupiter/

# 5. 清理下载失败残留
find ~/.m2/repository -name "*.lastUpdated" -delete

# 6. 强制更新 SNAPSHOT 依赖
mvn clean install -U
```
