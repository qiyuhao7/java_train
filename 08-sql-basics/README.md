# 第08期：SQL 基础介绍

## 实战内容

1. 建表练习（用户、订单、订单明细）
2. CRUD 操作
3. 多表 JOIN 查询
4. 聚合统计与分组
5. 子查询与 EXISTS

## 使用方式

```bash
# 连接数据库后依次执行 SQL 文件
psql -U postgres -d training -f sql/01-create-tables.sql
psql -U postgres -d training -f sql/02-insert-data.sql
psql -U postgres -d training -f sql/03-queries.sql
```
