# 中国电子口岸CEBXxxMessage进口单报文加签

> China E-Port data signature，中国电子口岸（海口海关CEBXxxMessage）末三段加签服务，开箱即用，无任何中间件；
>
> 本项目遵守`GNU 3.0`协议，本项目里面所有到的`x509`证书不具有任何真实性、合法性，企业根据自己的情况替换成自己真实的证书，`ukey`加签直接直接下载`release`运行修改参数运行既可，不需要导出`.cer`证书。

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

REM ------ 全局参数配置 ------

REM 参数说明:
REM APP_NAME: jar包名称
REM UKEY_CERT_PATH: .cer证书在classpath下的相对路径，直接下载release运行的同学不需要配置
REM UKEY_HOST: 中国电子口岸操作员u-key所连接的Windows PC的主机ip
REM UKEY_PASSWORD: 中国电子口岸操作员u-key密码，默认: 88888888

set "APP_NAME=chinaport-data-signature"
set "UKEY_CERT_PATH="
set "UKEY_HOST=127.0.0.1"
set "UKEY_PASSWORD=88888888"

REM ------ 全局参数配置 ------

title %APP_NAME%

PUSHD %~DP0 & cd /d "%~dp0"
%1 %2
mshta vbscript:createobject("shell.application").shellexecute("%~s0","goto :runas","","runas",1)(window.close)&goto :eof
:runas

set "APP=%CD%\%APP_NAME%"
set "JVM_ARGS=-Dfile.encoding=UTF-8 -Xms1g -Xmx1g"

java %JVM_ARGS% -jar %APP%.jar ^
--server.port=8080 ^
--spring.profiles.active=prod ^
--eport.signature.ukey.cert-path=%UKEY_CERT_PATH% ^
--eport.signature.ukey.ws-url=ws://%UKEY_HOST%:61232 ^
--eport.signature.ukey.password=%UKEY_PASSWORD%

echo 按人任意键退出.
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

APP_NAME="chinaport-data-signature"
UKEY_CERT_PATH=""
UKEY_HOST="127.0.0.1"
UKEY_PASSWORD="88888888"
JAVA_ARGS="-Xms1g -Xmx1g"

# ------ 全局参数配置 ------
java ${JAVA_ARGS} -jar ${APP_NAME}.jar \
  --server.port=8080 \
  --spring.profiles.active=prod \
  --eport.signature.ukey.cert-path=${UKEY_CERT_PATH} \
  --eport.signature.ukey.ws-url=ws://${UKEY_HOST}:61232 \
  --eport.signature.ukey.password=${UKEY_PASSWORD}
