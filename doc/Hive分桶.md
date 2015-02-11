# Hive桶

###概述
对于Hive的每一个表（table）或者分区， Hive可以进一步组织成桶，也就是说桶是更为细粒度的数据范围划分。Hive也是针对某一列进行桶的组织。Hive采用对列值哈希，然后除以桶的个数求余的方式决定该条记录存放在哪个桶当中。
把表（或者分区）组织成桶（Bucket）有两个理由：

（1）获得更高的查询处理效率。桶为表加上了额外的结构，Hive 在处理有些查询时能利用这个结构。具体而言，连接两个在（包含连接列的）相同列上划分了桶的表，可以使用 Map 端连接 （Map-side join）高效的实现。比如JOIN操作。对于JOIN操作两个表有一个相同的列，如果对这两个表都进行了桶操作。那么将保存相同列值的桶进行JOIN操作就可以，可以大大较少JOIN的数据量。

（2）使取样（sampling）更高效。在处理大规模数据集时，在开发和修改查询的阶段，如果能在数据集的一小部分数据上试运行查询，会带来很多方便。

###创建带桶的table
```python
create table bucketed_user(id int,name string) clustered by (id) sorted by(name) into 4 buckets row format delimited fields terminated by '\t' stored as textfile;
```
首先，我们来看如何告诉Hive—个表应该被划分成桶。我们使用`CLUSTERED BY`子句来指定划分桶所用的列和要划分的桶的个数： 
```python
CREATE TABLE bucketed_user (id INT) name STRING) 
CLUSTERED BY (id) INTO 4 BUCKETS; 
```
在这里，我们使用用户ID来确定如何划分桶(Hive使用对值进行哈希并将结果除 以桶的个数取余数。这样，任何一桶里都会有一个随机的用户集合。

对于map端连接的情况，两个表以相同方式划分桶。处理左边表内某个桶的mapper知道右边表内相匹配的行在对应的桶内。因此，mapper只需要获取那个桶 (这只是右边表内存储数据的一小部分)即可进行连接。这一优化方法并不一定要求两个表必须桶的个数相同，两个表的桶个数是倍数关系也可以。

桶中的数据可以根据一个或多个列另外进行排序。由于这样对每个桶的连接变成了高效的归并排序(merge-sort), 因此可以进一步提升map端连接的效率。以下语法声明一个表使其使用排序桶： 
```python
CREATE TABLE bucketed_users (id INT, name STRING) 
CLUSTERED BY (id) SORTED BY (id ASC) INTO 4 BUCKETS; 
```
我们如何保证表中的数据都划分成桶了呢？把在Hive外生成的数据加载到划分成桶的表中，当然是可以的。其实让Hive来划分桶更容易。这一操作通常针对已有的表。 

Hive并不检查数据文件中的桶是否和表定义中的桶一致(无论是对于桶 的数量或用于划分桶的列）。如果两者不匹配，在査询时可能会碰到错 误或未定义的结果。因此，建议让Hive来进行划分桶的操作。 

有一个没有划分桶的用户表： 
```python
hive> SELECT * FROM users; 
0    Nat 
2    Doe 
B    Kay 
4    Ann 
```
###强制多个reduce进行输出

要向分桶表中填充成员，需要将`hive.enforce.bucketing` 属性设置为true。这样，Hive 就知道用表定义中声明的数量来创建桶。然后使用 INSERT 命令即可。需要注意的是： clustered by和sorted by不会影响数据的导入，这意味着，用户必须自己负责数据如何如何导入，包括数据的分桶和排序。 
`'set hive.enforce.bucketing = true'` 可以自动控制上一轮reduce的数量从而适配bucket的个数，当然，用户也可以自主设置mapred.reduce.tasks去适配bucket个数，推荐使用'set hive.enforce.bucketing = true'  
###往表中插入数据
```python
INSERT OVERWRITE TABLE bucketed_users SELECT * FROM users; 
```
物理上，每个桶就是表(或分区）目录里的一个文件。它的文件名并不重要，但是桶n是按照字典序排列的第n个文件。事实上，桶对应于MapReduce 的输出文件分区：一个作业产生的桶(输出文件)和reduce任务个数相同。我们可以通过查看刚才 创建的bucketd_users表的布局来了解这一情况。  

###查看表的结构
```python
hive> dfs -ls /user/hive/warehouse/bucketed_users; 
```
将显示有4个新建的文件。文件名如下(文件名包含时间戳，由Hive产生，因此每次运行都会改变)： 
attempt_201005221636_0016_r_000000_0 
attempt_201005221636_0016_r-000001_0 
attempt_201005221636_0016_r_000002_0 
attempt_201005221636_0016_r_000003_0 
第一个桶里包括用户ID为O和4，因为一个INT的哈希值就是这个整数本身，在这里除以桶数(4)以后的余数。

###读取数据，看每一个文件的数据
```python
hive> dfs -cat /user/hive/warehouse/bucketed_users/*0_0; 
0 Nat 
4 Ann 
```
用TABLESAMPLE子句对表进行取样，我们可以获得相同的结果。这个子句会将 查询限定在表的一部分桶内，而不是使用整个表。

###对桶中的数据进行采样：
```python
hive> SELECT * FROM bucketed_users 
>    TABLESAMPLE(BUCKET 1 OUT OF 4 ON id); 
0 Nat 
4 Ann 
```
桶的个数从1开始计数。因此，前面的查询从4个桶的第一个中获取所有的用户。 对于一个大规模的、均匀分布的数据集，这会返回表中约四分之一的数据行。我们也可以用其他比例对若干个桶进行取样(因为取样并不是一个精确的操作，因此这个比例不一定要是桶数的整数倍)。

###查询一半返回的桶数：
```python
hive> SELECT * FROM bucketed_users 
>    TABLESAMPLE(BUCKET 1 OUT OF 2 ON id)； 
0 Nat 
4 Ann 
2 Joe 
```
因为查询只需要读取和TABLESAMPLE子句匹配的桶，所以取样分桶表是非常高效 的操作。如果使用rand()函数对没有划分成桶的表进行取样，即使只需要读取很 小一部分样本，也要扫描整个输入数据集： 
```python
hive> SELECT * FROM users 
> TABLESAMPLE(BUCKET 1 OUT OF 4 ON rand()); 
2 Doe 
```
如果桶是排序的，还需要把hive.enforce.sorting设为true。 
显示原始文件时，因为分隔字符是一个不能打印的控制字符，因此字段都挤在一起。 

###抽样语法
tablesample是抽样语句，语法：`TABLESAMPLE(BUCKET x OUT OF y)`
y必须是table总bucket数的倍数或者因子。hive根据y的大小，决定抽样的比例。例如，table总共分了64份，当y=32时，抽取(64/32=)2个bucket的数据，当y=128时，抽取(64/128=)1/2个bucket的数据。x表示从哪个bucket开始抽取。例如，table总bucket数为32，tablesample(bucket 3 out of 16)，表示总共抽取（32/16=）2个bucket的数据，分别为第3个bucket和第（3+16=）19个bucket的数据。