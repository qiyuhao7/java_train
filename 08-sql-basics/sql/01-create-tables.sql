-- 第08期：建表练习
-- 电商系统基础表结构

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id          BIGSERIAL    PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL UNIQUE,
    email       VARCHAR(128) NOT NULL,
    password    VARCHAR(256) NOT NULL,
    age         INT          DEFAULT 0,
    city        VARCHAR(64),
    is_active   BOOLEAN      DEFAULT TRUE,
    create_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- 商品表
CREATE TABLE IF NOT EXISTS t_product (
    id          BIGSERIAL     PRIMARY KEY,
    name        VARCHAR(128)  NOT NULL,
    category    VARCHAR(64)   NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    stock       INT           NOT NULL DEFAULT 0,
    create_time TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id          BIGSERIAL     PRIMARY KEY,
    order_no    VARCHAR(64)   NOT NULL UNIQUE,
    user_id     BIGINT        NOT NULL REFERENCES t_user(id),
    amount      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status      SMALLINT      NOT NULL DEFAULT 0,  -- 0-待支付 1-已支付 2-已取消
    create_time TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

-- 订单明细表
CREATE TABLE IF NOT EXISTS t_order_item (
    id          BIGSERIAL     PRIMARY KEY,
    order_id    BIGINT        NOT NULL REFERENCES t_order(id),
    product_id  BIGINT        NOT NULL REFERENCES t_product(id),
    quantity    INT           NOT NULL DEFAULT 1,
    price       DECIMAL(10,2) NOT NULL
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_order_user_id ON t_order(user_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON t_order(status);
CREATE INDEX IF NOT EXISTS idx_order_create_time ON t_order(create_time);
CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON t_order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_user_city ON t_user(city);

COMMENT ON TABLE t_user IS '用户表';
COMMENT ON TABLE t_order IS '订单表';
COMMENT ON COLUMN t_order.status IS '0-待支付 1-已支付 2-已取消';
