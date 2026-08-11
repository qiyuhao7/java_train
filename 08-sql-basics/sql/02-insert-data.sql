-- 第08期：插入测试数据

-- 用户数据
INSERT INTO t_user (username, email, password, age, city, is_active) VALUES
('zhangsan', 'zhangsan@test.com', 'pwd_hash_1', 28, '北京', TRUE),
('lisi', 'lisi@test.com', 'pwd_hash_2', 35, '上海', TRUE),
('wangwu', 'wangwu@test.com', 'pwd_hash_3', 22, '北京', FALSE),
('zhaoliu', 'zhaoliu@test.com', 'pwd_hash_4', 31, '深圳', TRUE),
('qianqi', 'qianqi@test.com', 'pwd_hash_5', 45, '上海', TRUE),
('sunba', 'sunba@test.com', 'pwd_hash_6', 26, '广州', TRUE),
('zhoujiu', 'zhoujiu@test.com', 'pwd_hash_7', 33, '深圳', TRUE),
('wushi', 'wushi@test.com', 'pwd_hash_8', 29, '杭州', FALSE);

-- 商品数据
INSERT INTO t_product (name, category, price, stock) VALUES
('MacBook Pro 14', '电脑', 14999.00, 50),
('iPhone 15', '手机', 7999.00, 200),
('AirPods Pro', '耳机', 1899.00, 500),
('机械键盘', '外设', 599.00, 300),
('4K显示器', '外设', 2999.00, 100),
('iPad Air', '平板', 4799.00, 150),
('USB-C Hub', '配件', 199.00, 1000),
('鼠标垫', '配件', 49.00, 2000);

-- 订单数据
INSERT INTO t_order (order_no, user_id, amount, status, create_time) VALUES
('ORD-20240101-001', 1, 14999.00, 1, NOW() - INTERVAL '30 days'),
('ORD-20240101-002', 1, 1899.00, 1, NOW() - INTERVAL '25 days'),
('ORD-20240102-001', 2, 7999.00, 1, NOW() - INTERVAL '20 days'),
('ORD-20240103-001', 2, 599.00, 2, NOW() - INTERVAL '18 days'),
('ORD-20240104-001', 4, 2999.00, 1, NOW() - INTERVAL '15 days'),
('ORD-20240105-001', 4, 4799.00, 0, NOW() - INTERVAL '10 days'),
('ORD-20240106-001', 5, 199.00, 1, NOW() - INTERVAL '7 days'),
('ORD-20240107-001', 6, 8598.00, 1, NOW() - INTERVAL '5 days'),
('ORD-20240108-001', 7, 49.00, 1, NOW() - INTERVAL '3 days'),
('ORD-20240109-001', 1, 12798.00, 0, NOW() - INTERVAL '1 day');

-- 订单明细
INSERT INTO t_order_item (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 14999.00),
(2, 3, 1, 1899.00),
(3, 2, 1, 7999.00),
(4, 4, 1, 599.00),
(5, 5, 1, 2999.00),
(6, 6, 1, 4799.00),
(7, 7, 1, 199.00),
(8, 2, 1, 7999.00),
(8, 4, 1, 599.00),
(9, 8, 1, 49.00),
(10, 2, 1, 7999.00),
(10, 6, 1, 4799.00);
