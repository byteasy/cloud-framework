#!/bin/bash
set -eu

# ===================== 环境变量配置 =====================
export JAVA_HOME=/data/tools/jdk-21.0.3
export MAVEN_HOME=/data/tools/apache-maven-3.9.12
export NODE_HOME=/data/tools/node-v24.12.0-linux-x64
export LOG_HOME=/data/app/logs
# 加入系统PATH，子脚本可继承环境
export PATH="${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${NODE_HOME}/bin:${PATH}"

# ===================== 前置路径校验 =====================
check_dir() {
    if [ ! -d "$1" ]; then
        echo "【错误】目录不存在：$1"
        exit 1
    fi
}

check_dir "${JAVA_HOME}"
check_dir "${MAVEN_HOME}"
check_dir "${NODE_HOME}"
check_dir "/data/devops/cloud-framework"
check_dir "/data/devops/ui-app"
check_dir "/data/devops/cloud-platform"

# ===================== 1. 编译framework依赖 =====================
echo "-----------------------------> 1.0 编译framework依赖"
cd /data/devops/cloud-framework
sudo -u tuna git checkout -B byteasy
sudo -u tuna git pull origin byteasy
# 改用bash执行，解决source not found
sudo -u tuna bash ./install.sh
echo "-----------------------------> 1.0 编译framework依赖成功"

# ===================== 2. 打包前端基座 =====================
echo "-----------------------------> 2.0 打包前端基座"
cd /data/devops/ui-app
sudo -u tuna git checkout -B byteasy-devops
# 分支删除容错：不存在不报错
sudo -u tuna git branch -D byteasy-base 2>/dev/null || true
sudo -u tuna git checkout origin/byteasy-base -b byteasy-base
sudo -u tuna git pull origin byteasy-base
sudo -u tuna bash bin/install-main.sh
echo "-----------------------------> 2.0 打包前端基座完成"

# ===================== 3. 打包前端平台服务分支 =====================
echo "-----------------------------> 3.0 打包前端平台服务分支"
cd /data/devops/ui-app
sudo -u tuna git checkout -B byteasy-devops
sudo -u tuna git branch -D byteasy-develop 2>/dev/null || true
sudo -u tuna git checkout origin/byteasy-develop -b byteasy-develop
sudo -u tuna git pull origin byteasy-develop
sudo -u tuna bash bin/install-platform-common.sh
echo "-----------------------------> 3.0 打包前端平台服务分支完成"

# ===================== 4. 打包platform平台服务 =====================
echo "-----------------------------> 4.0 打包platform平台服务"
cd /data/devops/cloud-platform
sudo -u tuna git checkout -B byteasy
sudo -u tuna git pull origin byteasy
sudo -u tuna $MAVEN_HOME/bin/mvn clean package -DskipTests
cd ./platform-api
sudo -u tuna $MAVEN_HOME/bin/mvn clean install -DskipTests
cd ..
echo "-----------------------------> 4.0 打包platform平台服务完成"

# ===================== 5. 重新部署platform平台服务 =====================
echo "-----------------------------> 5.0 重新部署platform平台服务"
sudo -u tuna mkdir -p /data/app/platform
sudo -u tuna mkdir -p "${LOG_HOME}/platform-service"

sudo -u tuna cp -f ./platform-service/target/platform-service-1.0.0.jar /data/app/platform
sudo -u tuna cp ./bin/startup.sh /data/app/platform/startup.sh
sudo -u tuna cp ./bin/shutdown.sh /data/app/platform/shutdown.sh

sudo -u tuna chmod +x /data/app/platform/*.sh
# bash执行启停脚本，兼容source语法
sudo -u tuna bash /data/app/platform/shutdown.sh
sudo -u tuna bash /data/app/platform/startup.sh it
echo "-----------------------------> 5.0 重新部署platform平台服务完成"

echo "==================== 全部部署任务执行完毕 ===================="