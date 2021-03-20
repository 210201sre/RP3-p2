FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR /application
COPY pom.xml pom.xml
COPY src/ src/
RUN mvn clean package
ARG JAR_FILE=target/*.jar

FROM adoptopenjdk:11-jre-hotspot as runner
WORKDIR /application
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract


FROM adoptopenjdk:11-jre-hotspot
WORKDIR /application
COPY --from=runner /application/dependencies/ ./
COPY --from=runner /application/snapshot-dependencies/ ./
COPY --from=runner /application/spring-boot-loader/ ./
COPY --from=runner /application/application/ ./
EXPOSE 8090

ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher" ]