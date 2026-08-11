# 第01期：使用 IDEA 操作 Git 实操手册

> 目标：掌握 IDEA 内置的 Git 操作，覆盖日常开发最常用的场景。
> 本手册对应培训笔记第01期的 Git 部分，全部在 IDEA 图形界面完成。

## 准备

1. 安装 IDEA（Community 版即可，内置 Git 支持）
2. 安装 Git，并配置好全局用户信息：
   - IDEA → Settings → Version Control → Git → 填 Git 路径
   - 命令行执行一次：`git config --global user.name "你的名字"` 和 `git config --global user.email "you@company.com"`

---

## 一、从 GitHub 克隆仓库

1. 打开 IDEA → **Get from VCS**（欢迎界面）或 File → New → **Project from Version Control**
2. URL 填仓库地址（如 `https://github.com/xxx/git-exercises.git`）
3. 点 **Clone**，IDEA 自动完成克隆并打开项目
4. 打开底部 **Git 工具窗口**（View → Tool Windows → Git 或 Alt+9）

---

## 二、日常提交流程（add / commit / push）

### 2.1 暂存与提交（add + commit）

1. 修改代码后，文件会显示为**蓝色**（已修改）
2. 在左侧 Project 面板选中文件 → 右键 → **Git → Add**（或 Ctrl+Alt+A）→ 文件变**绿色**
3. 右键 → **Git → Commit Directory**（或 Ctrl+K）
4. Commit 面板中：
   - 左侧勾选要提交的文件
   - 中间写提交信息（按规范：`feat: 添加用户模块`）
   - 点 **Commit**（仅本地提交）或 **Commit and Push**（提交并推送）

> 常用快捷键：Ctrl+K 提交，Ctrl+Shift+K 推送

### 2.2 查看变更（diff）

1. 提交前双击已修改的文件 → IDEA 打开 **Diff 视图**
2. 左侧是旧版本，右侧是新版本，改动高亮显示
3. 可以逐块接受/拒绝（点箭头图标）

### 2.3 推送（push）

1. Ctrl+Shift+K 或右键 → **Git → Push**
2. 确认推送的分支和目标仓库，点 **Push**
3. 首次推送会要求配置远程仓库（IDEA 通常已从 clone 自动带出 origin）

---

## 三、分支操作

### 3.1 创建与切换分支

1. 右下角状态栏显示当前分支（如 `main`）
2. 点它 → **New Branch** → 输入分支名（如 `feature/user-module`）→ 自动切换
3. 或者 Git 工具窗口 → Branches 面板管理

### 3.2 合并分支（merge）

1. 先**切换**到目标分支（如 main）
2. 右下角分支名 → **要合并的分支**（如 feature/user-module）→ 选中后右键 → **Merge into Current**
3. 如果出现冲突，见下方"冲突解决"

### 3.3 删除分支

1. 右下角分支名 → 找到要删的分支 → 右键 → **Delete Branch**

---

## 四、Stash 暂存

> 场景：正在开发 feature，突然要切分支修 bug，但不想提交当前半成品。

1. 右键项目 → **Git → Stash Changes**
2. 填说明（如"用户模块开发中"）→ OK
3. 此时工作区干净了，可以随意切换分支
4. 修完 bug 后：右键 → **Git → Unstash Changes** → 选择之前的 stash → **Apply**（保留记录）或 **Pop**（应用并删除）

> 底部 Version Control 工具窗口 → Local Changes 标签页，能看到 Stash 列表

---

## 五、冲突解决（merge 冲突）

1. 合并分支发生冲突时，IDEA 弹出提示，冲突文件显示**红色**
2. 打开冲突文件 → IDEA 弹出 **Merge Revisions** 三栏视图：
   - **左**：本地版本（ours）
   - **中**：合并结果（可手动编辑）
   - **右**：要合并进来的版本（theirs）
3. 用顶部的按钮逐块选择：`>>` 取右边 / `<<` 取左边 / `X` 都不要
4. 编辑完中间结果 → 点 **Apply** → 文件变绿色
5. 右键 → **Git → Commit** 完成合并提交

---

## 六、查看历史与回退

### 6.1 查看提交历史（log）

1. Git 工具窗口 → **Log 标签页**
2. 左侧是提交列表（可按分支/作者/日期筛选）
3. 点某条提交 → 右侧看它改了什么文件、哪些代码

### 6.2 撤销工作区修改（checkout / revert）

| 场景 | IDEA 操作 |
|------|----------|
| 撤销单个文件的未提交修改 | 右键文件 → Git → Revert → 选择 **Local Changes** |
| 撤销已 commit 的某次提交 | Log 中右键该提交 → **Revert Commit**（生成反向提交，安全） |

---

## 七、查看谁改的代码（git blame）

1. 打开文件 → 右键编辑器 → **Git → Annotate**（或 Ctrl+Alt+Shift+A）
2. 每行代码左侧显示：提交人、提交时间、提交 hash
3. 点左侧信息可以查看该行的提交详情

---

## 八、常见问题

| 问题 | 解决 |
|------|------|
| 文件变红色 | 未跟踪的新文件，需要先 Add |
| 文件变绿色 | 已 Add（暂存），需要 Commit |
| 文件变蓝色 | 已修改，未 Add |
| Push 被拒绝 | 远程有新提交，先 **Pull**（选择 Rebase）再 Push |
| 误提交了 | Log 中右键 → **Revert Commit** |
| 误删了未提交内容 | 右键 → Git → **Local History** 找回 |

---

## 九、对应笔记练习的映射

| 笔记练习 | IDEA 对应操作 |
|----------|--------------|
| git init / clone | 一、克隆仓库 |
| git add / commit / push | 二、日常提交流程 |
| git branch / switch | 三、分支操作 |
| git merge 冲突 | 五、冲突解决 |
| git stash | 四、Stash 暂存 |
| git log / revert | 六、查看历史与回退 |
| git blame | 七、Annotate |