```

## 4 访问开发接口文档

浏览器输入: http://127.0.0.1:8080

![image-20220217013840347](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220217013840347.png)

![image-20220217013917388](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220217013917388.png)

**XML加签响应示例**

```json
{
    "message": "操作成功",
    "success": true,
    "timestamp": "2022-02-17 12:12:59",
    "code": 200,
    "data": {
        "success": true,
        "certNo": "01681fe8",
        "x509Certificate": "MIIE+TCCBGKgAwIBAgIEAWkf6TANBgkqhkiG9w0BAQUFADB8MQswCQYDVQQGEwJjbjEVMBMGA1UECh4MTi1W/XU1W1BT41y4MRUwEwYDVQQLHgyLwU5me6F0Bk4tX8MxDTALBgNVBAgeBFMXTqwxITAfBgNVBAMeGE4tVv11NVtQThpSoYvBTmZ7oXQGTi1fwzENMAsGA1UEBx4EUxdOrDAeFw0yMTA1MzEwMDAwMDBaFw0zMTA1MzEwMDAwMDBaMBwxDTALBgNVBAMeBH8qdDMxCzAJBgNVBBIeAgAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDt9T7JOQikKKL7oY7RWbRMp7y2VsrXIbFzSDh5DZQPimduUijZzZlK6AkZMDYJBM2/IJGI0QjWjBJVZ8hEUkgJb4UOOBCBM+cCGFCNY5LX/mAo5BoexgG4Kdr0TwxAQ+s3H85fCU81ROgqAS05/IzOR7eDSEYYT9CRmNYXIRTPVwIDAQABo4IC5jCCAuIwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgacGA1UdIwSBnzCBnIAU+XWjeEULQmjCBFOr68NOPlR4dGChgYCkfjB8MQswCQYDVQQGEwJjbjEVMBMGA1UECh4MTi1W/XU1W1BT41y4MRUwEwYDVQQLHgyLwU5me6F0Bk4tX8MxDTALBgNVBAgeBFMXTqwxITAfBgNVBAMeGE4tVv11NVtQThpSoYvBTmZ7oXQGTi1fwzENMAsGA1UEBx4EUxdOrIIBADAdBgNVHQ4EFgQU1eAz50lwZ6gidtkJNPIr7O0jKvUwQgYDVR0gBDswOTA3BgYrgQcBAQIwLTArBggrBgEFBQcCARYfaHR0cDovL2Nwcy5jaGluYXBvcnQuZ292LmNuL0NQUzByBgNVHR8EazBpMDCgLqAshipsZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS8wMDAtMS5jcmwwNaAzoDGGL2h0dHA6Ly9sZGFwLmNoaW5hcG9ydC5nb3YuY246ODA4OC9kemthMDAwLTEuY3JsMG0GCCsGAQUFBwEBBGEwXzAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuY2hpbmFwb3J0Lmdvdi5jbjo4ODAwLzAtBggrBgEFBQcwAYYhaHR0cDovL29jc3AuY2hpbmFwb3J0Lmdvdi5jbjo4MDg4MCoGCisGAQQBqUNkBQEEHBYauqPEz9DHtLS7pcGqzfjSvdKp09DP3rmry74wEgYKKwYBBAGpQ2QFAwQEFgIwMTAiBgorBgEEAalDZAUIBBQWEjMxMDEwMjE5ODAwNzI0MzIxNDAdBgorBgEEAalDZAUJBA8WDUpKMEc5MDAxNTkyNzIwGQYKKwYBBAGpQ2QFCwQLFglNQTVUTFVBNTMwEgYKKwYBBAGpQ2QFDAQEFgIwMDASBgorBgEEAalDZAIEBAQWAjE0MBIGCisGAQQBqUNkAgEEBBYCMTIwDQYJKoZIhvcNAQEFBQADgYEAqSuOMxAzM4bXvdlDcE6fODsCvQMFKctlA+LCllFQwl58HaBmcWx4T/ddKF9LBYc8A986LlcUw6Mkwxraj2WO+meXdDzRLEO8t3gyZk7tYp5aneV4zGYUsphwyMdLt8N8o4kVgg16bQy43XgA1jRMv8nvhb908IqQQBxwv0SIuXU=",
        "digestValue": "b2FtM/jHvW4fnJpMMPWk/177RwA=",
        "signatureValue": "mhWpJI3RsJZGiR8MqI3kcJAJPqgVOQi0IFJzp0CBWLG+Qa7ZnYJX+hi70J+/09diHYkCMDxcKuT5uogbFvi/9rOZnrn+YsL0h0/nGcO31lx/RpqT8i5GoSt/UcZe9COIbnrKwdh+90FLYIW4xS3Rsglv4CDL+e53GGmSdduSTUU="
    }
}
```

![image-20220217014013410](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220217014013410.png)

**海关179返回示例**

```json
{
    "message": "操作成功",
    "success": true,
    "timestamp": "2022-02-17 01:44:49",
    "code": 200,
    "data": {
        "success": true,
        "certNo": "01681fe8",
        "signatureValue": "GV+t/IwqDZFL0K44tv8OWfiiEp6RhxtT0/c7OjrmboaQiYuQaSXZfwsAgqdLA4sU/gInK5K5cs9/wxG+E4hW8nL4UslsLVFmTQXtW4I3p2DitnovHAbqELU4/vetzLQeOPTXUOFTBEUU9fc9C6yAPJlDuz13f9QjwsmdFniW/yU="
    }
}
```

