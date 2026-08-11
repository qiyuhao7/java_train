#!/bin/bash
# 03-process.sh — 进程与系统状态（最常用命令实操）
# 运行：bash 03-process.sh

echo "=== 1. ps 查看进程快照（ps aux 查看所有进程详情）==="
ps aux | head -5

echo ""
echo "=== 2. ps + grep 查找指定进程 ==="
echo "查找 java 进程:"
ps aux | grep java | grep -v grep

echo ""
echo "=== 3. ss 查看端口占用（-tulnp）==="
echo "查看本机监听的所有 TCP/UDP 端口:"
ss -tulnp | head -10

echo ""
echo "=== 4. top 实时查看 CPU/内存（q 退出）==="
echo "执行: top"

echo ""
echo "=== 5. free -h 查看内存 ==="
free -h

echo ""
echo "=== 6. df -h 查看磁盘 ==="
df -h | head -5

echo ""
echo "=== 7. nohup + & 后台运行命令 ==="
nohup sleep 300 > /dev/null 2>&1 &
PID=$!
echo "已在后台启动 sleep 300, PID=$PID"

echo ""
echo "=== 8. kill 终止进程（-9 强制）==="
kill "$PID"
echo "已终止 PID=$PID"
if ps -p "$PID" > /dev/null 2>&1; then
    echo "进程仍存在"
else
    echo "进程已退出"
fi

echo ""
echo "=== 9. du 查看目录占用 ==="
du -sh "$HOME/training-workspace" 2>/dev/null || echo "目录不存在，先运行 01-files.sh"

echo ""
echo "=== 10. lsof 查看端口被谁占用（需安装 lsof）==="
echo "执行: lsof -i :9099"
