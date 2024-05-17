FROM openjdk:21

COPY target/FX-Deals-Task-0.0.1-SNAPSHOT.jar FX-Deals-Task.jar

ENTRYPOINT ["java", "-jar", "FX-Deals-Task.jar"]