FROM eclipse-temurin:17

COPY target/*.jar configserver.jar

ENTRYPOINT ["java","-jar","/configserver.jar"]

EXPOSE 9296