

# hive/hbase表结构设计

## 数据收集表

### hbase表结构

表名称 pv_log_yyyy_mm_dd_HH

```sql
create database my_hive;
```

```shell
create 'pv_log_yyyy_mm_dd_HH','log';
```

### hive表结构 

表名称：pv_log_hive_yyyy_mm_dd_HH

| 字段名称        | 类型   | 备注                |
| --------------- | ------ | ------------------- |
| key             | string |                     |
| appid           | string | 项目appid           |
| method          | string |                     |
| ip              | string |                     |
| port            | string |                     |
| url             | string |                     |
| country         | string | 国家                |
| province        | string | 省                  |
| city            | string | 市                  |
| request_time    | string | 请求时间 10位时间戳 |
| os_name         | string | 操作系统名称        |
| os_version      | string | 操作系统版本号      |
| browser_name    | string | 浏览器名称          |
| browser_version | string | 浏览器版本          |
| device_type     | string | moibie/pc           |

```sql
-- 已经过时
CREATE EXTERNAL TABLE pv_log_hive 
(key string, user_agent string, method string,ip string,port string,url string,request_time string)  
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'  
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,log:user_agent,log:method,log:ip,log:port,log:url,log:request_time")  
TBLPROPERTIES ("hbase.table.name" = "pv_log");
```

## mysql 表设计

### 项目表

表名称：kfd_project

| 列名称       | 类型        | 备注        |
| ------------ | ----------- | ----------- |
| id           | bigint      | 主键自增    |
| project_name | varchar(50) | 项目名称    |
| appid        | varchar(50) |             |
| project_url  | varchar(50) | 项目url路径 |
| create_time  | datetime    | 创建时间    |

### 用户项目关联表

表名称：kfd_user_project_relation

| 列名称      | 类型     | 备注     |
| ----------- | -------- | -------- |
| id          | bigint   | 主键自增 |
| user_id     | bigint   | 用户id   |
| project_id  | bigint   | 项目id   |
| create_time | datetime | 创建时间 |

### PV统计类表

#### 项目pv总统计表

表名称：kfd_pv_all

| 列名称       | 类型        | 备注                |
| ------------ | ----------- | ------------------- |
| id           | bigint      | 主键自增            |
| appid        | varchar(50) |                     |
| pv_count     | bigint      | pv总数              |
| create_time  | datetime    | 创建时间            |
| request_time | datetime    | yyyy-mm-dd HH:00:00 |


####  客户端请求方式pv总统计表

表名：kfd_pv_http_client

| 列名称       | 类型        | 注释                |
| ------------ | ----------- | ------------------- |
| id           | bigint      | 主键                |
| appid        | varchar(50) | 项目appid           |
| create_time  | datatime    | 创建时间            |
| request_time | datetime    | yyyy-mm-dd HH:00:00 |
| method       | varchar(50) | 请求方式            |
| pv_count     | bigint      | pc总数量            |




### UV统计类表

#### 浏览器UV统计表

表名：kfd_uv_browser

| 列名称       | 类型        | 注释                |
| ------------ | ----------- | ------------------- |
| id           | bigint      | 主键                |
| appid        | varchar(50) | 项目appid           |
| create_time  | datatime    | 创建时间            |
| request_time | datetime    | yyyy-mm-dd HH:00:00 |
| browser_name | varchar(50) | 浏览器名称          |
| uv_count     | bigint      | 浏览器UV数量        |

#### 操作系统UV统计表

表名：kfd_uv_os

| 列名称       | 类型        | 注释                |
| ------------ | ----------- | ------------------- |
| id           | bigint      | 主键                |
| appid        | varchar(50) | 项目appid           |
| create_time  | datatime    | 创建时间            |
| request_time | datetime    | yyyy-mm-dd HH:00:00 |
| os_name      | varchar(50) | 操作系统+版本       |
| uv_count     | bigint      | 操作系统uv数量      |

#### 客户端类型UV统计表

表名：kfd_uv_client_type

| 列名称          | 类型        | 注释                |
| --------------- | ----------- | ------------------- |
| id              | bigint      | 主键                |
| appid           | varchar(50) | 项目appid           |
| create_time     | datatime    | 创建时间            |
| request_time    | datetime    | yyyy-mm-dd HH:00:00 |
| mobile_uv_count | bigint      | 移动端uv数量        |
| pc_uv_count     | bigint      | pc端uv数量          |
#### 用户地域分布UV统计表

表名：kfd_uv_user_area_distribution

| 列名称          | 类型        | 注释                |
| --------------- | ----------- | ------------------- |
| id              | bigint      | 主键                |
| appid           | varchar(20) | 项目appid           |
| start_count     | datatime    | 启动次数            |
| create_time     | datetime    | 创建时间            |
| request_time    | bigint      | 访问时间-yyyy-mm-dd HH  |
| country         | bigint      | 国家          |
| province        | varchar(20) | 省份          |
| city            | varchar(20) | 城市          |
