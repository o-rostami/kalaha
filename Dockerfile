FROM maven:3.6.3-adoptopenjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests
FROM adoptopenjdk:11.0.9_11-jdk-openj9-0.23.0
ENV JAVA_OPTS ""
COPY --from=build /usr/src/app/target/kalaha-app.jar /usr/app/kalaha-app.jar
EXPOSE 8080
CMD exec java $JAVA_OPTS -jar /usr/app/kalaha-app.jar $0 $@