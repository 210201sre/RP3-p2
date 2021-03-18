FROM maven:3.6.3-openjdk-8 as builder

COPY pom.xml pom.xml
COPY src/ src/

RUN mvn clean package

ARG JAR_FILE=target/project-one-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

RUN java -Djarmode=layertools -jar app.jar extract

#FROM java:8 as runner
FROM maven:3.6.3-openjdk-8
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

EXPOSE 8090

#COPY --from=builder target/project-one-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher" ]