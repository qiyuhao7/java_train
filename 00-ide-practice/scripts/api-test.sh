#!/bin/bash
# 接口测试脚本（替代 IDEA Ultimate 的 HTTP Client）
# 用法：bash scripts/api-test.sh  [user|order|batch|pay|cancel]
# 不加参数 = 全部执行

BASE="http://localhost:9099"

echo "=== 1. 查询用户列表 ==="
curl -s "$BASE/api/users" | python3 -m json.tool 2>/dev/null || curl -s "$BASE/api/users"
echo ""

echo "=== 2. 查询单个用户 ==="
curl -s "$BASE/api/users/1"
echo ""

echo "=== 3. 创建用户 ==="
curl -s -X POST "$BASE/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@test.com","age":25,"vip":true}'
echo ""

echo "=== 4. 批量创建 ==="
curl -s -X POST "$BASE/api/users/batch?count=3"
echo ""

echo "=== 5. 下单（调试断点用）==="
curl -s -X POST "$BASE/api/orders" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":999.00}'
echo ""

echo "=== 6. 下单（金额小，条件断点不触发）==="
curl -s -X POST "$BASE/api/orders" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":100.00}'
echo ""

echo "=== 7. 支付订单 1（条件断点：金额>500 才暂停）==="
curl -s -X POST "$BASE/api/orders/1/pay"
echo ""

echo "=== 8. 异常场景：不存在的用户下单（触发异常断点）==="
curl -s -X POST "$BASE/api/orders" \
  -H "Content-Type: application/json" \
  -d '{"userId":999,"amount":50.00}'
echo ""

echo "=== 完成 ==="
