FROM majiajue/jdk1.8:latest
ARG JAR_FILE
ADD ${JAR_FILE} kdf_web.jar
COPY SIMSUN.ttf /usr/share/fonts/simsun.ttf
EXPOSE 10000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/kdf_web.jar"]