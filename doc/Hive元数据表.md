# Hive元数据表

---

###分类
DADABASE相关
TABLE相关
数据存储相关SDS
COLUMN相关
SERDE相关(序列化)
PARTITION相关(分区)
SKEW相关(数据倾斜)
BUCKET相关(分桶)
PRIVS相关(权限管理)

###Hive元数据表
| 表名        | 说明                |  关联键  |
| --------    | -----               | :----:   |
| DBS|hive所有数据库信息|DB_ID|
| TBLS|hive表的基本信息|TBL_ID,DB_ID,SD_ID|
| TABLE_PARAMS|表级属性，如是否外部表，表注释等|TBL_ID|
| COLUMNS_V2|Hive表字段信息(字段注释，字段名，字段类型，字段序号)|CD_ID|