FROM maven:3.9.9-eclipse-temurin-17 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /usr/app/target/api-1.0.jar /app/api.jar
CMD ["sh", "-c","sleep 10 && java -Dspring.config.location=classpath:/application.properties  -jar api.jar"]