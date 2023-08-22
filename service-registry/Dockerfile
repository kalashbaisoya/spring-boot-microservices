FROM eclipse-temurin:17

COPY target/*.jar serviceregistry.jar

ENTRYPOINT ["java","-jar","/serviceregistry.jar"]

EXPOSE 8761