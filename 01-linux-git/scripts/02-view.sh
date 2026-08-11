#!/bin/bash
# 02-view.sh — 文件查看与搜索（最常用命令实操）
# 运行：bash 02-view.sh
# 需先运行 01-files.sh 生成练习文件

WORK="$HOME/training-workspace"
FILE="$WORK/app/README.md"

# 先生成示例日志，便于练习 grep/tail
mkdir -p "$WORK/logs"
for i in $(seq 1 20); do
    if [ $((i % 5)) -eq 0 ]; then
        echo "$(date '+%Y-%m-%d %H:%M:%S') [ERROR] 连接超时: retry=$i" >> "$WORK/logs/app.log"
    else
        echo "$(date '+%Y-%m-%d %H:%M:%S') [INFO] 请求处理完成: /api/users/$i" >> "$WORK/logs/app.log"
    fi
done

echo "=== 1. cat 查看小文件全部内容 ==="
cat "$FILE"

echo ""
echo "=== 2. head / tail 查看文件开头 / 结尾 ==="
head -3 "$WORK/logs/app.log"
echo "..."
tail -3 "$WORK/logs/app.log"

echo ""
echo "=== 3. tail -f 实时跟踪日志（Ctrl+C 退出）==="
echo "执行: tail -f $WORK/logs/app.log"
echo "另开一个终端执行: echo 'test' >> $WORK/logs/app.log 可看到实时输出"

echo ""
echo "=== 4. grep 搜索关键词（-n 行号, -c 计数, -i 忽略大小写）==="
grep "ERROR" "$WORK/logs/app.log"
echo "ERROR 条数: $(grep -c 'ERROR' "$WORK/logs/app.log")"

echo ""
echo "=== 5. grep -r 目录递归搜索 ==="
grep -rn "ERROR" "$WORK/logs/"

echo ""
echo "=== 6. less 分页查看大文件（空格翻页, q 退出）==="
echo "执行: less $WORK/logs/app.log"
