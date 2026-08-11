-- 第08期：查询练习
-- 逐条执行，理解每个查询的含义

-- ===== 基础查询 =====

-- 1. 查询所有活跃用户
SELECT id, username, email, city FROM t_user WHERE is_active = TRUE;

-- 2. 模糊查询：北京的用户
SELECT * FROM t_user WHERE city LIKE '%北京%';

-- 3. 排序 + 分页：按年龄降序，取前3
SELECT username, age FROM t_user ORDER BY age DESC LIMIT 3;

-- ===== 聚合统计 =====

-- 4. 各城市用户数
SELECT city, COUNT(*) AS user_count
FROM t_user
GROUP BY city
ORDER BY user_count DESC;

-- 5. 各状态订单数和总金额
SELECT status,
       COUNT(*) AS order_count,
       SUM(amount) AS total_amount,
       AVG(amount) AS avg_amount
FROM t_order
GROUP BY status;

-- 6. HAVING：订单数超过1的用户
SELECT user_id, COUNT(*) AS cnt
FROM t_order
GROUP BY user_id
HAVING COUNT(*) > 1;

-- ===== 多表 JOIN =====

-- 7. 用户 + 订单（INNER JOIN）
SELECT u.username, o.order_no, o.amount, o.status
FROM t_user u
INNER JOIN t_order o ON u.id = o.user_id
WHERE o.status = 1
ORDER BY o.amount DESC;

-- 8. LEFT JOIN：包含无订单的用户
SELECT u.username, COUNT(o.id) AS order_count, COALESCE(SUM(o.amount), 0) AS total
FROM t_user u
LEFT JOIN t_order o ON u.id = o.user_id
GROUP BY u.id, u.username
ORDER BY total DESC;

-- 9. 三表连接：订单 + 用户 + 商品明细
SELECT o.order_no, u.username, p.name AS product, oi.quantity, oi.price
FROM t_order o
JOIN t_user u ON o.user_id = u.id
JOIN t_order_item oi ON o.id = oi.order_id
JOIN t_product p ON oi.product_id = p.id
ORDER BY o.create_time DESC;

-- ===== 子查询 =====

-- 10. 消费超过平均订单金额的用户
SELECT username FROM t_user
WHERE id IN (
    SELECT user_id FROM t_order
    WHERE amount > (SELECT AVG(amount) FROM t_order)
);

-- 11. EXISTS：从未下单的用户
SELECT * FROM t_user u
WHERE NOT EXISTS (SELECT 1 FROM t_order o WHERE o.user_id = u.id);

-- ===== 实用函数 =====

-- 12. 最近7天每天的订单量
SELECT DATE(create_time) AS day, COUNT(*) AS cnt
FROM t_order
WHERE create_time >= NOW() - INTERVAL '30 days'
GROUP BY DATE(create_time)
ORDER BY day DESC;

-- 13. CASE 表达式：订单状态翻译
SELECT order_no, amount,
    CASE status
        WHEN 0 THEN '待支付'
        WHEN 1 THEN '已支付'
        WHEN 2 THEN '已取消'
    END AS status_text
FROM t_order;

-- 14. 各品类销售额（已支付订单）
SELECT p.category, SUM(oi.quantity * oi.price) AS sales
FROM t_order o
JOIN t_order_item oi ON o.id = oi.order_id
JOIN t_product p ON oi.product_id = p.id
WHERE o.status = 1
GROUP BY p.category
ORDER BY sales DESC;
