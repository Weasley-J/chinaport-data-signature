# 中国电子口岸海关总署XML报文&海关179数据加签

> **China e-port data signature**
>
>
>
> 中国电子口岸海关总署[海口海关CEBXxxMessage]`XML末三段报文`和`海关179数据抓取报文`加签服务，开箱即用，无任何中间件；本项目遵守`GNU 3.0`协议，本项目里面所有到的`x509`证书不具有任何真实性、合法性，企业根据自己的情况替换成自己真实的证书，`ukey`加签直接直接下载`release`运行修改参数运行既可，不需要导出`.cer`证；
>
>
>
> 使用业务场景：中国境内的跨进电商业务



<u>在第一次提交代码之前，地球上的搜索引擎在那个时间点[**2022-02-14**]之前还找不到任何有参价值的信息......</u>

<table>
    <tr>
        <td><img src="http://tva1.sinaimg.cn/large/007rXfoIly1gh72bl7bcqj30u00u0tb8.jpg" border=0 alt=""></td>
    </tr>
    <tr>
        <td><img src="https://img.aidotu.com/down/jpg/20201218/57240fd08e72b5b1c551b58cc9e4c2c5_7043_240_192.jpg"
                 border=0 alt=""></td>
        <td><img src="https://img.aidotu.com/down/png/20200928/e69d844afd0d05d573a6621064961169_32283_200_200.png" border=0 alt=""></td>
    </tr>
</table>

- **Previews of China e-port data signature**

项目开始前我们先了解下海关总署XML报文的加签都在什么价位？顺便说下`海关179数据抓取`加签直接按要求拼参数即可，像百度、CSDN这种垃圾桶里面随处都有。**但唯独没有XML的加签，**
2022-02-14之前。可见这XML报文加签在这之前有几毛钱的商业价值在里面，细品下图:

<img src="https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/IMG_0373-side.jpg" alt="IMG_0373-side">
海关总署XML价位</img>



<img src="https://img.aidotu.com/down/jpg/20200928/90df7eba2b2552e589790c2f5ca3cb37_54589_640_640.jpg"/>



**开始进入正题 》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》**

## 1 `sonarqube`代码审查结果

![image-20220218023508803](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220218023508803.png)

## 2 软件运行概况

| 序号 | 类目             | 版本                                                         | 备注                 |
| ---- | ---------------- | ------------------------------------------------------------ | -------------------- |
| 1    | 软件运行环境     | [Java SE Development Kit 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)，jdk17 | 基础软件最低运行要求 |
| 2    | 硬件运行环境     | Windows-10-x64/Windows-11-x64/Windows-server-x64-2007+/Linux-x86-x64(自行解决内网穿透问题，**
ukey只能插在Windows电脑上**) |                      |
| 3    | 硬件性能要求     | > =1核CPU+2G运行内存                                          | 不能比这小了         |
| 4    | 对技术人员的要求 | 会把电子口岸的ukey插入安装好[电子口岸控件]([中国国际贸易单一窗口登录管理 (singlewindow.cn)](https://app.singlewindow.cn/cas/login?service=http%3A%2F%2Fwww.singlewindow.cn%2Fsinglewindow%2Flogin.jspx))Windows电脑，会改启动脚本里面的参数，会用鼠标双击启动脚本 |                      |

## 3 运行参数概况

| 参数名                           | 备注                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| `eport.signature.ukey.cert-path` | u-key的`.cer`证书在`classpath`的相对路径，如: `cert/01691fe9.cer`,提示：下载`release`直接运行的不管用这个参数。 |
| `eport.signature.ukey.ws-url`    | u-key做插`Windows`电脑的`socket`链接`url`,如: `ws://127.0.0.1:61232`,下载`release`直接运行的修改全局配置的`UKEY_HOST`即可 |
| `eport.signature.ukey.password`  | u-key密码的密码，默认: `88888888`, 如果密码改过，需要指定下，下载`release`直接运行的修改全局配置的`UKEY_PASSWORD`即可 |

其他，本应用是标准的`springboot`应用，支持`springboot`的所有配置参数。

## 4 启动脚本介绍

本人已提供好`Windows`环境、`Linux`/`MacOS`的环境下的脚本，企业只需要修改**2**个参数既可**开箱即用**。

启动文件相对的位置:

![image-20220214002812928](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220214002812928.png)

#### 4.1 Windows环境

> 运行方式：鼠标双击打开

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

#### 4.2 Linux/MacOS环境

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

## 5 软件架构

| 类目                           | 版本        | 备注                                                         |
| ------------------------------ | ----------- | ------------------------------------------------------------ |
| Jdk17                          | 17.0.2 LTS  |                                                              |
| spring-boot-starter-parent     | 3.0.0-M1    |                                                              |
| spring-cloud                   | 2022.0.0-M1 |                                                              |
| smart-doc                      | 2.3.7       | 一个自动化的API文档输出工具，还在用word、rap2、yapi、swagger? 该更新技术栈了。 |
| sonar                          | 3.9.0.2155  | 一款开源的代码质量分析工具，能编码是规避很多潜在bug          |
| spring-boot-starter-validation | 3.0.0-M1    | spring新一代的参数校验器，替代javax JSR303的工具             |

单体架构，可直接开箱即用，也可与分布式微服务集成，可拔插注入配置中心，通过远程`RPC`调用加签接口也是可以的。

## 6 开发文档

启动项目，浏览器访问: http://127.0.0.1:8080

![image-20220217013840347](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220217013840347.png)

![image-20220218212801220](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220218212801220.png)

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

**海关179加签返回示例**

提示：179加签只需要两个字即可

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





