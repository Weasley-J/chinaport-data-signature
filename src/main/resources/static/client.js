/*!
 中国电子口岸数据中心,JavaScript Library v1.3.9 For 客户端控件 v1.5.12
 http://www.chinaport.gov.cn
 Date:2017年4月25日
 LastModifyDate:2019年09月05日
 */
(function (window, document, navigator) {
    //单一窗口:
    var installlerUrl;////="http://patchdownload.chinaport.gov.cn/EportClient/EportClientSetup_V1.5.3.exe";
    //安装包动获取地址
    var swVersionScript = "https://app.singlewindow.cn/sat/swVersion.js";
    //客户端控件安装包下载地址;
    if (!window.SwVersion) {
        var onloadFunc = function () {
            var jsDom = document.createElement("script");
            jsDom.setAttribute("type", "text/javascript");
            jsDom.setAttribute("src", swVersionScript + "?d=" + new Date().getTime());
            document.body.appendChild(jsDom);
            installlerUrl = window.SwVersion && window.SwVersion.getIkeyDownloadUrl();
            if (!installlerUrl) {
                setTimeout(function () {
                    installlerUrl = window.SwVersion && window.SwVersion.getIkeyDownloadUrl();
                    if (window.console && window.console.log) {
                        window.console.log("%c installlerUrl地址为:" + installlerUrl, 'color:#1941EC;font-size:12px');
                    }
                }, 3000);//等待3秒渲染
            }
        };
        //页面完全加载之后运行
        if (window.addEventListener) {///新的浏览器标准.
            window.addEventListener('load', onloadFunc, false);
        } else if (window.attachEvent) {///兼容IE8以下的浏览器.
            window.attachEvent('onload', onloadFunc);
        } else {
            window.onload = onloadFunc;
        }
    }
    //再次试图加载installlerUrl,保险机制
    if (!installlerUrl) {
        setTimeout(function () {
            installlerUrl = window.SwVersion && window.SwVersion.getIkeyDownloadUrl();
            if (window.console && window.console.log) {
                window.console.log("%c installlerUrl地址为:" + installlerUrl, 'color:#1941EC;font-size:12px');
            }
        }, 3000);//
    }
    //默认连接类型等待3秒渲染
    var DefaultType = "iKey";
    // //互联网+
    // //客户端控件安装包下载地址;
    // var installlerUrl;
    // //默认连接类型
    // var DefaultType = "iKeyEportCus";

    //对象转Json
    var toJson = function (obj) {
        if (window.JSON) {
            return JSON.stringify(obj);
        } else {
            alert("JSON转换错误!");
            return null;
        }
    };

    //返回值Json转对象
    var jsonToObj = function (text) {
        if (window.JSON) {
            return JSON.parse(text);
        } else {
            return eval("(" + text + ")");
        }
    };

    var getGuid = function () {
        var s4 = function () {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        };
        return (s4() + s4() + "-" + s4() + "-" + s4() + "-" + s4() + "-" + s4() + s4() + s4());
    };

    //计算数据分块
    var splitStrData = function (dataStr) {
        if (typeof dataStr !== "string") {
            throw new Error("数据类型错误");
        }
        var MaxLength = 120 * 1024;
        var byteCount = 0, p = 0;
        var rst = [];
        for (var i = 0; i < dataStr.length; i++) {
            var _escape = escape(dataStr.charAt(i));
            byteCount = byteCount + ((_escape.length >= 4 && _escape.charAt(0) === "%" && _escape.charAt(1) === "u")/*汉字*/ ? 3 : 1);
            if (byteCount > MaxLength - 3) {
                rst.push(dataStr.substring(p, i + 1));
                p = i + 1;
                byteCount = 0;
            }
        }
        if (p !== dataStr.length) {
            rst.push(dataStr.substring(p));
        }
        return rst;
    };

    //"BLOCKTXT"+总校验码(4字节)+块校验码(4字节)+数据总大小(16字节)+当前块大小(8字节)+是否分块/分为几块(4字节)+数据分块组号(36字节)+数据包号(4字节)+保留位(44字节),总计128字长
    //        8               12              16                 32                40                       44                   80              84            128
    var getDataHeader = function (checkCode, blockCheckCode, size, currsize, blockCount, blockGuid, blockNum) {
        var rst = "BLOCKTXT";//8
        rst += equilongString(checkCode, 4, "0");//4
        rst += equilongString(blockCheckCode, 4, "0");//4
        rst += equilongString(size, 16, "0");//16
        rst += equilongString(currsize, 8, "0");//8
        rst += equilongString(blockCount, 4, "0");//4
        rst += equilongString(blockGuid, 36, "0");//36
        rst += equilongString(blockNum, 4, "0");//4
        rst += "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";//128个占位符0
        return rst.substring(0, 128);//截取前128个子
    };

    //获取等长字符串,默认补0
    var equilongString = function (str, length, prefix) {
        if (!str) {
            str = "";
        }
        if (typeof str !== "string") {
            str = str + "";
        }
        if (!prefix) {
            prefix = "0";
        }
        var diff = length - str.length;
        for (var i = 0; i < diff; i++) {
            str = prefix + str;
        }
        return str;
    };

    //判断IE
    var isIE6789 = function () {
        var version = (!!navigator.appVersion) ? navigator.appVersion.split(";") : [];
        var trim_Version = (version.length > 1) ? version[1].replace(/[ ]/g, "") : "";
        return navigator.appName === "Microsoft Internet Explorer" && (trim_Version === "MSIE6.0" || trim_Version === "MSIE7.0" || trim_Version === "MSIE8.0" || trim_Version === "MSIE9.0");
    };

    //IE6789,创建WebSocket对象
    if (!window.WebSocket && isIE6789()) {
        //alert("IE~");
        WebSocket = function (url) {
            this.activeXObject = new ActiveXObject("snsoft.WebSocket");
            var _self = this, ax = this.activeXObject;
            setTimeout(function () {
                ax.websocketOpen(_self, url);
            }, 0);
        };
        WebSocket.prototype = {
            _callback: function (call, ev) {
                var f;
                if (typeof (f = this[call]) === "function") {
                    f.call(this, ev);
                }
            },
            getReadyState: function (type) {
                return this.activeXObject.getReadyState((type || DefaultType));
            },
            send: function (data) {
                this.activeXObject.websocketSendText(data);
            },
            close: function () {
                this.activeXObject.websocketClose();
            }
        };
    }

    //连接本地服务
    var ws;
    var conn = function () {
        //readyState->0:尚未建立连接;1:已经建立连接;2:正在关闭;3:已经关闭或不可用
        //=0的时候不要创建新的了,连接慢的情况下,创建新的可能会更慢!
        if (!ws || getWebSocketReadyState(ws) === 2 || getWebSocketReadyState(ws) === 3) {
            try {
                //改为:http协议页面,使用ws://127.0.0.1:61232地址,其他使用wss://wss.singlewindow.cn:61231
                var websocketurl = ((!!window.location) && window.location.protocol === "http:") ? "ws://127.0.0.1:61232" : "wss://wss.singlewindow.cn:61231";
                if (window.console && window.console.log) {
                    window.console.log("%c 使用" + websocketurl + "连接控件服务", 'color:#1941EC;font-size:12px');
                }
                ws = new WebSocket(websocketurl); //'wss://testlocal.sino-clink.com.cn:61231'
                //WebSocket事件
                ws.onmessage = function (e) {
                    if (e.data.charAt(0) === "{") {
                        var msg = jsonToObj(e.data);
                        //if(msg&&msg['_method']=="open"){
                        //握手成功,停掉timeTask的Task
                        //	clearTimeout(timeTask);
                        //}
                        if (window.console && window.console.log) {
                            var errMsg = "调用" + msg["_method"] + "方法已返回, Result=" + (msg["_args"] && msg["_args"].Result);
                            var errStyle = 'color:#1941EC;font-size:12px';
                            if (!(msg["_args"] && msg["_args"].Result)) {
                                errMsg += ", CallbackInfos=" + e.data;
                                errStyle = 'color:#D94E34;font-size:14px';
                            }
                            window.console.log("%c " + errMsg, errStyle);
                        }
                        if (callbacks[msg["_method"]]) {
                            callbacks[msg["_method"]](msg["_args"], e.data);
                        }
                    } else {
                        alert("数据格式非法:" + e.data);
                    }
                };
            } catch (ex) {
                if (console && console.log) {
                    console.log(ex);
                }
            }
        }
        return ws;
    };

    ws = conn();

    //setInterval(conn,10000);//每10秒检查一次

    /**
     * 回调函数集合
     * @type {{}}
     */
    var callbacks = {};

    /**
     * 分块数据{guid:{checkcode:"98AC",totalLength:14571,retry:0,block:[data1,data2,data3,...]},...}
     * @type {{}}
     */
    var blockData = {};

    var sendMessage = function (msg, callback) {
        if (getWebSocketReadyState(ws) === 1) {
            ws.send(msg);
        } else {
            //如果状态为0
            var times = 0;
            var waitForWebSocketConn = function () {
                if (times > 9) {
                    //throw new Error("连接客户端控件服务失败，请重试。");
                    callback({Result: false, Data: [], Error: ["连接客户端控件服务失败,请重试.", "Err:Base60408"]});
                    conn();//重连
                } else {
                    if (getWebSocketReadyState(ws) === 0) {//试图等待500毫秒
                        setTimeout(
                            function () {
                                if (getWebSocketReadyState(ws) === 1) {
                                    ws.send(msg);
                                } else {
                                    times++;
                                    waitForWebSocketConn();
                                }
                            }, 500);
                    } else if (getWebSocketReadyState(ws) === 2 || getWebSocketReadyState(ws) === 3) {//试图重连
                        times++;
                        conn();//重连
                    }
                }
            };
            waitForWebSocketConn();
        }
    };

    /**
     * 分块的回调函数
     * @param args:{id:1,guid:"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",next:1,finish:false}
     * @private
     */
    callbacks._nextBlock = function (args) {
        var guid = args.Data[0]["guid"];
        if (args.Data[0]["finish"]) {
            //要从内存中释放掉这个数据
            if (blockData[guid]) {
                delete blockData[guid];
            }
        } else {
            //传输
            conn();
            var next = args.Data[0]["next"];
            var blockObj = blockData[guid];
            if (!args.Result) {//失败的
                var retry = blockObj["retry"] || 0;
                retry = retry + 1;
                blockObj["retry"] = retry;
                if (retry > blockObj.block.length * 3) {
                    //达到最大重试次数
                    alert("数据接收错误!");
                }
            }
            var currData = blockObj.block[next];
            var blockCheckCode = DIGEST.CheckCode(currData);
            var pakHeaser = getDataHeader(blockObj["checkcode"], blockCheckCode, blockObj["totalLength"], currData.length, blockObj.block.length, guid, next);
            var msg = pakHeaser + currData;
            sendMessage(msg);
            //alert(toJson(args));
        }
    };

    /**
     * 一种简化的摘要校验码,相比于CRC,对汉字的编码做的简化的处理,提高了一些效率.
     * @type {Object}
     */
    var DIGEST = {};
    DIGEST._auchCRCHi = [
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
    ];
    DIGEST._auchCRCLo = [
        0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,
        0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,
        0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,
        0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,
        0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
        0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
        0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,
        0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
        0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,
        0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,
        0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,
        0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
        0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
        0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
        0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,
        0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
        0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,
        0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
        0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,
        0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,
        0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,
        0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,
        0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
        0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
        0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
        0x43, 0x83, 0x41, 0x81, 0x80, 0x40
    ];

    DIGEST.CheckCode = function (buffer) {
        var hi = 0xff;
        var lo = 0xff;
        for (var i = 0; i < buffer.length; i++) {
            var idx = 0xff & (hi ^ buffer.charCodeAt(i));
            hi = (lo ^ DIGEST._auchCRCHi[idx]);
            lo = DIGEST._auchCRCLo[idx];
        }
        return DIGEST.padLeft((hi << 8 | lo).toString(16).toUpperCase(), 4, '0');
    };
    DIGEST.padLeft = function (s, w, pc) {
        if (pc === undefined) {
            pc = '0';
        }
        for (var i = 0, c = w - s.length; i < c; i++) {
            s = pc + s;
        }
        return s;
    };

    var id = 0;

    //调用本地服务方法的抽象(数据不分包)
    var baseInvoke = function (method, args, callback) {
        if (typeof args === "function") {
            callback = args;
            args = {};
        }
        conn();
        if (window.console && window.console.log) {
            window.console.log("%c 调用方法" + method, 'color:#95F065;font-size:12px');
        }
        callbacks[method] = callback;
        var _data = {"_method": method};
        _data["_id"] = id++;
        args = args || {};
        _data["args"] = args;
        var s = toJson(_data);
        if (getWebSocketReadyState(ws) === 0) {
            setTimeout(function () {
                //建立连接需要时间~ 首次延迟半秒~
                sendMessage(s, callback);
            }, 500)
        } else {
            sendMessage(s, callback);
        }
    };

    var baseInvokeByFrames = function (method, args, callback) {
        if (typeof args === "function") {
            callback = args;
            args = {};
        }
        conn();
        if (window.console && window.console.log) {
            window.console.log("%c 调用方法" + method, 'color:#95F065;font-size:12px');
        }
        callbacks[method] = callback;
        var _data = {"_method": method};
        _data["_id"] = id++;
        args = args || {};
        _data["args"] = args;
        var s = toJson(_data);
        if (getWebSocketReadyState(ws) === 0 || getWebSocketReadyState(ws) === 3) {
            setTimeout(function () {
                //建立连接需要时间~ 首次延迟半秒~
                sendFrames(s, callback);
            }, 500)
        } else {
            sendFrames(s, callback);
        }
    };

    /**
     * 分帧传输数据
     * @param s 数据
     * @param callback 回调函数
     */
    var sendFrames = function (s, callback) {
        //校验码
        var checkCode = DIGEST.CheckCode(s);//打校验码
        //分包的组
        var guid = getGuid();
        while (blockData[guid]) {
            //存在则重新生成一个!
            guid = getGuid();
        }
        //数据分块
        var splitData = splitStrData(s);//数据切块
        //记录以备重发
        blockData[guid] = {checkcode: checkCode, totalLength: s.length, retry: 0, block: splitData};
        //传第一个包
        //报头(纯文本类型)含义
        //"BLOCKTXT"+总校验码(4字节)+块校验码(4字节)+数据总大小(16字节)+当前块大小(8字节)+是否分块/分为几块(4字节)+数据分块组号(36字节)+数据包号(4字节)+保留位(44字节),总计128字长
        //        8               12              16                 32                40                       44                   80              84            128
        var blockCheckCode = DIGEST.CheckCode(splitData[0]);
        var pakHeaser = getDataHeader(checkCode, blockCheckCode, s.length, splitData[0].length, splitData.length, guid, 0);
        var msg = pakHeaser + splitData[0];
        sendMessage(msg, callback);
    };

    /**
     * readyState->0:尚未建立连接;1:已经建立连接;2:正在关闭;3:已经关闭或不可用
     * @param thisWs
     * @returns {*}
     */
    var getWebSocketReadyState = function (thisWs) {
        var currWs = thisWs || conn();
        if (!currWs) {
            return 0;
        }
        if (currWs.readyState !== undefined) {
            return currWs.readyState;
        }
        if (currWs.getReadyState) {//IE8,IE9
            return currWs.getReadyState();
        }
        /*
         if(currWs.activeXObject){//IE8,IE9
         return currWs.activeXObject.readyState;
         }
         */
        return 0;
    };

    window.EportClient = {
        isInstalled: function (type, callback, currInstalllerUrl) {
            //这个方法最慢需要3秒钟返回信息.
            if (typeof type === "function") {
                if (callback) {
                    currInstalllerUrl = callback;
                }
                callback = type;
                type = DefaultType;
            }
            ws = conn();
            var bInstalled = getWebSocketReadyState(ws) === 1;
            var retryConut = 0;

            function retry() {
                retryConut++;
                ws = conn();
                bInstalled = getWebSocketReadyState(ws) === 1;
                if (!bInstalled) {
                    if (retryConut < 3) {
                        setTimeout(retry, 1500);
                    } else if (retryConut === 3) {
                        //改为使用iframe代替href=的方式,避免FireFox不拦截本地协议而发生页面跳转.
                        var iframeDom = document.createElement('iframe');
                        iframeDom.style.cssText = 'width:1px;height:1px;position:fixed;top:0;left:0;display:none;';
                        iframeDom.src = 'singlewindow://Restart';
                        document.body.appendChild(iframeDom);
                        setTimeout(retry, 2500);
                    } else {
                        //未安装
                        var errMsg = "检测到您未安装" + type + "客户端! " + type + "下载地址为:" + currInstalllerUrl || installlerUrl || window.installlerUrl;
                        if (callback) {
                            callback({"Result": false, "Data": [], "Error": [errMsg]});
                        } else {
                            if (window.console) {
                                window.console.log(errMsg);
                            }
                            //alert(errMsg);
                        }
                    }
                } else {
                    //已经安装了
                    var okMsg = "已经安装了" + type + "客户端.";
                    if (callback) {
                        callback({"Result": true, "Data": [okMsg], "Error": []});
                    } else {
                        if (window.console) {
                            window.console.log(okMsg);
                        }
                        //alert(okMsg);
                    }
                }
            }

            retry();
            //iKeyInstalllerUrl
        },
        /**
         *
         * @param func 被执行函数
         * @param arg1
         * @param arg2
         * @param argX
         */
        isInstalledTest: function (func, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) {//这里argX只多不少即可
            if (!func) {
                throw new Error("未知的JS的function,请检查调用isInstalledTest传入的第一个参数是否存在该函数.");
            }
            EportClient.isInstalled(DefaultType, function (msg) {
                if (msg.Result) {
                    if (func && (typeof func) === "function") {
                        func.call(null, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
                    } else {
                        alert(msg.Data[0]);
                    }
                } else {
                    if (window.console) {
                        window.console.log(toJson(msg));
                    }
                    ///alertErrMsg(msg);
                }
            }, installlerUrl || window.installlerUrl);
        },
        /*----------------------------------内嵌方法--------------------------------------------------------------------*/
        /**
         * 试图更新控件
         * @param callback
         */
        update: function (callback) {
            conn();
            var method = "update";
            callbacks[method] = callback;
            var _data = {"_method": method};
            _data["_id"] = id++;
            _data["args"] = {};
            var s = toJson(_data);
            sendMessage(s);
        },
        /**
         * 全量更新
         * @param callback
         */
        fullupdate: function (callback) {
            baseInvoke("fullupdate", {}, callback);
        },
        /**
         * 获取控件文件的路径
         * @param file
         * @param callback
         */
        getfilepath: function (file, callback) {
            baseInvoke("getfilepath", {"file": file}, callback);
        },
        /**
         * 获取安装包版本号
         * @param callback
         */
        getInstallerVersion: function (callback) {
            baseInvoke("ver", callback);
        },
        /**
         * 获取插件模块版本号
         * @param module 模块名称
         * @param callback
         */
        getModuleVersion: function (module, callback) {
            baseInvoke(module + "_GetVersions", callback);
        },


        /*----------------------------------基础功能--------------------------------------------------------------------*/
        /**
         * 选择文件(打开或读取用)
         * @param filter 文件类型描述
         * @param fileSize 文件大小过滤(限制条件)
         * @param multiSelect 是否多选,Y多选,默认Y
         * @param callback
         */
        choosefile: function (filter, fileSize, multiSelect, callback) {
            if ((!callback) && (!fileSize) && (!multiSelect) && typeof filter === "function") {
                callback = filter;
                filter = null;
                baseInvoke("base_choosefile", callback);
            } else if ((!callback) && (!multiSelect) && typeof fileSize === "function") {
                callback = fileSize;
                fileSize = null;
                baseInvoke("base_choosefile", {
                    "filter": filter
                }, callback);
            } else if ((!callback) && typeof multiSelect === "function") {
                callback = multiSelect;
                multiSelect = null;
                baseInvoke("base_choosefile", {
                    "filter": filter,
                    "fileSize": fileSize
                }, callback);
            } else {
                baseInvoke("base_choosefile", {
                    "filter": filter,
                    "fileSize": fileSize,
                    "multiSelect": multiSelect
                }, callback);
            }
        },
        /**
         * 选择目录,用于保存或者读取文件.
         * @param callback
         */
        chooseDir: function (callback) {
            baseInvoke("base_chooseDir", {}, callback);
        },
        /**
         * 指定文件(保存或写入文件使用)
         * @param filter 文件类型描述
         * @param callback
         */
        savefile: function (filter, callback) {
            if ((!callback) && typeof filter === "function") {
                callback = filter;
                filter = null;
                baseInvoke("base_savefile", callback);
            } else {
                baseInvoke("base_savefile", {"filter": filter}, callback);
            }
        },
        /**
         * 打开文件,并返回Base64的内容
         * @param path 文件全路径
         * @param fileSize 文件限制大小
         * @param callback
         */
        openFileAsBase64: function (path, fileSize, callback) {
            baseInvoke("base_OpenFileAsBase64", {"path": path, "fileSize": fileSize}, callback);
        },
        /**
         * 计算指定的文件的摘要
         * @param path 文件全路径
         * @param algorithm 摘要算法,目前支持SHA384,SHA256,SHA1,MD5
         * @param callback
         */
        computeFileHash: function (path, algorithm, callback) {
            baseInvoke("base_ComputeFileHash", {
                "path": path,
                "algorithm": algorithm
            }, callback);
        },
        /**
         * 文件批量转移
         * @param args 参见文档
         * @param callback
         */
        movefiles: function (args, callback) {
            baseInvoke("base_movefiles", args, callback);
        },
        /**
         * 直接打开ClickOnce地址
         * @param url 地址
         * @param HideIE 是否隐藏IE界面
         * @param callback
         */
        startClickOnceAppWithUser: function (url, HideIE, callback) {
            if (callback && typeof callback === "function") {
                baseInvoke("base_StartClickOnceAppWithUserinfo", {"qpurl": url, "HideIE": HideIE}, callback);
            } else if (!(callback) && typeof HideIE === "function") {
                callback = HideIE;
                HideIE = null;
                baseInvoke("base_StartClickOnceAppWithUserinfo", {"qpurl": url}, callback);
            } else {
                throw new Error("startClickOnceAppWithUser的调用参数非法!");
            }
        },
        /**
         * 打开带有ticket的ClickOnce地址,地址需要包含ticket参数
         * @param url 带有ticket参数的地址
         * @param HideIE 是否隐藏IE界面
         * @param callback
         */
        startClickOnceAppWithTicket: function (url, HideIE, callback) {
            if (callback && typeof callback === "function") {
            } else if (!(callback) && typeof HideIE === "function") {
                callback = HideIE;
                HideIE = null;
            } else {
                throw new Error("startClickOnceAppWithTicket的调用参数非法!");
            }
            if (!url || url === "") {
                callback({
                    "Result": false,
                    "Data": [],
                    "Error": ["args中必须要包含必要的非空的qpurl参数!", "Err:Base50000"]
                });
                return;
            } else if (url.indexOf("ticket=") < 0 && url.indexOf("ticket =") < 0) {
                callback({"Result": false, "Data": [], "Error": ["args中的url必须要包含ticket!", "Err:Base50000"]});
                return;
            }
            baseInvoke("base_StartClickOnceApp", {
                "qpurl": url,
                "HideIE": HideIE,
                "casurl": "-1",
                "passwd": "-1"
            }, callback);
        },

        /**
         * 获取打印机列表
         * @param callback
         */
        getLocalPrinters: function (callback) {
            baseInvoke("base_GetLocalPrinters", {}, callback);
        },
        /**
         * 将指定名称的打印机设为系统默认
         * @param printerName 打印机名称
         * @param callback
         */
        setDefaultPrinter: function (printerName, callback) {
            baseInvoke("base_SetDefaultPrinter", {"printerName": printerName}, callback);
        },
        /**
         * 打印PDF内容
         * @param fileContent 文件内容,Base64格式
         * @param printerName 打印机名称
         * @param ranges 页码选择(过滤)
         * @param marginLeft 上边距xxx像素,支持负数
         * @param marginTop 左边距xxx像素,支持负数
         * @param milliseconds 超时时间(毫秒) 默认25000
         * @param callback
         */
        printPdf: function (fileContent, printerName, ranges, marginLeft, marginTop, milliseconds, callback) {
            if (callback && typeof callback === "function") {
                baseInvokeByFrames("base_PrintPdf", {
                    "fileContent": fileContent,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": marginLeft,
                    "marginTop": marginTop,
                    "milliseconds": milliseconds
                }, callback);
            } else if (!(callback) && typeof milliseconds === "function") {
                callback = milliseconds;
                milliseconds = null;
                baseInvokeByFrames("base_PrintPdf", {
                    "fileContent": fileContent,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": marginLeft,
                    "marginTop": marginTop
                }, callback);
            } else if (!(callback) && typeof marginTop === "function") {
                callback = marginTop;
                marginTop = null;
                baseInvokeByFrames("base_PrintPdf", {
                    "fileContent": fileContent,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": marginLeft,
                    "marginTop": 0
                }, callback);
            } else if (!(callback) && typeof marginLeft === "function") {
                callback = marginLeft;
                marginTop = null;
                marginLeft = null;
                baseInvokeByFrames("base_PrintPdf", {
                    "fileContent": fileContent,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": 0,
                    "marginTop": 0
                }, callback);
            } else if (!(callback) && typeof ranges === "function") {
                callback = ranges;
                marginTop = null;
                marginLeft = null;
                ranges = null;
                baseInvokeByFrames("base_PrintPdf", {
                    "fileContent": fileContent,
                    "printerName": printerName,
                    "marginLeft": 0,
                    "marginTop": 0
                }, callback);
            } else if (!(callback) && typeof printerName === "function") {
                callback = printerName;
                marginTop = null;
                marginLeft = null;
                ranges = null;
                printerName = null;
                baseInvokeByFrames("base_PrintPdf", {
                    "fileContent": fileContent,
                    "marginLeft": 0,
                    "marginTop": 0
                }, callback);
            } else {
                throw new Error("printPdf的调用参数非法!");
                //alert("printPdf的调用参数非法");
            }
        },
        /**
         * 打印PDF本地文件
         * @param filePath 文件绝对路径
         * @param printerName 打印机名称
         * @param ranges 打印的页面(过滤)
         * @param marginLeft 上边距xxx像素,支持负数
         * @param marginTop 左边距xxx像素,支持负数
         * @param milliseconds 超时时间(毫秒) 默认25000
         * @param callback
         */
        printPdfFile: function (filePath, printerName, ranges, marginLeft, marginTop, milliseconds, callback) {
            if (callback && typeof callback === "function") {
                baseInvoke("base_PrintPdf", {
                    "filePath": filePath,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": marginLeft,
                    "marginTop": marginTop,
                    "milliseconds": milliseconds
                }, callback);
            } else if (!(callback) && typeof milliseconds === "function") {
                callback = milliseconds;
                milliseconds = null;
                baseInvoke("base_PrintPdf", {
                    "filePath": filePath,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": marginLeft,
                    "marginTop": marginTop
                }, callback);
            } else if (!(callback) && typeof marginTop === "function") {
                callback = marginTop;
                marginTop = null;
                baseInvoke("base_PrintPdf", {
                    "filePath": filePath,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": marginLeft,
                    "marginTop": 0
                }, callback);
            } else if (!(callback) && typeof marginLeft === "function") {
                callback = marginLeft;
                marginTop = null;
                marginLeft = null;
                baseInvoke("base_PrintPdf", {
                    "filePath": filePath,
                    "printerName": printerName,
                    "ranges": ranges,
                    "marginLeft": 0,
                    "marginTop": 0
                }, callback);
            } else if (!(callback) && typeof ranges === "function") {
                callback = ranges;
                marginTop = null;
                marginLeft = null;
                ranges = null;
                baseInvoke("base_PrintPdf", {
                    "filePath": filePath,
                    "printerName": printerName,
                    "marginLeft": 0,
                    "marginTop": 0
                }, callback);
            } else if (!(callback) && typeof printerName === "function") {
                callback = printerName;
                marginTop = null;
                marginLeft = null;
                ranges = null;
                printerName = null;
                baseInvoke("base_PrintPdf", {"filePath": filePath, "marginLeft": 0, "marginTop": 0}, callback);
            } else {
                throw new Error("printLocalPdf的调用参数非法!");
                //alert("printLocalPdf的调用参数非法");
            }
        },
        /**
         * 下载文件
         * @param url 文件下载地址
         * @param localpath 保存在本地的地址
         * @param method http的Method,默认为get
         * @param cookies cookies
         * @param returnData 是否直接返回数据
         * @param callback
         */
        fileDownload: function (url, localpath, method, cookies, returnData, callback) {
            baseInvoke("base_FileDownload", {
                "url": url,
                "localpath": localpath,
                "method": method,
                "cookies": cookies,
                "returnData": returnData
            }, callback);
        },
        /**
         * 获取文件的大小
         * @param path 本地路径
         * @param callback 回调函数
         */
        fileSize: function (path, callback) {
            baseInvoke("base_FileSize", {"path": path}, callback);
        },
        /**
         * 文件批量上传
         * @param args 参见文档
         * @param callback
         */
        fileBatchUpload: function (args, callback) {
            baseInvoke("upload_FileBatchUpload", args, callback);
        },

        /**
         * 删除控件的临时文件
         * @param args 参见文档
         * @param callback
         */
        deleteTempFile: function (args, callback) {
            baseInvoke("base_DeleteTempFiles", args, callback);
        },
        /**
         * 判断文件是否为pdf文件
         * @param args 参见文档
         *新增接口，满足行政审批业务需求
         * @param callback
         */
        determineFilesIsPDF: function (args, callback) {
            baseInvoke("base_DetermineFilesIsPDF", args, callback);
        },

        /*----------------------------------海关安全功能-----------------------------------------------------------------*/

        /**
         * 设置加密设备的优先级
         * @param szInitEnvPriority
         * @param callback
         */
        cusSpcSetInitEnvPriority: function (szInitEnvPriority, callback) {
            baseInvoke("cus-sec_SpcSetInitEnvPriority", {"szInitEnvPriority": szInitEnvPriority}, callback);
        },
        /**
         * 打开卡
         * @param callback
         */
        cusSpcInitEnvEx: function (callback) {
            baseInvoke("cus-sec_SpcInitEnvEx", callback);
        },
        /**
         * 关闭卡
         * @param callback
         */
        cusSpcClearEnv: function (callback) {
            baseInvoke("cus-sec_SpcClearEnv", callback);
        },
        /**
         * 获取附加信息
         * @param callback
         */
        cusSpcGetCardAttachInfo: function (callback) {
            baseInvoke("cus-sec_SpcGetCardAttachInfo", callback);
        },
        /**
         * 获取附加信息(PEM格式)
         * @param callback
         */
        cusSpcGetCardAttachInfoAsPEM: function (callback) {
            baseInvoke("cus-sec_SpcGetCardAttachInfoAsPEM", callback);
        },
        /**
         * 获取卡号
         * @param callback
         */
        cusSpcGetCardID: function (callback) {
            baseInvoke("cus-sec_SpcGetCardID", callback);
        },
        /**
         * 获取证书编号
         * @param callback
         */
        cusSpcGetCertNo: function (callback) {
            baseInvoke("cus-sec_SpcGetCertNo", callback);
        },
        /**
         * 申请者名称
         * @param callback
         */
        cusSpcGetUName: function (callback) {
            baseInvoke("cus-sec_SpcGetUName", callback);
        },
        /**
         * 单位ID
         * @param callback
         */
        cusSpcGetEntID: function (callback) {
            baseInvoke("cus-sec_SpcGetEntID", callback);
        },
        /**
         * 单位名
         * @param callback
         */
        cusSpcGetEntName: function (callback) {
            baseInvoke("cus-sec_SpcGetEntName", callback);
        },
        /**
         * 单位类别
         * @param callback
         */
        cusSpcGetEntMode: function (callback) {
            baseInvoke("cus-sec_SpcGetEntMode", callback);
        },
        /**
         * 用户信息
         * @param callback
         */
        cusSpcGetCardUserInfo: function (callback) {
            baseInvoke("cus-sec_SpcGetCardUserInfo", callback);
        },
        /**
         * 取海关签名证书
         * @param callback
         */
        cusSpcGetSignCert: function (callback) {
            baseInvoke("cus-sec_SpcGetSignCert", callback);
        },
        /**
         * 取海关签名证书PEM
         * @param callback
         */
        cusSpcGetSignCertAsPEM: function (callback) {
            baseInvoke("cus-sec_SpcGetSignCertAsPEM", callback);
        },
        /**
         * 取海关加密证书
         * @param callback
         */
        cusSpcGetEnvCert: function (callback) {
            baseInvoke("cus-sec_SpcGetEnvCert", callback);
        },
        /**
         * 取海关加密证书PEM
         * @param callback
         */
        cusSpcGetEnvCertAsPEM: function (callback) {
            baseInvoke("cus-sec_SpcGetEnvCertAsPEM", callback);
        },
        /**
         * 查看海关证书有效期
         * @param cert 证书内容,可为空
         * @param callback
         */
        cusSpcGetValidTimeFromCert: function (cert, callback) {
            if (typeof cert === "function") {
                callback = cert;
                cert = null;
            }
            baseInvoke("cus-sec_SpcGetValidTimeFromCert", ((!!cert) ? {"cert": cert} : {}), callback);
        },
        /**
         * 验证海关卡密码,仅仅验证密码,不自动开关卡~
         * @param passwd 密码
         * @param callback
         */
        cusSpcVerifyPIN: function (passwd, callback) {
            baseInvoke("cus-sec_SpcVerifyPIN", {"passwd": passwd}, callback);
        },
        /**
         * 验证海关卡密码(自动开关卡)
         * @param passwd 密码
         * @param callback
         */
        cusSpcVerifyPINAll: function (passwd, callback) {
            baseInvoke("cus-sec_SpcVerifyPINAll", {"passwd": passwd}, callback);
        },
        /**
         * 修改密码
         * @param oldPin 旧密码
         * @param newPin 新密码
         * @param callback
         */
        cusSpcChangePIN: function (oldPin, newPin, callback) {
            baseInvoke("cus-sec_SpcChangePIN", {"oldPin": oldPin, "newPin": newPin}, callback);
        },
        /**
         * PEM编码
         * @param inData 原始内容
         * @param callback
         */
        cusSpcEncodePEM: function (inData, callback) {
            baseInvoke("cus-sec_SpcEncodePEM", {"inData": inData}, callback);
        },
        /**
         * PEM解码
         * @param inData PEM编码的内容(字符串)
         * @param callback
         */
        cusSpcDecodePEM: function (inData, callback) {
            baseInvoke("cus-sec_SpcDecodePEM", {"inData": inData}, callback);
        },
        /**
         * PEM解码,返回String类型的原文
         * @param inData PEM编码的内容(字符串)
         * @param callback
         */
        cusSpcDecodePEMAsString: function (inData, callback) {
            baseInvoke("cus-sec_SpcDecodePEMAsString", {"inData": inData}, callback);
        },
        /**
         * 签名,返回byte[]
         * @param inData 原文
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcSignData: function (inData, passwd, callback) {
            baseInvoke("cus-sec_SpcSignData", (!!passwd) ? {
                "inData": inData,
                "passwd": passwd
            } : {"inData": inData}, callback);
        },
        /**
         * 签名,返回PEM格式
         * @param inData 原文
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcSignDataAsPEM: function (inData, passwd, callback) {
            baseInvoke("cus-sec_SpcSignDataAsPEM", (!!passwd) ? {
                "inData": inData,
                "passwd": passwd
            } : {"inData": inData}, callback);
        },

        /**
         * 签名PEM编码格式原文,返回PEM格式
         * @param inData PEM编码格式原文
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcSignPemDataAsPEM: function (inData, passwd, callback) {
            baseInvoke("cus-sec_SpcSignPemDataAsPem", (!!passwd) ? {
                "inData": inData,
                "passwd": passwd
            } : {"inData": inData}, callback);
        },

        /**
         * 验证签名
         * @param inData 原文信息
         * @param signData 签名信息
         * @param certDataPEM 签名证书,PEM编码格式 可以为空,则取当前插着的卡中的证书
         * @param callback
         */
        cusSpcVerifySignData: function (inData, signData, certDataPEM, callback) {
            if (!callback && typeof certDataPEM === "function") {
                callback = certDataPEM;
                certDataPEM = null;
                baseInvoke("cus-sec_SpcVerifySignData", {
                    "inData": inData,
                    "signData": signData
                }, callback);
            } else {
                baseInvoke("cus-sec_SpcVerifySignData", {
                    "inData": inData,
                    "signData": signData,
                    "certDataPEM": certDataPEM
                }, callback);
            }
        },
        /**
         * 使用卡计算摘要
         * @param szInfo 原文内容
         * @param callback
         */
        cusSpcSHA1Digest: function (szInfo, callback) {
            baseInvoke("cus-sec_SpcSHA1Digest", {"szInfo": szInfo}, callback);
        },
        /**
         * 使用卡计算摘要,返回PEM格式信息
         * @param szInfo 原文内容
         * @param callback
         */
        cusSpcSHA1DigestAsPEM: function (szInfo, callback) {
            baseInvoke("cus-sec_SpcSHA1DigestAsPEM", {"szInfo": szInfo}, callback);
        },
        /**
         * 签名,不对原文计算摘要,请您自行计算好摘要传入
         * @param inData 原文的SHA1摘要
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcSignDataNoHash: function (inData, passwd, callback) {
            baseInvoke("cus-sec_SpcSignDataNoHash", (!!passwd) ? {
                "inData": inData,
                "passwd": passwd
            } : {"inData": inData}, callback);
        },
        /**
         * 签名,不对原文计算摘要,请您自行计算好摘要传入
         * 返回PEM格式数据
         * @param inData 原文的SHA1摘要
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcSignDataNoHashAsPEM: function (inData, passwd, callback) {
            baseInvoke("cus-sec_SpcSignDataNoHashAsPEM", (!!passwd) ? {
                "inData": inData,
                "passwd": passwd
            } : {"inData": inData}, callback);
        },
        /**
         * 验证签名,不对原文计算摘要,请您自行计算好摘要传入
         * @param inData 原文的SHA1摘要
         * @param signData 签名信息
         * @param certDataPEM 签名证书,PEM编码格式 可以为空,则取当前插着的卡中的证书
         * @param callback
         */
        cusSpcVerifySignDataNoHash: function (inData, signData, certDataPEM, callback) {
            if (!callback && typeof certDataPEM === "function") {
                callback = certDataPEM;
                certDataPEM = null;
                baseInvoke("cus-sec_SpcVerifySignDataNoHash", {
                    "inData": inData,
                    "signData": signData
                }, callback);
            } else {
                baseInvoke("cus-sec_SpcVerifySignDataNoHash", {
                    "inData": inData,
                    "signData": signData,
                    "certDataPEM": certDataPEM
                }, callback);
            }
        },
        /**
         * 签名随机数的组和方法
         * @param random 随机数
         * @param passwd 密码
         * @param callback
         */
        cusSpcSignRandom: function (random, passwd, callback) {
            baseInvoke("cus-sec_SpcSignRandom", {"random": random, "passwd": passwd}, callback);
        },
        /**
         * RSA加密(公钥)
         * @param inData 原文数据
         * @param certData 支持传证书
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcRSAEncrypt: function (inData, certData, passwd, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("cus-sec_SpcRSAEncrypt", {
                    "inData": inData,
                    "certData": certData,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("cus-sec_SpcRSAEncrypt", {
                    "inData": inData,
                    "certData": certData
                }, callback);
            } else {
                throw new Error("spcRSAEncrypt的调用参数非法!");
            }
        },
        /**
         * RSA解密
         * @param inData 密文数据
         * @param passwd 卡密码
         * @param callback
         */
        cusSpcRSADecrypt: function (inData, passwd, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("cus-sec_SpcRSADecrypt", {
                    "inData": inData,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("cus-sec_SpcRSADecrypt", {
                    "inData": inData
                }, callback);
            } else {
                throw new Error("spcRSAEncrypt的调用参数非法!");
            }
        },
        /*----------------------------------海关业务功能-----------------------------------------------------------------*/
        /**
         * 扫描仪初始化
         * @param callback
         */
        cusPdfScanInit: function (callback) {
            baseInvoke("pdf-scan_PdfScanScanInitialize", {}, callback);
        },
        /**
         * 使用扫描仪生成PDF文件
         * @param Edoc_code
         * @param Edoc_Name
         * @param fileFullPath 生成文件的目标全路径
         * @param passwd 海关卡密码
         * @param callback
         */
        cusPdfScan: function (Edoc_code, Edoc_Name, fileFullPath, passwd, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-scan_PdfScan", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "fileFullPath": fileFullPath,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("pdf-scan_PdfScan", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "fileFullPath": fileFullPath
                }, callback);
            } else if ((!callback) && typeof fileFullPath === "function") {//缺少2个参数,我们兼容缺少passwd,fileFullPath的情况
                callback = fileFullPath;
                fileFullPath = null;
                baseInvoke("pdf-scan_PdfScan", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少3个参数,我们兼容缺少passwd,fileFullPath,Edoc_Name的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("pdf-scan_PdfScan", {
                    "Edoc_code": Edoc_code
                }, callback);
            }
        },
        /**
         * 使用扫描仪生成Base64格式的PDF数据
         * @param Edoc_code
         * @param Edoc_Name
         * @param passwd 海关卡密码
         * @param callback
         */
        cusPdfScanAsPEM: function (Edoc_code, Edoc_Name, passwd, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-scan_PdfScanAsPEM", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("pdf-scan_PdfScanAsPEM", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少2个参数,我们兼容缺少passwd和Edoc_Name的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("pdf-scan_PdfScanAsPEM", {"Edoc_code": Edoc_code}, callback);
            }
        },
        /**
         * 使用方正组件预览Base64编码的PDF
         * @param fileContent Base64编码的PDF文件
         * @param callback
         */
        cusPdfContentView: function (fileContent, callback) {//destDir,fileName,
            baseInvokeByFrames("pdf-sign_PdfView", {"fileContent": fileContent}, callback);
        },
        /**
         * 使用方正组件预览本地文件的PDF
         * @param filePath 文件全路径
         * @param cookies Http地址需要的Cookies
         * @param callback
         */
        cusPdfFileView: function (filePath, cookies, callback) {//filePath可以是本地的file:,也可以是http:的
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-sign_PdfView", {"filePath": filePath, "cookies": cookies}, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少1个参数,我们兼容缺少cookies的情况
                callback = cookies;
                cookies = null;
                baseInvoke("pdf-sign_PdfView", {"filePath": filePath}, callback);
            } else {
                throw new Error("cusPdfFileView的调用参数非法!");
            }
        },
        /**
         * PDF格式检查 Base64编码的PDF
         * @param fileContent Base64编码的PDF文件
         * @param blankBookAuthorityCheck 字体或空白页检查未通过,是否视为检查失败.
         * @param callback
         */
        cusPdfContentCheck: function (fileContent, blankBookAuthorityCheck, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvokeByFrames("pdf-sign_PdfCheck", {
                    "fileContent": fileContent,
                    "blankBookAuthorityCheck": blankBookAuthorityCheck
                }, callback);
            } else if ((!callback) && typeof blankBookAuthorityCheck === "function") {//缺少1个参数,我们兼容缺少blankBookAuthorityCheck的情况
                callback = blankBookAuthorityCheck;
                blankBookAuthorityCheck = null;
                baseInvokeByFrames("pdf-sign_PdfCheck", {"fileContent": fileContent}, callback);
            } else {
                throw new Error("cusPdfContentCheck的调用参数非法!");
            }
        },
        /**
         * PDF格式检查 本地文件的PDF
         * @param filePath 文件全路径
         * @param cookies Http地址需要的Cookies
         * @param blankBookAuthorityCheck 字体或空白页检查未通过,是否视为检查失败.
         * @param callback
         */
        cusPdfFileCheck: function (filePath, cookies, blankBookAuthorityCheck, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-sign_PdfCheck", {
                    "filePath": filePath,
                    "cookies": cookies,
                    "blankBookAuthorityCheck": blankBookAuthorityCheck
                }, callback);
            } else if ((!callback) && typeof blankBookAuthorityCheck === "function") {//缺少1个参数,我们兼容缺少blankBookAuthorityCheck的情况
                callback = blankBookAuthorityCheck;
                blankBookAuthorityCheck = null;
                baseInvoke("pdf-sign_PdfCheck", {
                    "filePath": filePath,
                    "cookies": cookies
                }, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少2个参数,我们兼容缺少blankBookAuthorityCheck和cookies的情况
                callback = cookies;
                cookies = null;
                baseInvoke("pdf-sign_PdfCheck", {
                    "filePath": filePath
                }, callback);
            } else {
                throw new Error("cusPdfFileCheck的调用参数非法!");
            }
        },
        /**
         * Base64编码的PDF验签名(使用方正组件)
         * @param fileContent Base64编码的PDF文件
         * @param blankBookAuthorityCheck 字体或空白页检查未通过,是否视为检查失败.
         * @param callback
         */
        cusPdfContentCheckSign: function (fileContent, blankBookAuthorityCheck, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvokeByFrames("pdf-sign_PdfCheckSign", {
                    "fileContent": fileContent,
                    "blankBookAuthorityCheck": blankBookAuthorityCheck
                }, callback);
            } else if ((!callback) && typeof blankBookAuthorityCheck === "function") {//缺少1个参数,我们兼容缺少blankBookAuthorityCheck的情况
                callback = blankBookAuthorityCheck;
                blankBookAuthorityCheck = null;
                baseInvokeByFrames("pdf-sign_PdfCheckSign", {"fileContent": fileContent}, callback);
            } else {
                throw new Error("cusPdfContentCheckSign的调用参数非法!");
            }
        },
        /**
         * PDF文件验签名(使用方正组件)
         * @param filePath 文件全路径
         * @param cookies Http地址需要的Cookies
         * @param blankBookAuthorityCheck 字体或空白页检查未通过,是否视为检查失败.
         * @param callback
         */
        cusPdfFileCheckSign: function (filePath, cookies, blankBookAuthorityCheck, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-sign_PdfCheckSign", {
                    "filePath": filePath,
                    "cookies": cookies,
                    "blankBookAuthorityCheck": blankBookAuthorityCheck
                }, callback);
            } else if ((!callback) && typeof blankBookAuthorityCheck === "function") {//缺少1个参数,我们兼容缺少cookies的情况
                callback = blankBookAuthorityCheck;
                blankBookAuthorityCheck = null;
                baseInvoke("pdf-sign_PdfCheckSign", {"filePath": filePath, "cookies": cookies}, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少1个参数,我们兼容缺少cookies的情况
                callback = cookies;
                cookies = null;
                baseInvoke("pdf-sign_PdfCheckSign", {"filePath": filePath}, callback);
            } else {
                throw new Error("cusPdfSignCheckSign的调用参数非法!");
            }
        },
        /**
         * Base64编码的PDF内容签章
         * @param fileContent Base64编码的PDF文件
         * @param datetiem 签章时间戳
         * @param Edoc_code edoc类型编码
         * @param Edoc_Name edoc类型名称
         * @param passwd 卡密码
         * @param callback
         */
        cusPdfContentAddSign: function (fileContent, datetiem, Edoc_code, Edoc_Name, passwd, callback) {//destDir,fileName,
            //少1个参数,认为没传userpin,少两个,认为没传认为没传userpin和Edoc_Name,保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvokeByFrames("pdf-sign_PdfAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvokeByFrames("pdf-sign_PdfAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvokeByFrames("pdf-sign_PdfAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
            }
        },
        /**
         * 对PDF文件签章,支持本地和Http
         * @param filePath
         * @param fileName
         * @param datetiem 签章时间戳
         * @param Edoc_code edoc类型编码
         * @param Edoc_Name edoc类型名称
         * @param cookies
         * @param passwd 卡密码
         * @param callback
         */
        cusPdfFileAddSign: function (filePath, fileName, datetiem, Edoc_code, Edoc_Name, cookies, passwd, callback) {//destDir,fileName,
            //少1个参数,认为没传passwd;少2个,认为没传认为没传cookies和passwd;少3个,认为没传认为没传Edoc_Name/cookies和userpin.保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-sign_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "cookies": cookies,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("pdf-sign_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "cookies": cookies
                }, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少2个参数,我们兼容缺少cookies和passwd的情况
                callback = cookies;
                cookies = null;
                baseInvoke("pdf-sign_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少3个参数,我们兼容缺少Edoc_Name/cookies和passwd的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("pdf-sign_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
            }
        },
        /**
         * 对本地PDF文件签章,并覆盖之...
         * @param filePath
         * @param Edoc_code edoc类型编码
         * @param Edoc_Name edoc类型名称
         * @param datetiem 签章时间戳
         * @param passwd 卡密码
         * @param callback 回调函数
         */
        cusFilePdfAddSign: function (filePath, Edoc_code, Edoc_Name, datetiem, passwd, callback) {
            //少1个参数,认为没传passwd;少2个,认为没传认为没传datetiem和passwd.保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("pdf-sign_FilePdfAddSign", {
                    "filePath": filePath,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "datetiem": datetiem,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("pdf-sign_FilePdfAddSign", {
                    "filePath": filePath,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "datetiem": datetiem
                }, callback);
            } else if ((!callback) && typeof datetiem === "function") {//缺少2个参数,我们兼容缺少cookies和passwd的情况
                callback = datetiem;
                datetiem = null;
                baseInvoke("pdf-sign_FilePdfAddSign", {
                    "filePath": filePath,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
            }
        },
        /**
         * PDF批量检查
         * @param args
         * @param callback
         */
        cusPdfFileBatchCheck: function (args, callback) {
            baseInvoke("pdf-sign_PdfFileBatchCheck", args, callback);
        },
        /**
         * PDF文件批量加签名,加签之前不检查文件
         * @param args
         * @param callback
         */
        cusPdfFileBatchAddSign: function (args, callback) {//args为js对象,不是json字符串!
            baseInvoke("pdf-sign_PdfFileBatchAddSign", args, callback);
        },
        /**
         * PDF文件批量检查和加签名
         * @param args
         * @param callback
         */
        cusPdfFileBatchCheckAndSign: function (args, callback) {//args为js对象,不是json字符串!
            baseInvoke("pdf-sign_PdfFileBatchCheckAndSign", args, callback);
        },
        /**
         * Xml中的的PDF内容加签名,需要的信息也全部在Xml中的节点上保存.
         * @param filePath Xml文件的本地路径
         * @param FileContent_NodeName PDF内容节点路径
         * @param Edoc_code_NodeName EDoc_code的节点路径
         * @param Edoc_Name_NodeName Edoc_Name的节点路径,可以为空
         * @param datetiem 加签时间,可以为空
         * @param replace [Y/N]是否替换原文件,可为空,默认值N
         * @param suffix_rule 不替换源文件的情况下,新的文件保存的后缀的规则
         * @param outDir 输出目录
         * @param passwd 卡的密码
         * @param callback
         * @author 王立鹏
         */
        cusXmlPdfFileAddSign: function (filePath, FileContent_NodeName, Edoc_code_NodeName, Edoc_Name_NodeName, datetiem, replace, suffix_rule, outDir, passwd, callback) {
            baseInvoke("pdf-sign_PdfXmlAddSign", {
                "filePath": filePath,
                "FileContent_nodepath": FileContent_NodeName,
                "Edoc_code_nodepath": Edoc_code_NodeName,
                "Edoc_Name_nodepath": Edoc_Name_NodeName,
                "datetiem": datetiem,
                "replace": replace,
                "suffix_rule": suffix_rule,
                "outDir": outDir,
                "passwd": passwd
            }, callback);
        },
        /**
         *  批量对Xml中的的PDF内容加签名,需要的信息也全部在Xml中的节点上保存.
         * @param args :{
         *   passwd:"卡密码",
         *   FileContent_nodepath:"Xml中的数据节点位置",
         *   datetiem:"加签时间,可以为空",
         *   Edoc_code_nodepath:"Edoc_code的节点位置.",
         *   Edoc_Name_nodepath:"Edoc_Name的节点位置.",
         *   rootPath:"输入文件的基础目录,可以为空",
         *   replace:"Y/N是否覆盖原文件",
         *   suffix_rule:"后缀规则,如果填写,则修改Xml后缀,放到源文件目录中.仅当replace=N的时候生效 ",
         *   outDir:"输出目录. 仅当replace=N的时候生效",
         *   filesInfo:[
         *       {filePath:"XML文件全路径1"},
         *       {filePath:"XML文件全路径2"}
         *   ]
         *}
         * @param callback
         */
        cusXmlPdfBatchFileAddSign: function (args, callback) {
            baseInvoke("pdf-sign_PdfXmlBatchAddSign", args, callback);
        },
        /**
         * 对整个目录中的全部Xml中的的PDF内容加签名,需要的信息也全部在Xml中的节点上保存.
         * @param rootPath Xml文件所在的目录
         * @param FileContent_NodeName PDF内容节点路径
         * @param Edoc_code_NodeName EDoc_code的节点路径
         * @param Edoc_Name_NodeName Edoc_Name的节点路径,可以为空
         * @param datetiem 加签时间,可以为空
         * @param replace [Y/N]是否替换原文件,可为空,默认值N
         * @param suffix_rule 不替换源文件的情况下,新的文件保存的后缀的规则
         * @param outDir 输出目录
         * @param passwd 卡的密码
         * @param callback
         * @author 王立鹏
         */
        cusXmlPdfDirAddSign: function (rootPath, FileContent_NodeName, Edoc_code_NodeName, Edoc_Name_NodeName, datetiem, replace, suffix_rule, outDir, passwd, callback) {
            baseInvoke("pdf-sign_PdfXmlDirAddSign", {
                "rootPath": rootPath,
                "FileContent_nodepath": FileContent_NodeName,
                "Edoc_code_nodepath": Edoc_code_NodeName,
                "Edoc_Name_nodepath": Edoc_Name_NodeName,
                "datetiem": datetiem,
                "replace": replace,
                "suffix_rule": suffix_rule,
                "outDir": outDir,
                "passwd": passwd
            }, callback);
        },


        /*----------------------------------PDFES的签章插件-------------------------------------------------------------*/
        /**
         * 提取印章图片
         * @param callback
         */
        pdfesExtractSeal: function (callback) {
            baseInvoke("pdf-es_ExtractSeal", {}, callback);
        },
        /**
         * 提取手签员签名图片
         * @param callback
         */
        pdfesExtractAutograph: function (callback) {
            baseInvoke("pdf-es_ExtractAutograph", {}, callback);
        },
        /**
         * 设置服务器URL地址
         * @param url url内容，string类型
         * @param callback
         */
        pdfesSetServerUrl: function (url, callback) {
            baseInvoke("pdf-es_SetServerUrl", {"url": url}, callback);
        },
        /**
         * 验证文档所有签章
         * @param fileData 待验签文档内容，base64编码的字符串
         * @param callback
         */
        pdfesVerifySeal: function (sessionId, fileData, callback) {
            baseInvokeByFrames("pdf-es_VerifySeal", {"sessionId": sessionId, "fileData": fileData}, callback);
        },
        /**
         * 电子签章
         * @param orignFile 待签章文件内容数据，base64编码的字符串
         * @param seSeal 印章数据内容，base64编码的字符串
         * @param cert 签章人证书，base64编码的字符串
         * @param left 签章距文件左边界距离，数值类型
         * @param top 签章距文件上边界距离，数值类型
         * @param callback
         */
        /*
       pdfesSign: function (sessionId, orignFile, seSeal, cert, left, top, pageIndex, callback) {
           baseInvokeByFrames("pdf-es_Sign",(!!pageIndex) ? {
               "sessionId": sessionId,
               "orignFile": orignFile,
               "seSeal": seSeal,
               "cert": cert,
               "left": left,
               "top": top,
               "pageIndex" : pageIndex
           } : {
               "sessionId": sessionId,
               "orignFile": orignFile,
               "seSeal": seSeal,
               "cert": cert,
               "left": left,
               "top": top
           }, callback);
       },*/
        pdfesSign: function (sessionId, orignFile, seSeal, cert, left, top, pageIndex, callback) {
            if (callback && typeof callback == "function") {
                baseInvokeByFrames("pdf-es_Sign", {
                    "sessionId": sessionId,
                    "orignFile": orignFile,
                    "seSeal": seSeal,
                    "cert": cert,
                    "left": left,
                    "top": top,
                    "pageIndex": pageIndex
                }, callback);
            } else if ((!callback) && typeof pageIndex === "function") {
                callback = pageIndex;
                pageIndex = null;
                baseInvokeByFrames("pdf-es_Sign", {
                    "sessionId": sessionId,
                    "orignFile": orignFile,
                    "seSeal": seSeal,
                    "cert": cert,
                    "left": left,
                    "top": top
                }, callback);
            } else {
                throw new Error("pdfesSign的调用参数非法!");
            }
        },
        /**
         * 电子签章
         * @param orignFile 待签章文件内容数据，base64编码的字符串
         * @param seSeal 印章数据内容，base64编码的字符串
         * @param cert 签章人证书，base64编码的字符串
         * @param left 签章距文件左边界距离，数值类型
         * @param top 签章距文件上边界距离，数值类型
         * @param callback
         */
        pdfesSpcSign: function (sessionId, orignFile, seSeal, cert, left, top, passwd, callback) {
            if (callback && typeof callback == "function") {
                baseInvokeByFrames("pdf-es_SpcSign", {
                    "sessionId": sessionId,
                    "orignFile": orignFile,
                    "seSeal": seSeal,
                    "cert": cert,
                    "left": left,
                    "top": top,
                    "passwd": passwd
                }, callback);
            } else {
                throw new Error("pdfesSpcSign的调用参数非法,请输出密码!");
            }
        },

        /*----------------------------------JSCA签名接口--------------------------------------------------------------*/

        /**
         * 签名,返回base64的原文、base64的证书、base64的签名值
         * @param inData 原文
         * @param passwd 卡密码
         * @param callback
         */
        jscaSignData: function (inData, passwd, callback) {
            if (callback && typeof callback === "function") {
                baseInvoke("jsca_JSCASignData", {
                    "inData": inData,
                    "passwd": passwd
                }, callback);
            } else if (!(callback) && typeof passwd === "function") {
                callback = passwd;
                passwd = null;
                baseInvoke("jsca_JSCASignData", {
                    "inData": inData
                }, callback);
            }
        },
        /**
         * 验证江苏CA卡密码,仅仅验证密码,不自动开关卡~
         * @param passwd 密码
         * @param callback
         */
        jscaVerifyPIN: function (passwd, callback) {
            baseInvoke("jsca_JSCAVerifyPIN", {"passwd": passwd}, callback);
        },
        /**
         * 打开卡
         * @param callback
         */
        jscaInitEnv: function (callback) {
            baseInvoke("jsca_JSCAInitEnv", callback);
        },
        /**
         * 开卡测试
         * @param callback
         */
        jscaInitEnvALL: function (callback) {
            baseInvoke("jsca_JSCAInitEnvALL", callback);
        },
        /**
         * 关闭卡
         * @param callback
         */
        jscaClearEnv: function (callback) {
            baseInvoke("jsca_JSCAClearEnv", callback);
        },
        /*----------------------------------TXRN国税接口---------------------------------------------------------------*/
        /**
         * 签名接口
         * @param callback
         */
        txrnSignData: function (inData, passwd, authXml, callback) {
            baseInvoke("txrn_TXRNSignData", {
                "inData": inData,
                "passwd": passwd,
                "authXml": authXml
            }, callback);
        },
        /**
         * 签名接口TXRNSignNoHash
         * @param callback
         */
        txrnSignDataNoHash: function (inData, passwd, callback) {
            baseInvoke("txrn_TXRNSignNoHash", {
                "inData": inData,
                "passwd": passwd
            }, callback);
        },
        /**
         * 获取进项数据接口
         * @param callback
         */
        txrnGetInvoiceData: function (passwd, strBeginTime, strEndTime, authXml, callback) {
            baseInvoke("txrn_TXRNgetInvoiceData", {
                "passwd": passwd,
                "strBeginTime": strBeginTime,
                "strEndTime": strEndTime,
                "authXml": authXml
            }, callback);
        },
        /**
         * 获取发票信息数据接口
         * @param callback
         */
        txrnGetFpDetailData: function (passwd, authXml, callback) {
            baseInvoke("txrn_TXRNgetFpDetailData", {
                "passwd": passwd,
                "authXml": authXml,
                "requestXml": requestXml
            }, callback);
        },
        /*----------------------------------商务部安全功能---------------------------------------------------------------*/
        /**
         * 判断是否插入了UKey
         * @param callback
         */
        mofcomDetectKey: function (callback) {
            baseInvoke("mofcom_DetectKey", {}, callback);
        },
        /**
         * 获取Usb-Key的序列号(ID)
         */
        mofcomGetFTKeySN: function (callback) {
            baseInvoke("mofcom_GetFTKeySN", {}, callback);
        },
        /**
         * 获取选用的证书下标
         * @param callback
         */
        mofcomGetSelectedCertIndex: function (callback) {
            baseInvoke("mofcom_GetSelectedCertIndex", {}, callback);
        },
        /**
         * 证书列表
         * @param callback
         */
        mofcomListCertsNames: function (callback) {
            baseInvoke("mofcom_ListCertsNames", {}, callback);
        },
        /**
         * 设置选用证书
         * @param index
         * @param callback
         */
        mofcomSetSelectedCertIndex: function (index, callback) {
            baseInvoke("mofcom_SetSelectedCertIndex", {"index": index}, callback);
        },
        /**
         * 验证证书密码
         * @param passwd
         * @param callback
         */
        mofcomCheckUsbKeyPin: function (passwd, callback) {
            baseInvoke("mofcom_CheckUsbKeyPin", {"passwd": passwd}, callback);
        },
        /**
         * 获取证书
         * @param callback
         */
        mofcomGetEncodedCert: function (callback) {
            baseInvoke("mofcom_GetEncodedCert", {}, callback);
        },
        /**
         * 获取证书信息
         * @param callback
         */
        mofcomGetCertInfo: function (callback) {
            baseInvoke("mofcom_GetCertInfo", {}, callback);
        },
        /**
         * 对信息加签
         * @param message 待签名的内容
         * @param callback
         */
        mofcomSignMessage: function (message, callback) {
            baseInvoke("mofcom_SignMessage", {"message": message}, callback);
        },
        /**
         * 信息批量签名
         * @param messages
         * @param callback
         */
        mofcomBatchSignMessage: function (messages, callback) {
            baseInvokeByFrames("mofcom_BatchSignMessage", {"messages": messages || []}, callback);
        },
        /**
         * 验证签名
         * @param p7message P7签名数据
         * @param message 原始数据
         * @param callback
         */
        mofcomVerifyMsg: function (p7message, message, callback) {
            baseInvoke("mofcom_VerifyMsg", {"p7message": p7message, "message": message}, callback);
        },
        /**
         * 批量验证签名
         * @param p7messages P7签名数据数组
         * @param messages 原始数据数组
         * @param callback
         */
        mofcomBatchVerifyMsg: function (p7messages, messages, callback) {
            baseInvokeByFrames("mofcom_BatchVerifyMsg", {
                "p7messages": p7messages || [],
                "messages": messages || []
            }, callback);
        },
        /**
         * 签名本地文件
         * @param inFile 文件全路径
         * @param callback
         */
        mofcomEnvelopedSignFile: function (inFile, callback) {
            baseInvoke("mofcom_EnvelopedSignFile", {"inFile": inFile}, callback);
        },
        /**
         * 验签本地文件
         * @param p7message P7签名信息
         * @param inFile 本地文件
         * @param callback
         */
        mofcomEnvelopedVerifyFile: function (p7message, inFile, callback) {
            baseInvoke("mofcom_EnvelopedVerifyFile", {"p7message": p7message, "inFile": inFile}, callback);
        },
        /*----------------------------------RA控件功能-----------------------------------------------------------------*/
        /**
         * 检查读卡器类型
         * @param callback
         */
        raIs903Reader: function (callback) {
            baseInvoke("ra_Is903Reader", callback);
        },
        /**
         * 根据读卡器类型获取卡信息，传密码则验证密码，不传不进行验证
         * @param callback
         */
        raVerifyPinAndGetCardUserInfo: function (readertype, userpin, callback) {
            if (!callback && typeof userpin === "function") {
                callback = userpin;
                userpin = null;
                baseInvoke("ra_VerifyPinAndGetCardUserInfo", {"readertype": readertype}, callback);
            } else {
                baseInvoke("ra_VerifyPinAndGetCardUserInfo", {"readertype": readertype, "userpin": userpin}, callback);
            }
        },
        /**
         * 打开卡
         * @param callback
         */
        raInitCpuCard: function (callback) {
            baseInvoke("ra_InitCpuCard", callback);
        },
        /**
         * 关闭卡
         * @param callback
         */
        raCloseCpuCard: function (callback) {
            baseInvoke("ra_CloseCpuCard", callback);
        },
        /**
         * 格式化卡
         * @param callback
         */
        raFormatCpuCard: function (callback) {
            baseInvoke("ra_FormatCpuCard", callback);
        },
        /**
         * 发卡初始化
         * @param raClearCardPWD 清卡口令，制新卡为空，更新证书不为空
         * @param callback
         */
        raInitMakeCard: function (raClearCardPWD, callback) {
            baseInvoke("ra_InitMakeCard", {"raClearCardPWD": raClearCardPWD}, callback);
        },
        /**
         * 验证口令
         * @param passwd 密码
         * @param callback
         */
        raverifyPassWD: function (passwd, callback) {
            baseInvoke("ra_verifyPassWD", {"passwd": passwd}, callback);
        },
        /**
         * 修改口令
         * @param userPWD 旧口令
         * @param newUserPWD 新口令
         * @param callback
         */
        rachangePasswd: function (userPWD, newUserPWD, callback) {
            baseInvoke("ra_changePasswd", {"userPWD": userPWD, "newUserPWD": newUserPWD}, callback);
        },
        /**
         * 解锁
         * @param unlockPWD 解锁密码
         * @param newUserPWD 新密码
         * @param callback
         */
        raunlockPasswd: function (unlockPWD, newUserPWD, callback) {
            baseInvoke("ra_unlockPasswd", {"unlockPWD": unlockPWD, "newUserPWD": newUserPWD}, callback);
        },
        /**
         * 取控件版本
         * @param callback
         */
        raGetOcxVersion: function (callback) {
            baseInvoke("ra_GetOcxVersion", callback);
        },
        /**
         * 取服务器证书
         * @param certName 服务器证书名
         * @param callback
         */
        ragetServerCert: function (certName, callback) {
            baseInvoke("ra_getServerCert", {"certName": certName}, callback);
        },
        /**
         * 取被制卡的信息
         * @param callback
         */
        raGetCard2UserInfo: function (callback) {
            baseInvoke("ra_GetCard2UserInfo", callback);
        },
        /**
         * 取卡内的用户信息
         * @param callback
         */
        ragetItemUserInfo: function (callback) {
            baseInvoke("ra_getItemUserInfo", callback);
        },
        /**
         * 获取被制卡的IC卡号，与卡上印刷的一致;未制证的可以获取
         * @param lReader 取指定卡的信息，1取RA卡，其它值则取被制卡或KEY
         * @param callback
         */
        raGetCardIDFromCard: function (lReader, callback) {
            baseInvoke("ra_GetCardIDFromCard", {"lReader": lReader}, callback);
        },
        /**
         * 取卡类型，调用该函数之前无须打开卡
         * @param lReader 取指定卡的信息，1取RA卡，其它值则取被制卡
         * @param callback
         */
        raGetGetICCType: function (lReader, callback) {
            baseInvoke("ra_GetGetICCType", {"lReader": lReader}, callback);
        },
        /**
         * 取算法类型（取加密算法标识）
         * @param lReader 取指定卡的信息，1取RA卡，其它值则取被制卡或KEY
         * @param callback
         */
        raGetCryptAlgo: function (lReader, callback) {
            baseInvoke("ra_GetCryptAlgo", {"lReader": lReader}, callback);
        },
        /**
         * 写卡 接收数据并验证写卡，调用该函数之前必须调用InitMakeCard函数
         * @param SignCertPEM 签名证书
         * @param EnvCertPEM 加密证书
         * @param CAEnvKeyPEM CA加密公钥，PEM编码格式
         * @param CASignKeyPEM CA签名公钥，PEM编码格式
         * @param KmsKeyPEM KMS公钥，PEM编码格式
         * @param EnvDataPEM 私钥信封，PEM编码格式
         * @param CardInfoPEM 卡信息，是PEM编码后的数据
         * @param AdminPassWd 管理员口令
         * @param callback
         */
        raWriteCpuCard: function (SignCertPEM, EnvCertPEM, CAEnvKeyPEM, CASignKeyPEM, KmsKeyPEM, EnvDataPEM, CardInfoPEM, AdminPassWd, callback) {
            baseInvoke("ra_WriteCpuCard", {
                "SignCertPEM": SignCertPEM,
                "EnvCertPEM": EnvCertPEM,
                "CAEnvKeyPEM": CAEnvKeyPEM,
                "CASignKeyPEM": CASignKeyPEM,
                "KmsKeyPEM": KmsKeyPEM,
                "EnvDataPEM": EnvDataPEM,
                "CardInfoPEM": CardInfoPEM,
                "AdminPassWd": AdminPassWd
            }, callback);
        },
        /**
         * 签名(自动验密、关卡)
         * @param inData 签名数据
         * @param passwd 用户密码
         * @param callback
         */
        raDeclareData: function (inData, passwd, callback) {
            baseInvoke("ra_DeclareData", (!!passwd) ? {
                "inData": inData,
                "passwd": passwd
            } : {"inData": inData}, callback);
        },
        /**
         * 身份认证第一步
         * @param strInputDecodePEM 服务器产生的随机数，是PEM编码的数据
         * @param callback
         */
        raidentifyFirstStep: function (strInputDecodePEM, passwd, callback) {
            baseInvoke("ra_identifyFirstStep", {"strInputDecodePEM": strInputDecodePEM, "passwd": passwd}, callback);
        },
        /*----------------------------------旧版过时的功能功能------------------------------------------------------------*/
        /**
         * 启动微软ClickOnes程序
         * @param qpurl
         * @param casurl
         * @param passwd
         * @param callback
         * @deprecated
         */
        startClickOnceApp: function (qpurl, casurl, passwd, callback) {
            baseInvoke("base_StartClickOnceApp", {"qpurl": qpurl, "casurl": casurl, "passwd": passwd}, callback);
        },
        //设置加密设备的优先级
        spcSetInitEnvPriority: function (szInitEnvPriority, callback) {
            baseInvoke("security_SpcSetInitEnvPriority", {"szInitEnvPriority": szInitEnvPriority}, callback);
        },
        //打开卡
        spcInitEnvEx: function (callback) {
            baseInvoke("security_SpcInitEnvEx", callback);
        },
        //关闭卡
        spcClearEnv: function (callback) {
            baseInvoke("security_SpcClearEnv", callback);
        },
        //下面是客户端控件组件的调用接口
        login: function (passwd, callback) {
            baseInvoke("security_login", {"passwd": passwd}, callback);
        },
        signData: function (passwd, inputData, callback) {
            baseInvoke("security_signData", {"passwd": passwd, "inputData": inputData}, callback);
        },
        spcSignData: function (inData, callback) {
            baseInvoke("security_SpcSignData", {"inData": inData}, callback);
        },
        spcSignDataAsPEM: function (inData, callback) {
            baseInvoke("security_SpcSignDataAsPEM", {"inData": inData}, callback);
        },
        spcSignPemDataAsPEM: function (inData, callback) {
            baseInvoke("security_SpcSignPemDataAsPem", {"inData": inData}, callback);
        },
        spcSignDataNoHash: function (inData, callback) {
            baseInvoke("security_SpcSignDataNoHash", {"inData": inData}, callback);
        },
        spcSignDataNoHashAsPEM: function (inData, callback) {
            baseInvoke("security_SpcSignDataNoHashAsPEM", {"inData": inData}, callback);
        },
        spcVerifySignData: function (certDataPEM, inData, signData, callback) {
            baseInvoke("security_SpcVerifySignData", {
                "certDataPEM": certDataPEM,
                "inData": inData,
                "signData": signData
            }, callback);
        },
        spcVerifySignDataNoHash: function (certDataPEM, inData, signData, callback) {
            baseInvoke("security_SpcVerifySignDataNoHash", {
                "certDataPEM": certDataPEM,
                "inData": inData,
                "signData": signData
            }, callback);
        },
        spcSHA1Digest: function (szInfo, callback) {
            baseInvoke("security_SpcSHA1Digest", {"szInfo": szInfo}, callback);
        },
        spcSHA1DigestAsPEM: function (szInfo, callback) {
            baseInvoke("security_SpcSHA1DigestAsPEM", {"szInfo": szInfo}, callback);
        },

        spcGetCardID: function (callback) {
            baseInvoke("security_SpcGetCardID", callback);
        },
        spcGetCertNo: function (callback) {
            baseInvoke("security_SpcGetCertNo", callback);
        },
        spcGetUName: function (callback) {
            baseInvoke("security_SpcGetUName", callback);
        },
        spcGetEntID: function (callback) {
            baseInvoke("security_SpcGetEntID", callback);
        },
        spcGetEntName: function (callback) {
            baseInvoke("security_SpcGetEntName", callback);
        },
        spcGetEntMode: function (callback) {
            baseInvoke("security_SpcGetEntMode", callback);
        },
        spcGetCardUserInfo: function (callback) {
            baseInvoke("security_SpcGetCardUserInfo", callback);
        },
        getGetCardUserInfoAll: function (userpin, callback) { //新增 by wlp 2017年11月15日,2017年11月29日 增加userpin
            if (!callback && typeof userpin === "function") {
                callback = userpin;
                userpin = null;
                baseInvoke("security_getGetCardUserInfoAll", {}, callback);
            } else {
                baseInvoke("security_getGetCardUserInfoAll", {"userpin": userpin}, callback);
            }
        },
        spcGetCardAttachInfo: function (callback) {
            baseInvoke("security_SpcGetCardAttachInfo", callback);
        },
        spcGetCardAttachInfoAsPEM: function (callback) {
            baseInvoke("security_SpcGetCardAttachInfoAsPEM", callback);
        },
        spcVerifyPIN: function (passwd, callback) {
            baseInvoke("security_SpcVerifyPIN", {"passwd": passwd}, callback);
        },
        spcChangePIN: function (oldPin, newPin, callback) {
            baseInvoke("security_SpcChangePIN", {"oldPin": oldPin, "newPin": newPin}, callback);
        },
        spcEncodePEM: function (inData, callback) {
            baseInvoke("security_SpcEncodePEM", {"inData": inData}, callback);
        },
        spcDecodePEM: function (inData, callback) {
            baseInvoke("security_SpcDecodePEM", {"inData": inData}, callback);
        },
        spcDecodePEMAsString: function (inData, callback) {
            baseInvoke("security_SpcDecodePEMAsString", {"inData": inData}, callback);
        },
        spcGetSignCert: function (callback) {
            baseInvoke("security_SpcGetSignCert", callback);
        },
        spcGetSignCertAsPEM: function (callback) {
            baseInvoke("security_SpcGetSignCertAsPEM", callback);
        },
        spcGetEnvCert: function (callback) {
            baseInvoke("security_SpcGetEnvCert", callback);
        },
        spcGetEnvCertAsPEM: function (callback) {
            baseInvoke("security_SpcGetEnvCertAsPEM", callback);
        },
        spcGetValidTimeFromCert: function (certByteArray, callback) {
            if (typeof certByteArray === "function") {
                callback = certByteArray;
                certByteArray = null;
            }
            baseInvoke("security_SpcGetValidTimeFromCert", ((!!certByteArray) ? {"certByteArray": certByteArray} : {}), callback);
        },
        spcGetValidTimeAll: function (callback) { //2017年11月01日 增加 by 王立鹏
            baseInvoke("security_getValidTimeAll", {}, callback);
        },
        spcSignRandom: function (passwd, random, callback) {
            baseInvoke("security_SpcSignRandom", {"passwd": passwd, "random": random}, callback);
        },
        spcRSAEncrypt: function (inData, certData, passwd, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_SpcRSAEncrypt", {
                    "inData": inData,
                    "certData": certData,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("security_SpcRSAEncrypt", {
                    "inData": inData,
                    "certData": certData
                }, callback);
            } else {
                throw new Error("spcRSAEncrypt的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        spcRSADecrypt: function (inData, passwd, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_SpcRSADecrypt", {
                    "inData": inData,
                    "passwd": passwd
                }, callback);
            } else if ((!callback) && typeof passwd === "function") {//缺少1个参数,我们兼容缺少passwd的情况
                callback = passwd;
                passwd = null;
                baseInvoke("security_SpcRSADecrypt", {
                    "inData": inData
                }, callback);
            } else {
                throw new Error("spcRSAEncrypt的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        pdfSignCheckSign: function (fileContent, callback) {
            baseInvokeByFrames("security_PdfSignCheckSign", {"fileContent": fileContent}, callback);
        },
        localPdfSignCheckSign: function (filePath, callback) {
            baseInvoke("security_PdfSignCheckSign", {"filePath": filePath}, callback);
        },
        pdfSignCheck: function (fileContent, callback) {
            baseInvokeByFrames("security_PdfSignCheck", {"fileContent": fileContent}, callback);
        },
        localPdfSignCheck: function (filePath, callback) {
            baseInvoke("security_PdfSignCheck", {"filePath": filePath}, callback);
        },
        pdfAddSign: function (fileContent, datetiem, Edoc_code, Edoc_Name, userpin, callback) {//destDir,fileName,
            //少1个参数,认为没传userpin,少两个,认为没传认为没传userpin和Edoc_Name,保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvokeByFrames("security_PdfAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "userpin": userpin
                }, callback);
            } else if ((!callback) && typeof userpin === "function") {//缺少1个参数,我们兼容缺少userpin的情况
                callback = userpin;
                userpin = null;
                baseInvokeByFrames("security_PdfAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少1个参数,我们兼容缺少userpin的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvokeByFrames("security_PdfAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        localPdfAddSign: function (filePath, fileName, datetiem, Edoc_code, Edoc_Name, cookies, userpin, callback) {//destDir,fileName,
            //少1个参数,认为没传userpin;少2个,认为没传认为没传cookies和userpin;少3个,认为没传认为没传Edoc_Name/cookies和userpin.保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "cookies": cookies,
                    "userpin": userpin
                }, callback);
            } else if ((!callback) && typeof userpin === "function") {//缺少1个参数,我们兼容缺少userpin的情况
                callback = userpin;
                userpin = null;
                baseInvoke("security_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "cookies": cookies
                }, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少2个参数,我们兼容缺少cookies和userpin的情况
                callback = cookies;
                cookies = null;
                baseInvoke("security_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少3个参数,我们兼容缺少Edoc_Name/cookies和userpin的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("security_PdfAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        pdfCheckAndAddSign: function (fileContent, datetiem, Edoc_code, Edoc_Name, callback) {//destDir,fileName,
            //少1个参数,认为没传destDir,少两个,认为没传认为没传destDir和Edoc_Name,保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvokeByFrames("security_PdfCheckAndAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少1个参数,我们兼容缺少Edoc_Name的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvokeByFrames("security_PdfCheckAndAddSign", {
                    "fileContent": fileContent,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        localPdfCheckAndAddSign: function (filePath, fileName, datetiem, Edoc_code, Edoc_Name, cookies, callback) {//destDir,fileName,
            //少1个参数,认为没传cookies,少两个,认为没传认为没传cookies和Edoc_Name,保证callback必须得有!
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_PdfCheckAndAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "cookies": cookies
                }, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少1个参数,我们兼容缺少Edoc_Name的情况
                callback = cookies;
                cookies = null;
                baseInvoke("security_PdfCheckAndAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少2个参数,我们兼容缺少cookies和Edoc_Name的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("security_PdfCheckAndAddSign", {
                    "filePath": filePath,
                    "fileName": fileName,
                    "datetiem": datetiem,
                    "Edoc_code": Edoc_code
                }, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        pdfScanInit: function (callback) {
            baseInvoke("security_PdfScanScanInitialize", {}, callback);
        },
        pdfScanAsPEM: function (Edoc_code, Edoc_Name, userpin, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_PdfScanAsPEM", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "userpin": userpin
                }, callback);
            } else if ((!callback) && typeof userpin === "function") {//缺少1个参数,我们兼容缺少userpin的情况
                callback = userpin;
                userpin = null;
                baseInvoke("security_PdfScanAsPEM", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少2个参数,我们兼容缺少userpin和Edoc_Name的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("security_PdfScanAsPEM", {"Edoc_code": Edoc_code}, callback);
            }
        },
        pdfScan: function (Edoc_code, Edoc_Name, userpin, callback) {
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_PdfScan", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name,
                    "userpin": userpin
                }, callback);
            } else if ((!callback) && typeof userpin === "function") {//缺少1个参数,我们兼容缺少userpin的情况
                callback = userpin;
                userpin = null;
                baseInvoke("security_PdfScan", {
                    "Edoc_code": Edoc_code,
                    "Edoc_Name": Edoc_Name
                }, callback);
            } else if ((!callback) && typeof Edoc_Name === "function") {//缺少2个参数,我们兼容缺少userpin和Edoc_Name的情况
                callback = Edoc_Name;
                Edoc_Name = null;
                baseInvoke("security_PdfScan", {"Edoc_code": Edoc_code}, callback);
            }
        },
        pdfView: function (fileContent, callback) {//destDir,fileName,
            baseInvokeByFrames("security_PdfView", {"fileContent": fileContent}, callback);
        },
        localPdfView: function (filePath, cookies, callback) {//filePath可以是本地的file:,也可以是http:的
            if (callback && typeof callback === "function") {//完整参数
                baseInvoke("security_PdfView", {"filePath": filePath, "cookies": cookies}, callback);
            } else if ((!callback) && typeof cookies === "function") {//缺少1个参数,我们兼容缺少title的情况
                callback = cookies;
                cookies = null;
                baseInvoke("security_PdfView", {"filePath": filePath}, callback);
            } else {
                throw new Error("addSign的调用参数非法!");
                //alert("addSign的调用参数非法");
            }
        },
        localPdfBatchCheckAndSign: function (args, callback) {//args为js对象,不是json字符串!
            baseInvoke("security_LocalPdfBatchCheckAndSign", args, callback);
        },
        localPdfBatchUpload: function (args, callback) {
            baseInvoke("security_LocalPdfBatchUpload", args, callback);
        },
        deleteSignedTempFile: function (args, callback) {
            baseInvoke("security_DeleteSignedTempFile", args, callback);
        },

        //下面是单一窗口组件的调用接口
        swcLogin: function (passwd, callback) {
            baseInvoke("swc_security_login", {"passwd": passwd}, callback);
        },
        swcPostData: function (data, callback, method) {
            conn();
            method = (method || "swc_postdata");
            callbacks[method] = callback;
            var _data = {"_method": method};
            _data["_id"] = id++;
            if (typeof data === "object") {
                _data["args"] = toJson(data);
            } else {
                _data["args"] = data;
            }
            var s = toJson(_data);
            //2016年10月27日 分块改为顺序传输
            //校验码
            var checkCode = DIGEST.CheckCode(s);
            //分包的组
            var guid = getGuid();
            while (blockData[guid]) {
                //存在则重新生成一个!
                guid = getGuid();
            }
            //数据分块
            var splitData = splitStrData(s);
            //如果需要分包传输
            if (splitData.length > 1) {
                blockData[guid] = {checkcode: checkCode, totalLength: s.length, retry: 0, block: splitData};
            }
            //传第一个包
            //报头(纯文本类型)含义
            //"BLOCKTXT"+总校验码(4字节)+块校验码(4字节)+数据总大小(16字节)+当前块大小(8字节)+是否分块/分为几块(4字节)+数据分块组号(36字节)+数据包号(4字节)+保留位(44字节),总计128字长
            //        8               12              16                 32                40                       44                   80              84            128
            var blockCheckCode = DIGEST.CheckCode(splitData[0]);
            var pakHeaser = getDataHeader(checkCode, blockCheckCode, s.length, splitData[0].length, splitData.length, guid, 0);
            var msg = pakHeaser + splitData[0];
            sendMessage(msg);
        },
        data: function () {
        }
    };
    //单一窗口组件兼容调用..
    EportClient.data.prototype = {
        Execute: function (callback) {
            var d = toJson(this);
            EportClient.swcPostData(d, callback);
        }
    };
})(window, document, navigator);
