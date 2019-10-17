FROM openjdk:8u191-jre-alpine3.9
VOLUME ["/tmpdocker","/data"]
ARG JAR_FILE
ADD ${JAR_FILE} kdf_etl_boot.jar
EXPOSE 9000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/kdf_etl_boot.jar"]