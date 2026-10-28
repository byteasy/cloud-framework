cd parent
call mvn clean install
rem call mvn source:jar install

cd ../cloud-util
call mvn clean install
rem call mvn source:jar install

cd ../cloud-service-api
call mvn clean install
rem call mvn source:jar install

cd ../cloud-framework-infinispan
call mvn clean install
rem call mvn source:jar install

cd ../cloud-framework-vertx
call mvn clean install
rem call mvn source:jar install

cd ../cloud-framework-service
call mvn clean install
rem call mvn source:jar install

cd ../cloud-service-core
call mvn clean install
rem call mvn source:jar install

cd ../cloud-platform/platform-api
call mvn clean install
rem call mvn source:jar install

cd ../../cloud-common
call mvn clean install
rem call mvn source:jar install

cd ../cloud-script-sdk
call mvn clean install
rem call mvn source:jar install

cd ../cloud-permission-sdk
call mvn clean install
rem call mvn source:jar install

cd ../cloud-service-starter
call mvn clean install
rem call mvn source:jar install

cd ../cloud-platform/platform-service
call mvn clean install
rem call mvn source:jar install

cd ../second-service
call mvn clean install
rem call mvn source:jar install