192.168.31.2 

pengbo_liang



# 項目介紹:

## 停止容器：

[root@JD shell]# docker ps

```shell
[root@JD shell]# docker ps
CONTAINER ID        IMAGE                  COMMAND                  CREATED              STATUS              PORTS                    NAMES
e5f3b2fcd8aa        mytest/bim_admin:1.8   "java -Djava.securit…"   About a minute ago   Up About a minute   0.0.0.0:9999->9000/tcp   confident_golick


 docker stop e5f3b2fcd8aa
 或者
 docker kill  e5f3b2fcd8aa
```

## 啓動容器：

```shell
[root@JD shell]# sh start_bim_docter.sh 
ca43dde8f1c45ced9e110692807310e60cb0cb6a42d8a838dd3fd76732addf5c
port=9999 start ok
```



## 查看日志：

```shell
[root@JD shell]# tail -fn 100 /testdco/testFile.log
```

```shell
[root@JD shell]# docker logs ca43dde8f1c4
```

























设置阿里云 镜像加速

 ```shell
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://v2ltjwbg.mirror.aliyuncs.com"]
}
EOF
systemctl daemon-reload
systemctl restart docker
 ```


