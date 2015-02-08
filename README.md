###这是一个学习hive的库

PS：Hive版本为0.10.0-cdh4.3.2

###TIPS

 1.使用jdbc方式连接hiveserver，只能使用默认的数据库即default。官方文档说明如下：
```python
"jdbc:hive://localhost:10000/default". Currently, the only dbname supported is "default".
```
 2.从hive导出数据到本地时注意指定的目录`/home/bigdata/syg`。如果指定的目录存在，hive会先删除该目录下的所有文件。如果目录不存在，则会创建。
```python 
insert overwrite local directory '/home/bigdata/syg' select * from syg;
```