# 开源大数据日志分析平台 kfd

kfd 开源大数据日志分析平台

github地址：

https://github.com/kfd-bigdata/kfd

期待您的小⭐⭐

码云地址:

 https://gitee.com/kfd-bigdata/kfd 

# 数据流图

![Image text](https://dev.tencent.com/u/java_1715656022/p/kfd/git/raw/master/%E6%95%B0%E6%8D%AE%E5%88%86%E6%9E%90%E6%95%B0%E6%8D%AE%E6%B5%81%E5%9B%BE.png)

# 开发所需环境

## 1 jdk8 环境

## 2 hadoop环境

## 3 hbase环境

## 4 hive环境

## 5 nginx环境

## 6 flum环境

## 7 kafka环境

# 技术选型

## v1版

大数据：hadoop hdfs/mr、hbase、hive、flum、kafka、zookeeper

基础框架：SpringBoot

持久框架：Hibernate+JPA

缓存：Apache Redis Cluster

数据库：Mysql主从

程序构建：Apache Maven

数据库连接池：阿里巴巴 Druid

日志处理：Apache Log4j/SLF4J

JSON处理：阿里巴巴

负载均衡、静态服务器：Apache Nginx

应用容器：Apache Tomcat

view:thymleaf模版

部署容器：docker

## 规划版v2

大数据：hadoop hdfs/mr、hbase、hive、flum、kafka、zookeeper

基础框架：SpringBoot

微服务网关：SpringCloud Gatway

微服务注册中心：SpringCloud Consul

微服务负载均衡：SpringCloud Feign

微服务链路追踪：Spring Cloud Sleuth、Spring Cloud ZipKin

微服务熔断降级：SpringCloud Hystrix

持久框架：Hibernate+JPA

搜索引擎：ElasticSearch

缓存：Apache Redis Cluster

数据库：Mysql主从

分库分表中间件：Apache ShardingSphere

程序构建：Apache Maven

数据库连接池：阿里巴巴 Druid

日志处理：Apache Log4j/SLF4J

JSON处理：阿里巴巴

负载均衡、静态服务器：LVS + Apache Nginx

应用容器：Apache Tomcat

veiw：vue/react 前后分离

部署容器：kubernates+docker





开发环境：

```shell
#打包
mvn package

```

 