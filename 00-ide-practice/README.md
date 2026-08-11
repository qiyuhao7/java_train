# 第00期：IDEA 快捷键实践手册

> 配套工程：`00-ide-practice`（Spring Boot 应用）
> 目的：每个快捷键都有具体的练习目标，照着做一遍就能记住。
> 建议：先跑通一遍 → 第二天不看手册再练一遍 → 形成肌肉记忆。

## 工程概览

```
00-ide-practice/
├── pom.xml                          # Maven 配置（含 DevTools 热部署）
├── scripts/api-test.sh              # 接口测试脚本（curl）
├── src/main/java/com/training/ide/
│   ├── IdePracticeApplication.java # 启动类（运行/调试入口）
│   ├── controller/
│   │   ├── UserController.java      # 用户接口
│   │   └── OrderController.java     # 订单接口
│   ├── service/
│   │   ├── UserService.java         # ⭐ 练习点最多的类
│   │   └── OrderService.java        # 断点调试练习
│   ├── model/
│   │   ├── User.java                # 用户实体
│   │   └── Order.java               # 订单实体
│   └── util/
│       └── StringUtil.java          # 含未使用 import 的练习
└── src/test/java/.../IdePracticeTest.java   # 单元测试
```

---

## 第一步：导入工程并启动

### 1. 导入工程
1. IDEA → **File → Open** → 选择 `/home/qyh/java_train/00-ide-practice` 目录
2. 选中 `pom.xml` 右键，选择 Add as Maven Project

### 2. 启动应用
1. 打开 `IdePracticeApplication.java`
2. 点击 `main` 方法左侧的**绿色三角** → **Run**
3. 或右键 → **Run 'IdePracticeApplication'**
4. 看到 `Started IdePracticeApplication` 即启动成功

### 3. 验证接口
1. 浏览器访问 `http://localhost:9099/api/users` → 看到用户列表 JSON
2. 运行测试脚本（练习 curl 接口测试）：
   ```bash
   bash scripts/api-test.sh
   ```
3. 也可以安装 Postman / Apifox 等 GUI 工具代替 curl（按个人习惯）

---

## 第二步：高频快捷键练习

> 按顺序做，每个都对照说明理解"为什么这么用"。

### 1. Alt+Enter（智能修复）— 万能键

**练习目标：** 移除未使用的 import。

1. 打开 `util/StringUtil.java`
2. 看第 4 行 `import java.util.ArrayList;` — 有灰色下划线（未使用）
3. 光标放在 `ArrayList` 上 → **Alt+Enter** → 选择 **Remove unused import**
4. ✅ 效果：import 被删除

> Alt+Enter 在报错时的用法：任何红/黄波浪线都可以用它，比如自动创建方法、修拼写、改返回值等。

### 2. Ctrl+D / Ctrl+Y（复制行 / 删除行）

**练习目标：** 快速复制、删除代码。

1. 打开 `UserService.java` 的 `listAll()` 方法
2. 光标停在 `return new ArrayList<>(userStore.values());` 行 → **Ctrl+D** → 复制出一行（此时会编译报错，因为有两个 return）
3. 光标停在多出的那行 → **Ctrl+Y** → 删掉
4. ✅ 效果：代码恢复原样

### 3. Ctrl+Shift+↑ / ↓（移动行）

**练习目标：** 调整代码顺序。

1. 打开 `UserService.java` 的 `create()` 方法
2. 光标停在 `user.setId(idGen.incrementAndGet());` 行
3. **Ctrl+Shift+↓** → 该行下移，**Ctrl+Shift+↑** → 上移
4. 移回原位（先 setBalance 再 setId 也行，不影响逻辑）

### 4. Ctrl+Alt+L（格式化代码）

**练习目标：** 一键排版。

1. 打开 `OrderService.java`
2. 故意把某行缩进打乱（删掉几个空格）
3. 全选代码 **Ctrl+A** → **Ctrl+Alt+L**
4. ✅ 效果：代码恢复规范缩进

### 5. Ctrl+/ 和 Ctrl+Shift+/（注释）

**练习目标：** 行注释、块注释。

