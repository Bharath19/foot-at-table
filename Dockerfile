FROM openjdk:11
WORKDIR /app
ARG JAR_FILE
COPY ./${JAR_FILE} /app/countrybroot.jar
ENTRYPOINT ["java","-jar","/app/countrybroot.jar"]