FROM maven:3.6.3-openjdk-11 AS maven_build

ENV MAVEN_VERSION 3.6.3


ENV MAVEN_HOME /usr/share/maven

COPY . /data/kalaha
WORKDIR /data/kalaha

RUN ["mvn", "clean", "install"]

EXPOSE 8080

CMD ["java", "-jar", "target/kalaha-0.0.1-SNAPSHOT.jar"]