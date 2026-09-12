#!/bin/bash

# 接收命令行参数，默认值为 prod
ENV=${1:-dev}

# 根据参数设置不同的配置
case $ENV in
    prod)
        PROFILE="prod"
        ;;
    dev)
        PROFILE="dev"
        ;;
    it)
        PROFILE="it"
        ;;
    test)
        PROFILE="test"
        ;;
    *)
        echo "未知环境: $ENV"
        echo "使用方法: $0 {prod|it|dev|test}"
        exit 1
        ;;
esac

source /etc/profile

echo "启动环境: $PROFILE"

nohup $JAVA_HOME/bin/java \
    -Xms512m -Xmx512m \
    -DJM.LOG.PATH=$LOG_HOME/platform-service \
    -DJM.SNAPSHOT.PATH=$LOG_HOME/platform-service \
    -Dsun.misc.Unsafe.disableMemoryAllocationWarning=true \
    -Djdk.unsupported.providers.disable=true \
    -Djava.net.preferIPv4Stack=true \
    -Dio.netty.tryReflectionSetAccessible=true \
    -Dconsumer.enabled=false \
    --add-opens java.base/java.io=ALL-UNNAMED \
    --add-opens java.base/sun.net=ALL-UNNAMED \
    --add-opens java.base/java.util.zip=ALL-UNNAMED \
    --add-opens java.management/sun.management=ALL-UNNAMED \
    --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
    --add-opens java.base/java.nio=ALL-UNNAMED \
    --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
    --add-opens java.base/jdk.internal.platform=ALL-UNNAMED \
    -Dspring.cloud.nacos.discovery.ip=127.0.0.1 \
    -Dnacos.logging.config=classpath:nacos-log4j2-prod.xml \
    -Duser.timezone=Asia/Shanghai \
    -jar /data/app/platform/platform-service-1.0.0.jar \
    --spring.profiles.active=$PROFILE \
    > $LOG_HOME/platform-service/startup.log 2>&1 &

echo "服务已启动，PID: $!"