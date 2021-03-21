FROM maven:3.6.3-openjdk-11 as builder
WORKDIR /application
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY .mvn .mvn
COPY pom.xml pom.xml
COPY src/ src/
RUN chmod +x mvnw
RUN ./mvnw clean package
ARG JAR_FILE=target/*.jar

FROM maven:3.6.3-openjdk-11 as runner
WORKDIR /application
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract


FROM maven:3.6.3-openjdk-11
WORKDIR /application
COPY --from=runner /application/dependencies/ ./
COPY --from=runner /application/snapshot-dependencies/ ./
COPY --from=runner /application/spring-boot-loader/ ./
COPY --from=runner /application/application/ ./
EXPOSE 8090

ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher" ]