# Hive数据导出

---

###导出文件到本地系统
```python
hive> insert overwrite local directory '/home/bigdata/syg' select * from syg;
```
注意：从hive导出数据到本地时注意指定的目录`/home/bigdata/syg`。如果指定的目录存在，hive会先删除该目录下的所有文件。如果目录不存在，则会创建。输出的数据序列化为text格式，分隔符为^A，行与行之间通过换行符连接。如果存在不是基本类型的列，则这些列将被序列化为JSON 格式。在Hive 0.11.0 可以输出字段的分隔符，之前版本的默认为 ^A。
###导出到HDFS
```python
hive> insert overwrite directory '/home/bigdata/syg' select * from syg;
```
注意：这里少了关键字`local`
###导出到Hive的另一个表中（表已存在）
我们可以将查询结果导出到另一个hive表中。
```python
insert into table test select id, name, tel from syg;
```
###导出到Hive的另一个表中（表不存在）
我们可以在创建表的同时将另一个表的数据导出到新建的表中。

CTAS（create table .. as select）
```python
hive> create table test_syg_ctas as select * from syg;
```
###Tips
hive版本在0.11.0及以后才支持导出数据时指定分隔符。
```python
hive> insert overwrite directory '/home/bigdata/syg' row format delimited fields terminated by ','  select * from syg;
```
执行如上命令会报如下错误：
AILED: ParseException line 1:47 cannot recognize input near 'row' 'format' 'delimited' in select clause.

语法：
```python
INSERT OVERWRITE [LOCAL] DIRECTORY directory1
  [ROW FORMAT row_format] [STORED AS file_format] (Note: Only available starting with Hive 0.11.0)
  SELECT ... FROM ...
```