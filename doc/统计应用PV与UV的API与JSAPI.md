# 统计应用PV，UV的API与JSAPI

### 什么是PV，UV

PV是page view的缩写，一般指的是网站的访问量或是点击量。

> PV之于网站，就像收视率之于电视，从某种程度上已成为投资者衡量商业网站表现的最重要尺度。

UV是unique visitor的缩写，指访问某个站点或点击某条新闻的不同IP地址的人数。

> 在同一天内，uv只记录第一次进入网站的具有独立IP的访问者，在同一天内再次访问该网站则不计数。独立IP访问者提供了一定时间内不同用户数量的统计指标，而没有反应出网站的全面活动。

### 引用统计工具的方式

#### JAVA API

对于java应用程序而言，仅仅只需要引入一个jar包依赖，对原程序完全没有侵入性。

jar包名称待定

jar包引入方式

```xml
maven
路径待定
```

#### JS API

待定

### 统计工具实现原理

#### JAVA

原理很简单，就是一个Interceptor，Interceptor拦截所有的request，在request中取出所需要的信息，而且不改变request，直接放行。

在Interceptor中将从request中取出的信息通过异步get请求发送到固定的nginx，并由nginx记录到日志中，以便于后续的程序进行分析。

#### JS

待定

### API参数说明

#### JAVA

`url` 客户端请求全路径

`ip` 客户端请求ip地址

`port` 客户端请求端口

`method` 客户端请求方式(GET, PUT, POST, DELETE等)

`user_agent` 客户端信息(包括浏览器内核与版本，系统内核与版本)

`request_time` 客户端请求时间(时间戳)

#### JS

待定







# 统计报表

## 1 pv统计

1.1 网站总pv统计（李双双）

1.2 总pv时间分布图（李双双）

1.3 url 列表总pv统计（李文龙）

1.4 url 列表 时间分布图（李文龙）

1.5 客户端请求方式(GET, PUT, POST, DELETE等) pv总统计（刘岩）

1.6  客户端请求方式(GET, PUT, POST, DELETE等) 时间分布图（刘岩）

## 2 用户统计

2.1 用户时间分布图 （皮皮）

2.2 活跃用户（李双双）

2.3 用户画像（后续完成）

2.4  用户地域分布图（裴广庭）

2.5  客户端统计图（终端分析）（张礼韬）

2.6  客户端时间分布图（张礼韬）

web展示端基础设施搭建 （皮皮）



## 参考：

百度统计：https://mtj.baidu.com/web/demo/overview?appId=468475



