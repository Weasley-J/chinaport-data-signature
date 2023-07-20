#  x86-64 架构的服务器或云平台
FROM openjdk:17-jdk
WORKDIR /app
COPY target/chinaport-data-signature-app.jar /app/
EXPOSE 8080
ENV JVM_OPTIONS="-Xmx512m -Xms256m"
ENV SPRING_ARGS="--spring.profiles.active=prod"
CMD ["java", "${JVM_OPTIONS}", "-jar", "chinaport-data-signature-app.jar","${SPRING_ARGS}"]
