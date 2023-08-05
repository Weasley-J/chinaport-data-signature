# 中国电子口岸加签服务的数据模型

[![Maven Central](https://img.shields.io/maven-central/v/io.github.weasley-j/chinaport-data-signature-data-model)](https://search.maven.org/artifact/io.github.weasley-j/chinaport-data-signature-data-model)

> 海关报文加签的数据模型，方便各方引入依赖。

## 1 目前已实现的数据模型

### 1.1 海关 179 数据抓取报文

### 1.2 海关总署`xml`报文

进口单：CEB311Message, CEB621Message,

出口单:  CEB303Message



## 2 数据模型的设计拓展

### 2.1 `AbstractCebMessage`拓展

> 用来接收各类进口单、出口单的底层数据模型对象

![image-20230728135612365](https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/image-20230728135612365.png)

### 2.2 `IMessageType`拓展

> 获取各类进口单、出口单消息类型

![image-20230728135844727](https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/image-20230728135844727.png)

### 2.3 数据上报客户端设计

![image-20230728140512291](https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/image-20230728140512291.png)

### 2.4 基于`AbstractCebMessage`和`ChinaEportReportClient#report`设计所支持的报文类型

> 此种设计理论上支持海关的所有进出口单的数据模型

**样例报文**:

```tex
├── 出口样例
│   ├── CEB213Message.xml
│   ├── CEB214Message .xml
│   ├── CEB215Message.xml
│   ├── CEB216Message.xml
│   ├── CEB303Message.xml
│   ├── CEB304Message.xml
│   ├── CEB403Message.xml
│   ├── CEB404Message.xml
│   ├── CEB505Message.xml
│   ├── CEB506Message.xml
│   ├── CEB507Message.xml
│   ├── CEB508Message.xml
│   ├── CEB509Message.xml
│   ├── CEB510Message.xml
│   ├── CEB603Message.xml
│   ├── CEB604Message.xml
│   ├── CEB605Message.xml
│   ├── CEB606Message.xml
│   ├── CEB607Message.xml
│   ├── CEB608Message.xml
│   ├── CEB701Message.xml
│   ├── CEB702Message.xml
│   └── CEB792Message.xml
└── 进口样例
    ├── CEB311Message.xml
    ├── CEB312Message.xml
    ├── CEB411Message.xml
    ├── CEB412Message.xml
    ├── CEB511Message.xml
    ├── CEB512Message.xml
    ├── CEB513Message.xml
    ├── CEB514Message.xml
    ├── CEB621Message.xml
    ├── CEB622Message.xml
    ├── CEB623Message.xml
    ├── CEB624Message.xml
    ├── CEB625Message.xml
    ├── CEB626Message.xml
    ├── CEB711Message.xml
    └── CEB712Message.xml
```