1. 打开 `User.java`，选中 `private Integer age;` 行 → **Ctrl+/** → 变成注释
2. 再按一次 **Ctrl+/** → 取消注释
3. 选中 `createTime` 和 `updateTime` 两行 → **Ctrl+Shift+/** → 块注释
4. 再按一次取消

---

## 第三步：导航跳转练习

### 6. Ctrl+N（查找类）

**练习目标：** 快速打开任意类。

1. **Ctrl+N**
2. 输入 `Order` → 出现 Order 和 OrderService、OrderController 列表
3. 回车打开 `OrderService.java`

### 7. Ctrl+B（跳转到定义）

**练习目标：** 方法/字段/类跳转。

1. 在 `UserController.java` 中，光标放在 `userService.listAll()` 的 `listAll` 上
2. **Ctrl+B** → 跳到 `UserService.listAll()` 方法定义
3. 再在 `UserService` 方法里按住 **Ctrl 键**点击 `userStore` 字段 → 跳到字段声明
4. 在 `private final Map<Long, User> userStore` 的 `User` 上 **Ctrl+B** → 跳到 User 类

### 8. Ctrl+Alt+B（跳转到实现）

**练习目标：** 接口 → 实现类。

1. 打开 `OrderService.java`，光标放在类名 `OrderService` 上
2. **Ctrl+Alt+B** → 显示谁实现了它（这里没有接口，会提示无实现）
3. 回到 `UserController.java`，光标放 `UserService userService` 上 → **Ctrl+Alt+B** → 跳到实现

### 9. Alt+F7（查找引用）

**练习目标：** 找出所有调用处。

1. 在 `UserService.java` 的 `calculatePayAmount` 方法名上
2. **Alt+F7** → 底部 Find 窗口列出所有调用位置
3. ✅ 效果：看到只有自己内部调用（或没有调用），理解"查找谁用了我"

### 10. Ctrl+F12（文件结构）

**练习目标：** 快速浏览类的方法列表。

1. 打开 `UserService.java`
2. **Ctrl+F12** → 弹出方法列表
3. 输入 `ca` 过滤 → 直接跳转到 `calculatePayAmount`

### 11. Ctrl+Alt+← / →（前进后退）

**练习目标：** 在跳转历史中往返。

1. 按第 7 步跳到 `UserService` 后
2. **Ctrl+Alt+←** → 回到 `UserController`
3. **Ctrl+Alt+→** → 回到 `UserService`

### 12. Ctrl+Shift+Backspace（回到上次编辑位置）

**练习目标：** 多文件编辑时快速返回。

1. 编辑 `UserController.java` 后切到 `OrderController.java` 编辑
2. **Ctrl+Shift+Backspace** → 跳回上次编辑的位置

### 13. 双击 Shift（全局搜索）

**练习目标：** 搜任何东西。

1. 双击 **Shift**
2. 输入 `calculatePayAmount` → 看到类/文件/操作三个 Tab 的结果
3. 回车跳转

---

## 第四步：代码生成练习

### 14. Alt+Insert（生成代码）

**练习目标：** 生成 Getter/Setter、构造器、toString。

1. 新建一个类练习：右键 `model` 包 → **New → Java Class** → 命名 `Product`
2. 输入字段：`private Long id;` `private String name;` `private BigDecimal price;`
3. 光标放在类体内 → **Alt+Insert**
4. 选择 **Getter and Setter** → 全选 → OK → 自动生成 getter/setter
5. 再 **Alt+Insert** → 选 **toString()** → 生成
6. ✅ 效果：不用手写模板代码

### 15. Live Templates（代码模板）

**练习目标：** psvm / sout / fori / iter。

| 输入 | Tab | 生成 |
|------|-----|------|
| `psvm` | Tab | `public static void main(String[] args) {}` |
| `sout` | Tab | `System.out.println();` |
| `fori` | Tab | `for (int i = 0; i < ; i++) {}` |
| `iter` | Tab | `for (xxx : xxx) {}` 增强 for |
| `try` | Tab | try-catch 块 |

**逐个练习：**
1. 打开 `UserService.batchCreate()` 方法
2. 删除里面的 for 循环（先记下逻辑）
3. 输入 `fori` + **Tab** → IDEA 生成标准 for 循环 → 手动填条件
4. 输入 `iter` + **Tab** → 生成 foreach → 删除多余的
5. 在 `UserService.create()` 方法首行输入 `sout` + Tab → 生成 println

---

## 第五步：重构练习（重点）

### 16. Shift+F6（重命名）

**练习目标：** 安全全局重命名。

1. 打开 `util/StringUtil.java`
2. 光标放在方法名 `capitalize` 上 → **Shift+F6**
3. 改成 `firstLetterUpper` → 回车
4. ✅ 效果：所有引用处全部更新（IDEA 自动同步）
5. 再看 `discount` 方法里的参数 `price` → **Shift+F6** 改为 `originalPrice`

### 17. Ctrl+Alt+C（提取常量）

**练习目标：** 消灭魔法数字。

1. 打开 `UserService.calculatePayAmount()` 方法
2. 选中 `new BigDecimal("0.85")` → **Ctrl+Alt+C**
3. 命名 `VIP_DISCOUNT` → 回车
4. 同样把 `new BigDecimal("0.90")` 提取为 `NORMAL_DISCOUNT`
5. ✅ 效果：魔法数字变成类常量，语义清晰

### 18. Ctrl+Alt+M（提取方法）

**练习目标：** 消除重复代码。

1. 打开 `UserService.create()` 方法
2. 选中两段校验代码（用户名非空判断 + 邮箱非空判断）→ **Ctrl+Alt+M**
3. 方法名填 `checkUser` → 回车
4. ✅ 效果：重复代码抽成私有方法，create() 变干净

### 19. Ctrl+Alt+V（提取变量）

**练习目标：** 拆分长表达式。

1. 打开 `UserService.calculatePayAmount()` 方法
2. 选中 `.setScale(2, RoundingMode.HALF_UP)` 前面的整个表达式
3. **Ctrl+Alt+V** → 变量名填 `payAmount`（如果之前提取过常量，先还原）
4. ✅ 效果：表达式拆成变量，可读性提升

### 20. Ctrl+Alt+O（优化 import）

**练习目标：** 清理无用导入。

1. 在 `OrderService.java` 中 **Ctrl+Alt+O**
2. ✅ 效果：自动移除未使用 import、合并重复 import

---

## 第六步：调试练习（重点）

### 21. 普通断点 + 单步执行

1. 在 `OrderService.createOrder()` 方法里，`order.setStatus(0);` 行左侧点击 → 打断点
2. **Shift+F9**（Debug 运行）
3. 另开一个终端，发送"下单"请求触发断点：
   ```bash
   # 方式1：跑脚本（推荐，会发多个请求）
   bash scripts/api-test.sh
   # 方式2：单独一条命令
   curl -X POST http://localhost:9099/api/orders \
     -H "Content-Type: application/json" \
     -d '{"userId":1,"amount":999.00}'
   ```
4. 程序在断点暂停：
   - **F8**（Step Over）→ 逐行执行，观察 Variables 面板 `order` 对象变化
   - **F7**（Step Into）→ 进入 `userService.getById()` 内部
   - **Shift+F8**（Step Out）→ 跳出当前方法
   - **F9**（Resume）→ 继续到下一个断点
5. **Ctrl+F2** → 停止

### 22. 条件断点

**练习目标：** 满足条件才暂停。

1. 在 `OrderService.pay()` 的 `order.setStatus(1);` 行打断点
2. **右键断点** → 在 Condition 填 `order.getAmount() != null && order.getAmount().doubleValue() > 500`
3. Debug 运行 → 分别发送金额 100 和 999 的支付请求（用下面的命令，观察哪个会暂停）：
   ```bash
   # 先下单两个订单
   curl -X POST http://localhost:9099/api/orders -H "Content-Type: application/json" -d '{"userId":1,"amount":100.00}'
   curl -X POST http://localhost:9099/api/orders -H "Content-Type: application/json" -d '{"userId":1,"amount":999.00}'
   # 再分别支付（记住返回的订单 id）
   curl -X POST http://localhost:9099/api/orders/1/pay
   curl -X POST http://localhost:9099/api/orders/2/pay
   ```
4. ✅ 效果：只有金额 > 500 的请求会暂停

### 23. Alt+F8（求值表达式）

**练习目标：** 断点时计算任意表达式。

1. 暂停在断点处（参考第 21 步）
2. **Alt+F8** → 输入 `order.getAmount().multiply(new java.math.BigDecimal("2"))` → Evaluate
3. ✅ 效果：不写代码直接看计算结果

### 24. 异常断点

**练习目标：** 抛异常时自动暂停。

1. **Run → View Breakpoints**（或 Ctrl+Shift+F8）
2. **+** → **Java Exception Breakpoints**
3. 输入 `IllegalArgumentException` → 确定
4. 发送一个不存在的用户的订单请求（触发异常）：
   ```bash
   curl -X POST http://localhost:9099/api/orders \
     -H "Content-Type: application/json" \
     -d '{"userId":999,"amount":50.00}'
   ```
5. ✅ 效果：抛异常瞬间暂停，Frames 面板看调用栈

---

## 第七步：运行测试与热部署

### 25. 运行单个测试

1. 打开 `IdePracticeTest.java`
2. 点击某个 `@Test` 方法左侧**绿色箭头** → **Run**（只跑这一个）
3. 或光标在方法内 → **Ctrl+Shift+F10**
4. ✅ 效果：绿色 = 通过，红色 = 失败（可故意改断言体验失败）

### 26. 热部署（DevTools）

1. 确认 pom.xml 已含 `spring-boot-devtools`
2. 设置：**Settings → Build, Execution, Deployment → Compiler → 勾选 Build project automatically**
3. 运行应用 → 修改 `UserService.listAll()` 返回内容 → **Ctrl+F9**（仅编译当前改动）
4. ✅ 效果：应用自动重启（看 Console 日志），不用手动重启

### 27. 方法级运行配置

1. 工具栏下拉框 → **Edit Configurations**
2. 添加 **Application** 类型配置
3. VM options 填：`-Dserver.port=9098`（注意：默认配置才是 9099，这里改成 9098 验证生效）
4. 运行 → 应用在 9098 端口启动（验证配置生效）

---

## 第八步：Git 集成练习

> 配合第01期 IDEA 操作手册使用。

### 28. 提交与推送
1. **Ctrl+K** → 打开 Commit 面板
2. 左侧勾选文件，中间写提交信息（如 `chore: 快捷键练习`）
3. 点击 **Commit** 或 **Commit and Push**
4. **Ctrl+Shift+K** → 推送

### 29. 查看历史
1. **Alt+9** 打开 Git 工具窗口 → **Log** 标签页
2. 点击某次提交 → 右侧看变更内容
3. 在编辑器中右键某行 → **Git → Annotate** → 看每行谁写的

### 30. 回滚
1. 修改 `UserService.java` 加一行垃圾代码（不提交）
2. 右键文件 → **Git → Revert** → 选择 **Local Changes** → 还原
3. 若已提交：Log 中右键该提交 → **Revert Commit**

---

## 第九步：查看接口映射与依赖（社区版替代方案）

> Spring 工具窗口是 Ultimate 专属功能，社区版没有。
> 用以下方式达到同样目的：

### 31. 查看所有接口映射

1. **Ctrl+Shift+F** → 全局搜索 `@RequestMapping`
2. ✅ 效果：一次看到所有 Controller 的路由前缀
3. 再搜 `@GetMapping` / `@PostMapping` 查看每个方法的具体映射
4. 需要完整接口清单时，也可以用 curl 访问测试：
   ```bash
   curl -s http://localhost:9099/api/users
   ```

### 32. 查看 Bean 依赖关系

1. **Ctrl+N** → 输入 `UserService` 打开类
2. 看构造方法参数 → 就知道它依赖谁（这里是 `Map/AtomicLong`）
3. **Ctrl+Alt+B**（跳转实现）在 `OrderService` 的 `UserService` 上 → 查看谁被注入
4. 想快速看一个类所有依赖：**Ctrl+F12** 看字段列表，或全局搜索 `new OrderService(` 查注入点

---

## 快捷键总表（本手册覆盖）

| 类别 | 快捷键 | 手册位置 |
|------|--------|----------|
| 智能修复 | Alt+Enter | 第1步 |
| 复制/删除行 | Ctrl+D / Ctrl+Y | 第2步 |
| 移动行 | Ctrl+Shift+↑/↓ | 第3步 |
| 格式化 | Ctrl+Alt+L | 第4步 |
| 注释 | Ctrl+/ 、Ctrl+Shift+/ | 第5步 |
| 查找类 | Ctrl+N | 第6步 |
| 跳转定义 | Ctrl+B | 第7步 |
| 跳转实现 | Ctrl+Alt+B | 第8步 |
| 查找引用 | Alt+F7 | 第9步 |
| 文件结构 | Ctrl+F12 | 第10步 |
| 前进/后退 | Ctrl+Alt+←/→ | 第11步 |
| 上次编辑位置 | Ctrl+Shift+Backspace | 第12步 |
| 全局搜索 | 双击 Shift | 第13步 |
| 生成代码 | Alt+Insert | 第14步 |
| 代码模板 | psvm/sout/fori/iter + Tab | 第15步 |
| 重命名 | Shift+F6 | 第16步 |
| 提取常量 | Ctrl+Alt+C | 第17步 |
| 提取方法 | Ctrl+Alt+M | 第18步 |
| 提取变量 | Ctrl+Alt+V | 第19步 |
| 优化导入 | Ctrl+Alt+O | 第20步 |
| 调试运行 | Shift+F9 | 第21步 |
| 单步跳过/进入/跳出 | F8 / F7 / Shift+F8 | 第21步 |
| 继续/停止 | F9 / Ctrl+F2 | 第21步 |
| 求值表达式 | Alt+F8 | 第23步 |
| 断点管理 | Ctrl+Shift+F8 | 第24步 |
| 运行测试 | Ctrl+Shift+F10 | 第25步 |
| 热部署编译 | Ctrl+F9 | 第26步 |
| Git 提交/推送 | Ctrl+K / Ctrl+Shift+K | 第28步 |

---

## 练习顺序建议

```
第一次（30分钟）：第1-15步（高频 + 导航 + 生成）
第二次（30分钟）：第16-24步（重构 + 调试）⭐ 最重要
第三次（20分钟）：第25-32步（测试 + 热部署 + Git + 接口排查）
```

## 参考资料

- [[第00期-IDE开发环境与工具使用]] — 培训笔记（含 VSCode 部分）
- [IDEA 官方快捷键手册](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf)
