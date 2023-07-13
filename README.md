# **China E-Port Data Signature**

中国电子口岸海关总署`XML`报文&海关179数据上报加签服务，提供一站式解决方案架构，开箱即用，无任何中间件；本项目遵守`GNU 3.0`
协议，本项目测试用例中用到的企业信息已经过企业的批准，企业使用`ukey`加签进行业务申报时, 请下载`release`
文件根据自己的实际情况适当修改运行参数既可。

> 支持:
>
> - `CEBXxxMessage.xml`进出口报文
> - `海关179号公告`数据抓取报文
>
> 业务场景:
>
> - 中国境内的跨进电商业务, 进出口贸易
>
> 提示:
>
> - 单体应用直接下载[release](https://github.com/Weasley-J/chinaport-data-signature/releases)的二进制文件使用即可（使用较多）
> - 分布式微服务请切换到`dev-microservice`分支构建（较少使用）



<u>决定将此项目开源项目的时候，地球上的搜索引擎在**2022-02-14**
之前还搜索不到任何有价值的信息，ChatGPT在面对这种付费的解决方案时，它能解决吗.....</u>

<table>
    <tr>
        <td><img src="https://tva1.sinaimg.cn/large/007rXfoIly1gh72bl7bcqj30u00u0tb8.jpg" border=0 alt="" height="200"></td>
        <td><img src="https://img.aidotu.com/down/jpg/20201218/57240fd08e72b5b1c551b58cc9e4c2c5_7043_240_192.jpg" border=0 alt="" height="200"></td>
        <td><img src="https://img.aidotu.com/down/png/20200928/e69d844afd0d05d573a6621064961169_32283_200_200.png" border=0 alt="" height="200"></td>
    </tr>
</table>

- **Previews of China e-port data signature**

项目开始前我们先了解下海关总署XML报文的加签都在什么价位？百度、CSDN里你能搜索到的仅一个海关179号文件对接，**但唯独没有XML的加签**，细品下图:

<img src="https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/IMG_0373-side.jpg" alt="IMG_0373-side"/>

你没看错：你去找乙方对接，乙方不告诉你加签用到的算法，需要接入方自己开发。好，那就开发吧！

- [x] 电子口岸的操作员`u-key`长啥样:

![IMG_0401](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/IMG_0401.jpg)

- [x] 启动项目：`chinaport-data-signature`

![image-20220219001115869](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220219001115869.png)

我这是在启动脚本里指定了`u-key`的`ip`为我与我`MacBookPro`处于统一局域网下面的`Windows`电脑的主机`ip`，`Windows`
上你使用`git bash`也能达到我终端这样的效果（`Windows`小伙伴注意下这里，我这是通过[Git Bash](https://gitforwindows.org/)
启动的，`Windows`终端汉字会乱码，你可以查看`log`文件避开汉字乱码的问题）。

![image-20220219162400840](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220219162400840.png)

## 1  项目代码质量

![image-20220218023508803](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220218023508803.png)

## 2 软件运行概况

| 序号 | 类目       | 版本                                                                                                                                                                                                       | 备注         |
|----|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| 1  | 软件运行环境   | [Java SE Development Kit 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)                                                                                               | 基础软件最低运行要求 |
| 2  | 硬件运行环境   | `Windows-10-x64`/`Windows-11-x64`/`Windows-server-x64`/`Linux`(自行解决内网穿透问题,ukey只能插Windows电脑上)                                                                                                             |            |
| 3  | 硬件性能要求   | > =1核CPU+2G运行内存                                                                                                                                                                                          | 不能比这小了     |
| 4  | 对技术人员的要求 | 会把电子口岸ukey插入安装好[电子口岸控件]([中国国际贸易单一窗口登录管理 (singlewindow.cn)](https://app.singlewindow.cn/cas/login?service=http%3A%2F%2Fwww.singlewindow.cn%2Fsinglewindow%2Flogin.jspx))Windows电脑上，会改启动脚本里面的参数，会用鼠标双击启动脚本 |            |

## 3 运行参数配置

> 本应用是标准的`springboot`应用，支持`springboot`
> 的所有配置参数，推荐通过在[线`yaml`转`properties`](https://www.toyaml.com/index.html)将配置元数据复制到命令行参数中按需使用,

- [推荐元数据配置示例](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/resources/application-dev.yml#L43-L71)

- [加签异常邮件通知配置示例](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/resources/application-dev.yml#L23-L41)

| 配置参数                                             | 是否必须（Y/N） | 解释                                                                                                                                                                                                                            |
|--------------------------------------------------|-----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| eport.signature.algorithm                        | N         | [XML签名的算法类型](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/java/cn/alphahub/eport/signature/config/SignatureAlgorithmProperties.java#L20)，不指定时使用程序自动推断的算法, 推荐使用程序自动推断 |
| eport.signature.ukey.ws-url                      | Y         | u-key做插`Windows`电脑的`socket`链接`url`,如: `ws://127.0.0.1:61232`,下载`release`直接运行的修改全局配置的`UKEY_HOST`即可                                                                                                                             |
| eport.signature.ukey.password                    | N         | u-key密码的密码，默认: `88888888`, 如果密码改过，需要指定下，下载`release`直接运行的修改全局配置的`UKEY_PASSWORD`即可                                                                                                                                              |
| eport.signature.ukey.health.endpoint.client-name | N         | Windows上重启的ukey可执行文件全限定文件名称，不指定将自动查找                                                                                                                                                                                          |
| eport.signature.auth.enable                      | N         | [是否启用token鉴权](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/java/cn/alphahub/eport/signature/config/AuthenticationProperties.java#L17)，取值：on/off                      |
| eport.signature.auth.token                       | N         | 客户端请求鉴权token, 默认值: DefaultAuthToken, [请求头](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/java/cn/alphahub/eport/signature/config/AuthenticationProperties.java#L24)   |
| eport.signature.report.ceb-message.cop-code      | Y         | [电子口岸XML报文](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/java/cn/alphahub/eport/signature/config/ChinaEportProperties.java#L20)传输企业代码                                |
| eport.signature.report.ceb-message.cop-name      | Y         | 传输企业名称，报文传输的企业名称                                                                                                                                                                                                              |
| eport.signature.report.ceb-message.dxp-id        | Y         | 文传输编号，向中国电子口岸数据中心申请数据交换平台的用户编号                                                                                                                                                                                                |
| eport.signature.report.ceb-message.server        | N         | [海关服务器地址](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/java/cn/alphahub/eport/signature/config/ChinaEportProperties.java#L43)，缺省则采用Client中的密文作文默认Server URL          |
| eport.signature.report.customs179.ebp-code       | Y         | [海关 179 数据上报](https://github.com/Weasley-J/chinaport-data-signature/blob/main/chinaport-data-signature-app/src/main/java/cn/alphahub/eport/signature/config/Customs179Properties.java#L24)的电商平台代码                             |
| eport.signature.report.customs179.server         | N         | 数据上报服务器URL地址，链接格式: https://域名联系海关/ceb2grab/grab/realTimeDataUpload，没有配置的话采用项目内置的URL密文地址                                                                                                                                       |

## 4 项目启动脚本

#### 4.1 Windows环境

[正确配置方式](https://github.com/Weasley-J/chinaport-data-signature/blob/main/%E4%BD%BF%E7%94%A8GitBash%E5%8F%B3%E9%94%AE%E8%BF%90%E8%A1%8C%E5%90%AF%E5%8A%A8%E8%84%9A%E6%9C%AC.md)

#### 4.2 Linux/MacOS环境

> 运行方式：终端执行`sh start.sh`启动程序

```bash
#!/bin/bash

# ------ 全局参数配置 start------
# 参数说明:
# APP_NAME: jar包名称
# UKEY_HOST: 中国电子口岸操作员u-key所连接的Windows PC的主机ip
# UKEY_PASSWORD: 中国电子口岸操作员u-key密码，默认: 88888888

#
# 邮件通知配置，默认：不启用
#
#spring.mail.enable=false
#spring.mail.to=主收件人邮箱
#spring.mail.cc=抄送箱邮（非必填）,抄送多个收件人箱邮用","隔开
#
# 以下发送邮件的客服端参数，使用哪个邮箱发出通知消息
#
#spring.mail.host=smtp.189.cn #邮件服务商主机
#spring.mail.port=465
#spring.mail.username=host_username
#spring.mail.password=host_password
#spring.mail.protocol=smtp #邮件协议
#spring.mail.properties.mail.smtp.ssl.enable=true
#spring.mail.properties.mail.debug=false #是否开始debug模式，终端会打印日志
#
#eport.sign.ukey.health.endpoint.client-name='Windows上重启的ukey可执行文件SetAccessControl.exe的全限定文件名称'
# ------ 全局参数配置 end------

APP_NAME="chinaport-data-signature"
UKEY_HOST="127.0.0.1"
UKEY_PASSWORD="88888888"
JAVA_ARGS="-Xms1g -Xmx1g"

# ------ 全局参数配置 ------
java -Dfile.encoding=utf-8 ${JAVA_ARGS} -jar ${APP_NAME}.jar \
  --server.port=8080 \
  --spring.profiles.active=prod \
  --eport.signature.ukey.ws-url=ws://${UKEY_HOST}:61232 \
  --eport.signature.ukey.password=${UKEY_PASSWORD} \
  --spring.mail.enable=false \
  --spring.mail.to='abc@qq.com' \
  --spring.mail.cc='abc@outlook.com,cbd@qq.com' \
  --spring.mail.host='smtp.189.cn' \
  --spring.mail.port=465 \
  --spring.mail.username='xxx@189.cn' \
  --spring.mail.password='your_password' \
  --spring.mail.protocol=smtp \
  --spring.mail.properties.mail.smtp.ssl.enable=true \
  --spring.mail.properties.mail.debug=false

```

## 5 软件架构

| 类目                             | 版本          | 备注                                                  |
|--------------------------------|-------------|-----------------------------------------------------|
| JDK17                          | 17.0.7 LTS  |                                                     |
| spring-boot                    | 3.1.1       |                                                     |
| spring-cloud                   | 2022.0.0-M1 |                                                     |
| smart-doc                      | 2.3.7       | 一个自动化的API文档输出工具，还在用word、rap2、yapi、swagger? 该更新技术栈了。 |
| sonar                          | 3.9.0.2155  | 一款开源的代码质量分析工具，能编码是规避很多潜在bug                         |
| spring-boot-starter-validation | 3.0.0-M1    | spring新一代的参数校验器，替代javax JSR303的工具                   |

单体架构，可直接开箱即用，也可与分布式微服务集成，可拔插注入配置中心，通过远程`RPC`调用加签接口也是可以的。

## 6 开发文档

启动项目，浏览器访问: http://127.0.0.1:8080 ,
会打开以下界面[接口文档](https://github.com/Weasley-J/chinaport-data-signature/blob/main/中国电子口岸报文加签接口文档.md)
的H5版本，可直接在页面给ukey发送请求.

![image-20220217013840347](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220217013840347.png)

![image-20220218212801220](https://alphahub-test-bucket.oss-cn-shanghai.aliyuncs.com/image/image-20220218212801220.png)

- **XML加签响应示例**

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

- **海关179加签返回示例**

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

感谢您使用本开源项目！为了持续改进并为更多企业提供免费的加签解决方案，我们诚恳地邀请您考虑赞助支持我们的项目。您的慷慨赞助将直接帮助我们维护和改进该项目，使其能够为更多企业节省成本、提高效率。

## 7 支持赞助

### 7.1 为什么赞助

- **降低企业成本**：我们的项目为企业提供了一站式解决方案，摆脱了每年支付高额费用购买加签设备的负担。您的赞助可以帮助更多企业降低经营成本，提高竞争力。
- **促进技术共享**：通过开源，我们希望能够促进技术共享和合作。您的赞助将鼓励我们继续开发和分享更多优质的解决方案，使得整个行业都能从中受益。
- **支持项目发展**：我们致力于不断改进和完善该项目，包括修复漏洞、添加新功能和提供更好的文档支持。您的赞助将直接用于项目的开发、测试和维护，确保项目的持续稳定和可靠性。

### 7.2 如何赞助

我们非常感激您对我们项目的赞助支持！您可以通过以下方式进行赞助：

> 您可以根据自身能力自由选择捐赠金额，无论您选择捐赠的金额大小，我们都非常感激，您的每一份赞助都对本项目发展都起到了重要的作用。

- **捐赠**：您可以通过以下链接进行捐赠: 

  - [PayPal](https://www.paypal.me/shiwenjinga)

  - <div style="display: flex; flex-direction: row;">
    <div>
      <img src="https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/image-20230713012837745.png" alt="AliPay" width="300" height="300">
      <p align="center">AliPay</p>
    </div>
    <div>
      <img src="https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/image-20230713013148459.png" alt="WeChatPay" width="300" height="300">
      <p align="center">WeChatPay</p>
    </div>
    </div>

    

- **技术贡献**：如果您是开发者，您也可以通过提交代码、报告问题或者提供改进建议等方式来支持我们的项目。

- **宣传推广**：如果您无法提供直接的赞助，您还可以通过在社交媒体上宣传、分享我们的项目，帮助我们扩大影响力。

### 7.3 感谢您的支持

我们衷心感谢您对我们项目的关注和支持！您的赞助将鼓励我们继续改进和提供更好的解决方案，造福更多的企业和开发者。如果您有任何问题或者疑问，请随时联系我们, 联系邮箱: 1432689025@qq.com



## 8 接入企业登记

感谢您对我们项目的赞助支持！为了更好地记录和感谢每一家接入企业，我们在此邀请您在捐赠时留下您的公司名，以便我们将您的公司列入接入企业名单中。您可以在捐赠备注中提供您的公司名，或者在捐赠后通过邮件等方式将公司名发送给我们。

您的公司名将在我们的 README 中特别标注，并列入接入企业名单，以展示对您的感激之情。同时，我们也欢迎您提供您公司的官方网站链接，以便其他用户了解更多关于您公司的信息。

感谢您的配合和支持，我们将竭诚为您提供更好的服务！



## 9 接入企业

> 排名不分先后，从2023-07-11开始登记

感谢以下公司对我们项目的赞助和支持：

- 海南省荣誉进出口贸易有限公司：[官方网站](http://www.hnzrjck.com/)
- ...

如果您的公司已赞助我们的项目，但在列表中没有找到，请及时与我们联系，我们将尽快添加您的公司名。
