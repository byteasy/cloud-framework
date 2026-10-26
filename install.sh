cd parent
mvn clean install
# mvn source:jar install

cd ../cloud-util
mvn clean install
# mvn source:jar install

cd ../cloud-service-api
mvn clean install
# mvn source:jar install

cd ../cloud-framework-vertx
mvn clean install
# mvn source:jar install

cd ../cloud-framework-service
mvn clean install
# mvn source:jar install

cd ../cloud-service-core
mvn clean install
# mvn source:jar install

cd ../cloud-platform/platform-api
mvn clean install
# mvn source:jar install

cd ../../cloud-common
mvn clean install
# mvn source:jar install

cd ../cloud-script-sdk
mvn clean install
# mvn source:jar install

cd ../cloud-permission-sdk
mvn clean install
# mvn source:jar install

cd ../cloud-mq-restfull
mvn clean install

cd ../cloud-service-starter
mvn clean install
# mvn source:jar install
