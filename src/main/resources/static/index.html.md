# chinaport-data-signature
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
1.0|2022-02-16 10:30|创建|Weasley J|创建api文档
1.1|2023-06-11 15:36|创建|Weasley J|更新api文档



## Ukey健康Controller
### ukey健康指令
**URL:** http://localhost:8080/rpc/ukey/health/endpoint/{command}

**Type:** POST

**Author:** lwj

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** <ul><br><b>支持command类型</b><br><li>RESTART:重启控件(较多使用)</li><br><li>START: 启动控件</li><br><li>STOP: 停止控件</li><br><li>REPAIR: 修复证书</li><br></ul>

**Request-example:**
```
curl -X POST -i http://localhost:8080/rpc/ukey/health/endpoint/
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
message|string|返回消息|-
success|boolean|是否成功|-
timestamp|string|响应时间戳|-
code|int32|状态码|-
data|object|响应数据|-
└─stdOutList|array|标准输出流信息|-
└─errorOutList|array|错误输出流信息|-

**Response-example:**
```
{
  "message": "success",
  "success": true,
  "timestamp": "2023-06-11 15:47:37",
  "code": 375,
  "data": {
    "stdOutList": [
      "zrfpst"
    ],
    "errorOutList": [
      "1m2g1n"
    ]
  }
}
```

## 电子口岸报文加签Controller
### 海关数据加签
**URL:** http://localhost:8080/rpc/eport/signature

**Type:** POST

**Author:** lwj

**Content-Type:** application/json; charset=utf-8

**Description:** 此接口已经整合"海关总署XML"和"海关179数据抓取"的加签<br/><br><ul><b>支持的加签类型</b><li>1.海关CEBXxxMessage XML数据加签</li><li>2. 海关179数据加签</li></ul>

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|唯一id, 用来区分是哪一次发送的消息，默认值=1，从1开始，{@code int} 最大, 2<sup>31</sup>-1.|false|-
data|string|加签源数据<br><ul><br>    <b>支持的加签数据类型</b><br>    <li>1. 不带ds:Signature节点的CEBXxxMessage.xml原文;<a href='http://tool.qdhuaxun.cn/ceb/CEB311Message.xml'>待加签xml报文样例</a></li><br>    <li>2. 海关179加签数据; 数据格式请传符合「海关179数据规范」的标准字符串,入参示例:<br><pre>"sessionID":"ad2254-8hewyf32-55616249"||"payExchangeInfoHead":"{"guid":"9D55BA71-22DE-41F4-8B50-C36C83B3B530","initalRequest":"原始请求","initalResponse":"ok","ebpCode":"4404840022","payCode":"312226T001","payTransactionId":"2018121222001354081010726129","totalAmount":100,"currency":"142","verDept":"3","payType":"1","tradingTime":"20181212041803","note":"批量订单，测试订单优化,生成多个so订单"}"||"payExchangeInfoLists":"[{"orderNo":"SO1710301150602574003","goodsInfo":[{"gname":"lhy-gnsku3","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"},{"gname":"lhy-gnsku2","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453"}],"recpAccount":"OSA571908863132601","recpCode":"","recpName":"YUNJIHONGKONGLIMITED"}]"||"serviceTime":"1544519952469"</pre><br>    </li><br></ul>|true|-

**Request-example:**
```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8080/rpc/eport/signature --data '{
  "id": 830,
  "data": "9p7bv4"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
message|string|返回消息|-
success|boolean|是否成功|-
timestamp|string|响应时间戳|-
code|int32|状态码|-
data|object|响应数据|-
└─success|boolean|本次加签是否成功|-
└─certNo|string|签名的ukey的卡序列号|-
└─x509Certificate|string|签名的ukey证书<br><ul><br>    如需导出.cer证书,文件格式:卡序列号.cer<br>    <li><a href='http://tool.qdhuaxun.cn/?getcert'>证书导出帮助连接</a></li><br></ul>|-
└─digestValue|string|xml的数字摘要，先对不包含Signature节点的原始报文,进行C14n格式化，然后取shal二进制摘要，然后对sha1的值进行base64编码<br><ul><br>    <li><a href='http://tool.qdhuaxun.cn/ceb/CEB311Message.xml'>待加签报文样例</a></li><br></ul>|-
└─signatureValue|string|调用ukey获取的签名值|-

**Response-example:**
```
{
  "message": "success",
  "success": true,
  "timestamp": "2023-06-11 15:47:37",
  "code": 613,
  "data": {
    "success": true,
    "certNo": "euwn6d",
    "x509Certificate": "abri7r",
    "digestValue": "cltmpb",
    "signatureValue": "qlxwu3"
  }
}
```

### 海关总署XML数据加签（测试）
**URL:** http://localhost:8080/rpc/eport/signature/test/ceb

**Type:** GET

**Author:** lwj

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 非正式调用API，只为了让你看到海关总署XML加密的数据返回格式

**Request-example:**
```
curl -X GET -i http://localhost:8080/rpc/eport/signature/test/ceb
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
message|string|返回消息|-
success|boolean|是否成功|-
timestamp|string|响应时间戳|-
code|int32|状态码|-
data|object|响应数据|-
└─any object|object|any object.|-

**Response-example:**
```
{
  "message": "success",
  "success": true,
  "timestamp": "2023-06-11 15:47:37",
  "code": 446,
  "data": {}
}
```

### 海关179数据抓取加签（测试）
**URL:** http://localhost:8080/rpc/eport/signature/test/179

**Type:** GET

**Author:** lwj

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 非正式调用API，只为了让你看到179加密的数据返回格式

**Request-example:**
```
curl -X GET -i http://localhost:8080/rpc/eport/signature/test/179
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
message|string|返回消息|-
success|boolean|是否成功|-
timestamp|string|响应时间戳|-
code|int32|状态码|-
data|object|响应数据|-
└─any object|object|any object.|-

**Response-example:**
```
{
  "message": "success",
  "success": true,
  "timestamp": "2023-06-11 15:47:37",
  "code": 386,
  "data": {}
}
```


