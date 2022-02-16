# 中国电子口岸CEBXxxMessage进口单报文加签

> China E-Port data signature，中国电子口岸（海口海关CEBXxxMessage）末三段加签服务，开箱即用，无任何中间件。本项目遵守`GNU 3.0`协议。本项目里面所有到的`x509`证书不具有任何真实性、合法性，企业根据自己的情况替换成自己真实的证书，`ukey`加签直接直接下载`release`运行修改参数运行既可。不需要导出`.cer`证书



## 1 软件运行概况

todo





## 2 运行参数配置

todo

## 3 启动脚本介绍

本人已提供好`Windows`环境、`Linux`/`MacOS`的环境下的脚本，企业只需要修改**2**个参数既可**开箱即用**。

启动文件相对的位置:

![image-20220214002812928](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220214002812928.png)

#### 3.1 Windows环境

> 运行方式：双击打开

```bat
@ECHO OFF
SETLOCAL EnableDelayedExpansion

REM ------ 全局参数配置 ------

REM 以下3个参数: .cer证书没方法jar包里面可以不用
REM APP_NAME: jar包名称
REM UKEY_CERT_PATH: .cer证书在classpath下的相对路径，直接下载release运行的同学不需要配置
REM UKEY_HOST: 中国电子口岸操作员u-key所连接的Windows PC的主机ip
REM UKEY_PASSWORD: 中国电子口岸操作员u-key密码，默认: 88888888

set "APP_NAME=chinaport-data-signature-v1.0.0"
set "UKEY_CERT_PATH=cert/01691fe9.cer"
set "UKEY_HOST=192.168.31.108"
set "UKEY_PASSWORD=88888888"

REM ------ 全局参数配置 ------

title %APP_NAME%

PUSHD %~DP0 & cd /d "%~dp0"
%1 %2
mshta vbscript:createobject("shell.application").shellexecute("%~s0","goto :runas","","runas",1)(window.close)&goto :eof
:runas

set "APP=%CD%\%APP_NAME%.jar"
set "JAVA=%CD%\jre\bin\java.exe"
set "JVM_ARGS=-Dfile.encoding=GBK -Xms512m -Xmx512m"

%JAVA% %JVM_ARGS% -jar %APP% ^
--server.port=8080 ^
--eport.signature.ukey.cert-path=%UKEY_CERT_PATH% ^
--eport.signature.ukey.ws-url=ws://%UKEY_HOST%:61232 ^
--eport.signature.ukey.password=%UKEY_PASSWORD%

echo 按任意键退出.
pause >nul
exit
```

#### 3.2 Linux/MacOS环境

> 运行方式：终端执行`sh start.sh`启动程序

```bash
#!/bin/bash

# ------ 全局参数配置 ------

# 参数说明:
# APP_NAME: jar包名称
# UKEY_CERT_PATH: .cer证书在classpath下的相对路径，直接下载release运行的同学不需要配置
# UKEY_HOST: 中国电子口岸操作员u-key所连接的Windows PC的主机ip
# UKEY_PASSWORD: 中国电子口岸操作员u-key密码，默认: 88888888

APP_NAME="chinaport-data-signature-v1.0.0"
UKEY_CERT_PATH="cert/01691fe9.cer"
UKEY_HOST="192.168.31.108"
UKEY_PASSWORD="88888888"

# ------ 全局参数配置 ------

APP="./${APP_NAME}.jar"
JVM_ARGS="-Xms512m -Xmx512m"

java ${JVM_ARGS} -jar ${APP} \
  --server.port=8080 \
  --eport.signature.ukey.cert-path=${UKEY_CERT_PATH} \
  --eport.signature.ukey.ws-url=ws://${UKEY_HOST}:61232 \
  --eport.signature.ukey.password=${UKEY_PASSWORD}
```
