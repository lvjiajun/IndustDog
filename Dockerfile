FROM openjdk:14-alpine
COPY target/cloudjumper-*.jar cloudjumper.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "cloudjumper.jar"]