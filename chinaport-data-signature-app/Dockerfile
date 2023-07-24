FROM azul/zulu-openjdk:17-jre
MAINTAINER weasley
WORKDIR /app
COPY target/chinaport-data-signature-*.jar /app/chinaport-data-signature.jar
EXPOSE 8080
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ >/etc/timezone

ENV JAVA_OPTS=""
ENV SPRING_ARGS=""
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTIONS} -jar chinaport-data-signature.jar ${SPRING_ARGS}"]
