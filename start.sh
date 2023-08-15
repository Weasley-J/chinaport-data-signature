#!/bin/bash

# Environment args
APP="chinaport-data-signature-webapp"
UKEY_HOST="127.0.0.1"
UKEY_PASSWORD="88888888"
JVM_ARGS="-Xms1g -Xmx1g"

java -Dfile.encoding=UTF-8 ${JVM_ARGS} -jar ${APP}.jar \
  --server.port=8080 \
  --spring.profiles.active=prod \
  --eport.signature.ukey.ws-url=ws://${UKEY_HOST}:61232 \
  --eport.signature.ukey.password=${UKEY_PASSWORD} \
  --spring.mail.enable=false \
  --spring.mail.to=abc@qq.com \
  --spring.mail.cc=abc@outlook.com,cbd@qq.com \
  --spring.mail.host=smtp.189.cn \
  --spring.mail.port=465 \
  --spring.mail.username=xxx@189.cn \
  --spring.mail.password=your_password \
  --spring.mail.protocol=smtp \
  --spring.mail.properties.mail.smtp.ssl.enable=true \
  --spring.mail.properties.mail.debug=false
