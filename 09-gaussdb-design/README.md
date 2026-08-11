# 第09期：GaussDB 数据库设计与 SQL 调优

## 实战内容

1. 电商系统表结构设计（范式与反范式权衡）
2. 索引设计（联合索引、部分索引）
3. EXPLAIN 执行计划分析
4. 慢查询优化案例
5. 分页优化（游标分页 vs OFFSET）

## 使用方式

```bash
# 连接 GaussDB（兼容 psql）
gsql -h <host> -U <user> -d training

# 执行设计脚本
\i sql/01-schema-design.sql
\i sql/02-index-design.sql
\i sql/03-explain-practice.sql
```

## docs/ 目录

- `design-principles.md`：表设计原则总结
