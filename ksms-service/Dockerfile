FROM maven:3.9.2-eclipse-temurin-20 AS buildOne
ARG PROFILE_NAME
WORKDIR /app
COPY . /app
RUN echo "Java build profile: $PROFILE_NAME"
RUN mvn clean package -P$PROFILE_NAME

FROM eclipse-temurin:20.0.1_9-jdk-jammy AS buildTwo
ARG PROFILE_NAME
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar
COPY --from=buildOne ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:20.0.1_9-jdk-jammy
ARG PROFILE_NAME
WORKDIR /app
COPY --from=buildTwo /app/dependencies/ ./
COPY --from=buildTwo /app/snapshot-dependencies/ ./
COPY --from=buildTwo /app/spring-boot-loader/ ./
COPY --from=buildTwo /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "-Dspring.profiles.active=$PROFILE_NAME"]