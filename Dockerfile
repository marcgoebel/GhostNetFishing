# Stage 1: Build with Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Deploy on WildFly (Full Jakarta EE Server)
FROM quay.io/wildfly/wildfly:31.0.0.Final-jdk17

# Add MySQL driver as a module
USER root
RUN mkdir -p /opt/jboss/wildfly/modules/com/mysql/main
COPY --from=build /app/target/ghostnetfishing.war /opt/jboss/wildfly/standalone/deployments/ghostnetfishing.war

USER jboss
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
