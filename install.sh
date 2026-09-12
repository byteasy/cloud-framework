. /etc/profile
cd parent
mvn clean install

cd ../cloud-service-api
mvn clean install

cd ../common
mvn clean install

cd ../cloud-service-core
mvn clean install

cd ../cloud-service-starter
mvn clean install

cd ../cloud-platform/platform-api
mvn clean install

cd ../cloud-script-sdk
mvn clean install

cd ../cloud-redis-service
mvn clean install

cd ../mybatis-plus-generator-byteasy
mvn clean install