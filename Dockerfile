FROM maven:3.6.3-openjdk-8 as builder
# WORKDIR /application
COPY pom.xml pom.xml
COPY src/ src/
RUN mvn clean package
ARG JAR_FILE=target/*.jar

# FROM maven:3.6.3-openjdk-11 as runner
# WORKDIR /application
# COPY ${JAR_FILE} application.jar
# RUN java -Djarmode=layertools -jar application.jar extract


FROM maven:3.6.3-openjdk-8
# WORKDIR /application
# COPY --from=builder /application/dependencies/ ./
# COPY --from=builder /application/snapshot-dependencies/ ./
# COPY --from=builder /application/spring-boot-loader/ ./
# COPY --from=builder /application/application/ ./
EXPOSE 8080


COPY --from=builder target/project-one-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar","app.jar"]