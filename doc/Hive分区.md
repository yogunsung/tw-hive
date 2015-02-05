# Hive分区

---

###背景

 1. Hive在select查询中一般会扫描整个表的内容，会浪费很多时间在没有必要的工作上。有时候只需要扫描表中我们关心的一部分数据，为此Hive引入了分区（partition）这个概念。
 2. 分区表指的是在创建表时指定的partition的分区空间。
 3. 如果需要创建有分区的表，需要在建表的时候加上partitioned by。

###说明

 1. 一个表可以拥有一个或者多个分区，每个分区以文件夹的形式单独存在表文件夹的目录下。
 2. 表和列名不区分大小写。
 3. 分区是以字段的形式在表结构中存在，通过describe table命令可以查看到字段存在，但是该字段不存放实际的数据内容，仅仅是分区的表示。
 4. 分区建表分为2种，一种是单分区，也就是说在表文件夹目录下只有一级文件夹目录。另外一种是多分区，表文件夹下出现层级文件夹。

###建表
```ptyhon
CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name [(col_name data_type [COMMENT col_comment], ...)] [COMMENT table_comment] [PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)] [CLUSTERED BY (col_name, col_name, ...) [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS] [ROW FORMAT row_format] [STORED AS file_format] [LOCATION hdfs_path]
```
例：
```ptyhon
DROP TABLE IF EXISTS TEST_SYG;
CREATE EXTERNAL TABLE TEST_SYG (
TIME_ID int,
CNTY_ID string,
CNTY_NAME string,
OPEN_CNT int,
SIGN_CNT int,
SK_CNT int,
ACTV_CNT int
)
PARTITIONED BY(dt STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;
```
建表的时候一定要指定`PARTITIONED BY`，用于指定分区列。

###添加分区
前提是表已经创建，并指定了PARTITIONED BY
```ptyhon
ALTER TABLE table_name ADD partition_spec [ LOCATION 'location1' ] partition_spec [ LOCATION 'location2' ] ... 

partition_spec: PARTITION (partition_col = partition_col_value, partition_col = partiton_col_value, ...)
```
用户可以用 ALTER TABLE ADD PARTITION 来向一个表中增加分区。当分区名是字符串时加引号。例：
```ptyhon
ALTER TABLE TEST_SYG ADD PARTITION (dt=20150204) location '/tmp/hive/test_syg' 
```
加载/tmp/hive/test_syg目录下的所有文件到TEST_SYG表中。

###删除分区
```ptyhon
ALTER TABLE table_name DROP partition_spec, partition_spec,...
```
用户可以用 ALTER TABLE DROP PARTITION 来删除分区。分区的元数据和数据将被一并删除。例：
```ptyhon
ALTER TABLE TEST_SYG DROP PARTITION (dt=200150204);
```
###数据加载进分区表
```ptyhon
LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]
```
例：
```ptyhon
LOAD DATA INPATH '/tmp/hive/test_syg.txt' INTO TABLE TEST_SYG PARTITION(dt=20150204); 
```
当数据被加载至表中时，不会对数据进行任何转换。Load操作只是将数据复制至Hive表对应的位置。数据加载时在表下自动创建一个目录，文件存放在该分区下。

###查看分区语句
```ptyhon
SHOW PARTITIONS table_name
```
例：
```ptyhon
show partitions test_syg;
```