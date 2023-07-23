#!/bin/bash
docker stop chinaport-data-signature && docker rm -f chinaport-data-signature
docker rmi -f weasleyj/chinaport-data-signature:latest
docker build -t weasleyj/chinaport-data-signature:latest . -f ./Dockerfile
exit 0

# JVM 参数
JAVA_OPTS="-Xmx512m -Xms512m"
# 项目启动的Spring-Boot 启动命令行配置参数
SPRING_ARGS="
--spring.profiles.active=prod \
--spring.mail.enable=false \
--spring.mail.to='abc@qq.com' \
--spring.mail.cc='abc@outlook.com,abc@qq.com' \
--spring.mail.host='smtp.189.cn' \
--spring.mail.port=465 \
--spring.mail.username='xxx@189.cn' \
--spring.mail.password='' \
--spring.mail.protocol=smtp \
--spring.mail.properties.mail.smtp.ssl.enable=true \
--spring.mail.properties.mail.debug=false \
--eport.signature.ukey.ws-url=ws://42.120.12.245:6232 \
--eport.signature.ukey.password=88888888 \
--eport.signature.auth.enable=off \
--eport.signature.auth.token=DefaultAuthToken \
--eport.signature.report.ceb-message.cop-code='' \
--eport.signature.report.ceb-message.cop-name='' \
--eport.signature.report.ceb-message.dxp-id='' \
--eport.signature.report.ceb-message.server='null' \
--eport.signature.report.customs179.ebp-code='' \
--eport.signature.report.customs179.server='null'"
docker run --name chinaport-data-signature --restart=always \
  -p 8080:8080 \
  -e TZ=Asia/Shanghai \
  -e JAVA_OPTS="${JAVA_OPTS}" \
  -e SPRING_ARGS="${SPRING_ARGS}" \
  -d weasleyj/chinaport-data-signature
