FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
