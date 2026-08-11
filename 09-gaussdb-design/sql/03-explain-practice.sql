-- 第09期：索引设计与执行计划分析

-- ===== 创建测试数据（模拟大表）=====
-- 如果数据量不够，可用以下方式生成：
-- INSERT INTO t_order (order_no, user_id, amount, status, create_time)
-- SELECT 'ORD-' || i, (random() * 1000)::int, (random() * 10000)::numeric(10,2),
--        (random() * 2)::smallint, NOW() - (random() * 365 || ' days')::interval
-- FROM generate_series(1, 100000) AS i;

-- ===== 执行计划分析 =====

-- 1. 无索引时的全表扫描
EXPLAIN ANALYZE
SELECT * FROM t_order WHERE user_id = 500 AND status = 1;

-- 2. 创建联合索引后对比
CREATE INDEX IF NOT EXISTS idx_order_user_status ON t_order(user_id, status);

EXPLAIN ANALYZE
SELECT * FROM t_order WHERE user_id = 500 AND status = 1;

-- 3. 覆盖索引（Index Only Scan）
CREATE INDEX IF NOT EXISTS idx_order_user_amount ON t_order(user_id) INCLUDE (amount, status);

EXPLAIN ANALYZE
SELECT amount, status FROM t_order WHERE user_id = 500;

-- 4. 排序优化
EXPLAIN ANALYZE
SELECT * FROM t_order WHERE status = 1 ORDER BY create_time DESC LIMIT 20;

-- 创建联合索引（等值在前，排序在后）
CREATE INDEX IF NOT EXISTS idx_order_status_time ON t_order(status, create_time DESC);

EXPLAIN ANALYZE
SELECT * FROM t_order WHERE status = 1 ORDER BY create_time DESC LIMIT 20;

-- ===== 分页优化 =====

-- 5. 大 OFFSET 的性能问题
EXPLAIN ANALYZE
SELECT * FROM t_order WHERE status = 1 ORDER BY create_time DESC LIMIT 20 OFFSET 50000;

-- 6. 游标分页（推荐）
EXPLAIN ANALYZE
SELECT * FROM t_order
WHERE status = 1 AND create_time < '2024-06-01'
ORDER BY create_time DESC
LIMIT 20;

-- 7. 延迟关联
EXPLAIN ANALYZE
SELECT o.* FROM t_order o
INNER JOIN (
    SELECT id FROM t_order WHERE status = 1
    ORDER BY create_time DESC LIMIT 20 OFFSET 50000
) tmp ON o.id = tmp.id;

-- ===== 索引失效场景验证 =====

-- 8. 函数导致索引失效
EXPLAIN ANALYZE
SELECT * FROM t_order WHERE EXTRACT(YEAR FROM create_time) = 2024;

-- 改写为范围查询
EXPLAIN ANALYZE
SELECT * FROM t_order
WHERE create_time >= '2024-01-01' AND create_time < '2025-01-01';

-- 9. 部分索引
CREATE INDEX IF NOT EXISTS idx_order_active ON t_order(user_id, create_time)
WHERE status != 2;  -- 只索引未取消的订单

EXPLAIN ANALYZE
SELECT * FROM t_order WHERE user_id = 100 AND status = 1;

-- ===== 统计信息更新 =====
ANALYZE t_order;

-- 查看表统计
SELECT relname, n_live_tup, n_dead_tup, last_analyze, last_autoanalyze
FROM pg_stat_user_tables
WHERE relname = 't_order';
