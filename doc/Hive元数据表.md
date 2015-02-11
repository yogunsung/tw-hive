# Hive元数据表

###分类
1. DATABASE相关
2. TABLE相关
3. 数据存储相关SDS
4. COLUMN相关
5. SERDE相关(序列化)
6. PARTITION相关(分区)
7. SKEW相关(数据倾斜)
8. BUCKET相关(分桶)
9. PRIVS相关(权限管理)

###Hive元数据表
| 表名        | 说明                |  关联键  |
| --------    | -----               | :----:   |
| DBS|hive所有数据库信息|DB_ID|
| DATABASE_PARAMS|hive数据库相关属性|DB_ID|
| TBLS|hive表的基本信息|TBL_ID,DB_ID,SD_ID|
| TABLE_PARAMS|表级属性，如是否外部表，表注释等|TBL_ID|
| COLUMNS_V2|Hive表字段信息(字段注释，字段名，字段类型，字段序号)|CD_ID|
| SDS|所有hive表、表分区所对应的hdfs数据目录和数据格式|SD_ID,CD_ID,SERDE_ID|
| SERDES|Hive表序列化反序列化使用的类库信息|SERDE_ID|
| SERDE_PARAMS|序列化反序列化信息，如行分隔符、列分隔符、NULL的表示字符等|SERDE_ID|
| PARTITIONS|Hive表分区信息|PART_ID,SD_DI,TBL_ID|
| PARTITION_KEYS|Hive分区表分区键|TBL_ID|
| PARTITION_KEY_VALS|Hive表分区名(键值)|PART_ID|
| PARTITION_PARAMS|Hive表分区的其它属性|PART_ID|