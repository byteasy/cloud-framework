cd parent
call mvn clean install
rem call mvn source:jar install

cd ../cloud-service-api
call mvn clean install
rem call mvn source:jar install

cd ../common
call mvn clean install
rem call mvn source:jar install

cd ../cloud-service-core
call mvn clean install
rem call mvn source:jar install

cd ../cloud-mq-vertx
call mvn clean install
rem call mvn source:jar install

cd ../cloud-platform/platform-api
call mvn clean install
rem call mvn source:jar install

cd ../../cloud-service-common
call mvn clean install
rem call mvn source:jar install

cd ../cloud-script-sdk
call mvn clean install
rem call mvn source:jar install

cd ../cloud-permission-sdk
call mvn clean install
rem call mvn source:jar install

cd ../cloud-mq-restfull
call mvn clean install

cd ../cloud-service-starter
call mvn clean install
rem call mvn source:jar install

cd ../cloud-redis-service
call mvn clean install
rem call mvn source:jar install

cd ../mybatis-plus-generator-byteasy
call mvn clean install
