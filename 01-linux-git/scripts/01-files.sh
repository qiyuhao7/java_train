#!/bin/bash
# 01-files.sh — 文件与目录操作（最常用命令实操）
# 运行：bash 01-files.sh
# 也可复制命令逐条在终端手动执行学习

WORK="$HOME/training-workspace"
rm -rf "$WORK"
mkdir -p "$WORK"

echo "=== 1. pwd 查看当前所在目录 ==="
pwd

echo ""
echo "=== 2. ls 列出文件（-l 详细, -a 含隐藏, -h 人类可读）==="
ls -la "$WORK"

echo ""
echo "=== 3. mkdir 创建目录（-p 可递归建多层）==="
mkdir -p "$WORK/app/src"
ls "$WORK/app"

echo ""
echo "=== 4. touch 创建空文件 ==="
touch "$WORK/app/README.md"
echo "# 培训项目" > "$WORK/app/README.md"
ls -l "$WORK/app"

echo ""
echo "=== 5. cp 复制文件 ==="
cp "$WORK/app/README.md" "$WORK/app/README.bak"
ls "$WORK/app"

echo ""
echo "=== 6. mv 移动/重命名（同目录下就是重命名）==="
mv "$WORK/app/README.bak" "$WORK/app/note.md"
ls "$WORK/app"

echo ""
echo "=== 7. rm 删除（-r 删目录, -f 强制）==="
rm "$WORK/app/note.md"
rm -rf "$WORK/app/src"
ls "$WORK/app"

echo ""
echo "=== 8. find 按名字查找文件 ==="
find "$WORK" -name "*.md"

echo ""
echo "=== 9. ln -s 创建软链接（类似快捷方式）==="
ln -s "$WORK/app/README.md" "$WORK/readme-link"
ls -la "$WORK"

echo ""
echo "=== 完成 ==="
echo "练习目录: $WORK"
