# 1.单机配置示例

```java
spring:
redis:
host:localhost
port:6379
password:123456
database:0
lettuce:
pool:
max-idle:16
max-active:32
min-idle:8
```

# 2.集群配置示例

```java
spring:
redis:
password:123456
cluster:
nodes:10.255.144.115:7001,10.255.144.115:7002,10.255.144.115:7003,10.255.144.115:7004,10.255.144.115:7005,10.255.144.115:7006
max-redirects:3
```