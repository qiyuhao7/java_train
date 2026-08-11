-- 容器启动时自动执行的初始化 SQL
-- 放置在 /docker-entrypoint-initdb.d/ 目录

CREATE TABLE IF NOT EXISTS t_todo (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    priority    INT          DEFAULT 3,
    status      VARCHAR(20)  DEFAULT 'PENDING',
    create_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO t_todo (title, priority, status) VALUES
('学习 Docker 基础', 1, 'DONE'),
('编写 Dockerfile', 2, 'DONE'),
('Docker Compose 编排', 2, 'PENDING'),
('部署到容器平台', 3, 'PENDING');
