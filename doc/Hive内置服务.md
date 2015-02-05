Hive内部自带了许多的服务，我们可以在运行时用–service选项来明确指定使用什么服务，如果你不知道Hive内部有多少服务，
可以用如下命令来查看帮助。

hive --service help

结果如下：
Usage ./hive <parameters> --service serviceName <service parameters>
Service List: beeline cli help hiveserver2 hiveserver hwi jar lineage metastore metatool rcfilecat 
Parameters parsed:
  --auxpath : Auxillary jars 
  --config : Hive configuration directory
  --service : Starts specific service/component. cli is default
Parameters used:
  HADOOP_HOME or HADOOP_PREFIX : Hadoop install directory
  HIVE_OPT : Hive options
For help on a particular service:
  ./hive --service serviceName --help
Debug help:  ./hive --debug --help

从上面可以看到Hive内置服务有beeline cli help hiveserver2 hiveserver hwi jar lineage metastore metatool rcfilecat 11中服务。

常用服务：
1、cli：这个就是Command Line Interface的简写，是Hive的命令行界面，用的比较多。这是默认的服务，直接可以在命令行里面使用。

2、hiveserver：这个可以让Hive以提供Trift服务的服务器形式来运行，可以允许许多不同语言编写的客户端进行通信。
使用需要启动HiveServer服务以和客户端联系，可以通过设置HIVE_PORT环境变量来设置服务器所监听的端口号，在默认的情况下，
端口为10000。可以通过下面方式来启动hiveserver：

hive --service hiveserver -p 10002
或者使用默认端口并在后台运行
nohup hive –service hiveserver & 

nohup hive --service hiveserver >> hiveserver.log 2>&1 &
echo $! > hive-server.pid

-p参数也是用来指定监听端口

3、hwi：其实就是hive web interface的缩写，它是Hive的Web接口，是hive cli的一个web替换方案。

4、jar：与Hadoop jar等价的Hive的接口，这是运行类路径中同时包含Hadoop和Hive类的Java应用程序的简便方式。

5、metastore：在默认情况下，metastore和Hive服务运行在同一个进程中。使用这个服务，可以让metastore作为一个单独的进程运行，我们可以通过METASTORE_PORT来指定监听的端口号。
