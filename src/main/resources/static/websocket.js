/**
 * 用户是否已经点击登陆按钮
 */
var isLoginButtonPressed;

/**
 * 密码节点
 */

var upnode = document.getElementById("password");

/**
 * 显示提示信息的节点
 */
var infonode;

/**
 * 初始化函数，做一些页面初始信息的获取
 */
function init(i) {
    var steupinfo = document.getElementById("setupinfo");
    if (steupinfo != undefined) {
        document.body.removeChild(steupinfo);
    }
    isLoginButtonPressed = false;
    syncStatus();//同步状态信息
    if (!isInstalled()) {
        return;
    }
    upnode = document.getElementById("password");
    try {
        upnode.focus();
    } catch (e) {

    }
}

/**
 * 封装获取元素节点的value属性值
 * 输入参数可以是1-n个节点的id属性值
 * 返回值是一个元素节点或一个包含所有元素节点的value值的数组
 */
function $V() {
    var values = [];
    for (var i = 0; i < arguments.length; i++) {
        var value = arguments[i];
        if (typeof value == 'string') {
            var tempnode = document.getElementById(value);
            if (tempnode == null || !(tempnode.value)) {
                value = null;
            } else {
                value = document.getElementById(value).value;
            }
        }
        if (arguments.length == 1) {
            return value;
        }
        values.push(value);
    }
    return values;
}

/**
 * 新建一个文本节点
 * @param content 文本节点内容
 * @return 文本节点
 */
function $CT(content) {
    return document.createTextNode(content);
}


/**
 * 封装获取元素节点中包含的唯一文本节点内容的方法
 * 输入参数可以是1-n个节点的id属性值
 * 返回值是一个元素节点或一个包含所有元素节点内部的唯一为本节点内容的数据
 */
function $T() {
    var texts = [];
    for (var i = 0; i < arguments.length; i++) {
        var text = arguments[i];
        if (typeof text == 'string') {
            var temp = document.getElementById(text);
            if (temp == null) {
                text = null;
            } else {
                var tempnode = document.getElementById(text).firstChild;
                if (tempnode == null) {
                    text = null;
                } else {
                    text = tempnode.nodeValue;
                }
            }
        }
        if (arguments.length == 1) {
            return text;
        }
        texts.push(text);
    }
    return texts;
}


/**
 * 键盘按下时处理事件
 */
function keyp(event) {
    upnode = document.getElementById("password");
    try {
        wt(document.getElementById("password"));
    } catch (e) {
    }

    var ev;
    if (event) {
        ev = event;
    } else {
        ev = window.event;
    }
    isLoginButtonPressed == false;
    if (ev.keyCode == 13) {

        doCAIdentify();
    }
}

function keypNoCa(event) {
    var ev;
    if (event) {
        ev = event;
    } else {
        ev = window.event;
    }
    if (ev.keyCode == 13) {
        doIdentify();
    }
}

function doIdentify() {
    if (isLoginButtonPressed == true) {
        return;
    }
    isLoginButtonPressed = true;
    infonode = document.getElementById("infoNoCa");
    if (infonode != null) {
        infonode.style.display = "none";
        infonode.style.backgroundColor = "";
        //infonode.css("display","none");
        //infonode.css("backgroundColor","")
        infonode.innerHTML = "";
    }
    //showinfo();
    username = $V("usernameNoCa");
    if (username == null || username.length < 1 || username == "请输入登录账号") {
        clearinfo();
        alert("用户名不允许为空");
        $("#username").focus();
        isLoginButtonPressed = false;
        return;
    }
    userpassword = $V("passwordNoCa");
    if (userpassword == null || userpassword.length < 1) {
        clearinfo();
        alert("密码不允许为空");
        $("#passwordNoCa").focus();
        isLoginButtonPressed = false;
        return;
    }

    var verifyCode = $V("verifyCode");
    if (verifyCode == null || verifyCode.length < 1) {
        clearinfo();
        alert("验证码不允许为空");
        $("#verifyCode").focus();
        isLoginButtonPressed = false;
        return;
    }


    $("#swy").val(username);
    if (plainCode == null) {
        $("#swm").val(hex_md5(userpassword));
    } else {
        $("#swm").val(userpassword);
    }

    try {
        chkmm(userpassword);
    } catch (exception) {
    }

    //$("#fmNoCa").submit();
    document.getElementById("fmNoCa").submit();
}


/**
 * 用户提交登陆认证请求入口
 */
function doCAIdentify() {
    if (isLoginButtonPressed == true) {
        return;
    }
    isLoginButtonPressed = true;
    infonode = document.getElementById("info");
    if (infonode != null) {
        infonode.style.display = "none";
        infonode.style.backgroundColor = "";
        infonode.innerHTML = "";
    }
    showinfo();
    userpassword = $V("password");
    if (userpassword == null || userpassword.length < 1) {
        clearinfo();
        alert("IC卡/Key密码不允许为空");
        upnode = $("password");
        upnode.value = "";
        upnode.focus();
        isLoginButtonPressed = false;
        return;
    }
//		setStatus("开始进行密码验证...");
    checkPwd(userpassword);
}


/**
 * 清除登录提示信息
 */
function clearinfo() {
    if (infonode != null) {
        infonode.style.display = "none";
        infonode.style.backgroundColor = "";
        infonode.innerHTML = "";
    }
}

//校验密码
function checkPwd(strPwd) {
    var cPw = ["0", "1", "2", "3", "4", "5", "6", "7", "8"]; //密码组
    var Max = 8;
    if (strPwd == "") {
        alert("您输入的IC卡/Key密码不能为空");
        isLoginButtonPressed = false;
        return -1;
    }
    for (i = 0; i <= Max; i++) {
        if (strPwd == cPw[i]) {
            alert("请正确输入8位IC卡/Key密码");
            clearinfo();
            upnode = $("password");
            upnode.value = "";
            upnode.focus();
            isLoginButtonPressed = false;
            return -2;
        }
    }

    //判读读卡器类型
    EportClient.isInstalledTest(EportClient.raIs903Reader, function (msg, msgJson) {
        RAIs903Reader(msg, msgJson, strPwd);
    });


    //初始化环境
    //EportClient.spcInitEnvEx(HandleCheckInsCardResult);
    //新版本：只用一个接口 xiejh
    //EportClient.isInstalledTest(EportClient.spcSignRandom, strPwd, document.getElementById("random").value, function (msg) {
    //if (msg.Result) {
    //    document.getElementById("Textarea11").value = JSON.stringify(msg);
    //} else {
    //    document.getElementById("Textarea11").value = "";
    //    alertErrMsg(msg);
    //}

    //HandleSignResult(msg);
    //});

}

//检查是否插卡了，使用取签名证书来验证
function HandleCheckInsCardResult(msg) {
    if (msg.Result) {
        EportClient.spcGetSignCert(HandleCheckInsCardStausResult);
    } else {
        if (JSON.stringify(msg.Error).indexOf("51590") > 0
            || JSON.stringify(msg.Error).indexOf("50200") > 0) {
            alert("请插入IC卡或者Key!");
        } else {
            alert(msg.Error);
        }

        isLoginButtonPressed = false;
        EportClient.spcClearEnv();
    }
}

//检查是否插卡了，结果处理
function HandleCheckInsCardStausResult(msg) {
    if (msg.Result) {
        HandleVerifyPINResult(msg);
    } else {
        if (msg.Error && msg.Error != "") {
            if (JSON.stringify(msg.Error).indexOf("51590") > 0
                || JSON.stringify(msg.Error).indexOf("50200") > 0) {
                alert("请插入IC卡或者Key");
            } else {
                alert(msg.Error);
            }
            clearinfo();
            isLoginButtonPressed = false;
            EportClient.spcClearEnv();
        }
    }
}

//检查Pin密码
function HandleVerifyPINResult(msg) {
    var strPwd = $V("password");
    EportClient.spcVerifyPIN(strPwd, HandleCheckPwdResult);
}

//处理验证密码结果
function HandleCheckPwdResult(msg) {
    if (msg.Result) {
        EportClient.spcSignPemDataAsPEM($V("random"), HandleSignResult)
    } else {
        if (msg.Error && msg.Error != "") {
            alert(msg.Error);
            clearinfo();
            upnode.value = "";
            upnode.focus();
            isLoginButtonPressed = false;

        } else {
            window.opener = null;
            window.open('', '_self', '');
            window.close();
        }
    }
}


//处理签名结果
function HandleSignResult(msg) {
    if (msg.Result) {
        //$("signData").value=(msg.Data)[0];
        document.getElementById("signData").value = (msg.Data)[0];
        //EportClient.spcGetCardUserInfo(HandleCardUerInfoResult);
        EportClient.getGetCardUserInfoAll($V("password"), HandleCardUerInfoResult);
    } else {
        if (msg.Error && msg.Error != "") {
            alert(msg.Error);
            clearinfo();
            isLoginButtonPressed = false;
            upnode.value = "";
            upnode.focus();

        } else {
            window.opener = null;
            window.open('', '_self', '');
            window.close();
        }
    }
}

//处理用户信息
function HandleCardUerInfoResult(msg) {
    if (msg.Result) {
        //密码验证通过后，查看证书是否过期
        getValidTimeFromCert(msg);


    } else {
        if (msg.Error && msg.Error != "") {
            alert(msg.Error);
            clearinfo();
            upnode.value = "";
            upnode.focus();
            isLoginButtonPressed = false;

        } else {
            window.opener = null;
            window.open('', '_self', '');
            window.close();
        }
    }
}

/**
 * 显示登录提示信息
 */
function showinfo() {
    //infonode = $("info");
    infonode = document.getElementById("info");
    if (infonode != null) {
        try {
            infonode.style.display = "block";
            infonode.style.backgroundColor = "yellow";
            var text = $CT("登录中，请稍等");
            infonode.appendChild(text);
        } catch (ex) {
            //alert(ex);
        }
    }
}

/**
 * 检查是否安装控件
 */
function isInstalled() {
    res = false;
    EportClient.isInstalled("iKey", function (msg) {
        if (msg.Result) {
            //已经安装完成了控件
            //document.getElementById("download_widget").style.display = "none";
            res = true;
        } else {
            if (msg.Error && msg.Error != "") {
                document.getElementById("password").value = ""
                document.getElementById("password").disabled = "true";
                isLoginButtonPressed = false;
                if ("undefined" == typeof delayAlertSetupControl) {
                    if (confirm("请下载安装IC卡/Key客户端控件，否则，将无法使用IC卡/Key")) {
                        downEportIkeySetup();
                    }
                } else {
                    delayAlertSetupControl();
                }
            } else {
                window.opener = null;
                window.open('', '_self', '');
                window.close();
            }
            res = false;
        }
    }, "http://iKey下载地址")
    return res;
}

/**
 * 单向认证标志
 */
var UNILATERAL_TYPE = "1";

/**
 * 同步状态信息：将$("status")展现到 $("info")里
 */
function syncStatus() {
    var errmsg = document.getElementById("status");
    if (errmsg != null) {
        infonode = document.getElementById("info");
        infonode.style.display = "block";
        var errmsgValue = $T("status");
        var err0 = errmsgValue.split("|")[0];
        var err1 = errmsgValue.split("|")[1];
        if (err0 == "5") {
            errmsgValue = "卡信息异常，请尝试用证书延期方式更正!";
            if (document.getElementById('error_attach_info') != null) {
                errmsgValue = errmsgValue + "\n("
                    + document.getElementById('error_attach_info').innerText + ")";
            }
        } else if (err0 == "4") {
            errmsgValue = errmsgValue.substring(2) + document.getElementById('error_black_info').innerText;
        } else if (err0 == "0") {
            errmsgValue = document.getElementById('server_error_para').innerText;
        } else if (err0 == "6") {
            errmsgValue = err1 + "(" + document.getElementById('server_error_para').innerText + ")";
        }
        var text = $CT(errmsgValue);
        infonode.appendChild(text);
        infonode.style.display = "block";
        infonode.style.backgroundColor = "yellow";
    }
}

/**
 * 下载控件
 */
function downEportIkeySetup() {
    window.open(SwVersion.getIkeyDownloadUrl(), '_blank');
}

//开始登录
function startLogin(msg) {
    var ary = [];
    var user = (msg.Data)[0];
    ary = user.split("||");
    //$("icCard").value=ary[5];
    //$("certNo").value=ary[0];

    //$("fm1").submit();

    document.getElementById("icCard").value = ary[5];
    document.getElementById("certNo").value = ary[0];
    try {
        userpassword = tomf(userpassword);
    } catch (exception) {
    }

    try {
        sbPre();
    } catch (e) {
    }

    document.getElementById("userPin").value = userpassword;
    document.getElementById("fm1").submit();

    //EportClient.spcClearEnv();
}

function tomf(value) {
    //var stkey = document.getElementById("lt").value;
    //stkey= stkey.substring(0,16);
    //var ivec = stkey;
    //var s4=new SM4Util(stkey,ivec);
    //return s4.encryptData_CBC(value);
    var userSmDt = "";
    var objSm = document.getElementById("user_sm_dt");
    if (objSm != null && typeof (objSm) != undefined) {
        userSmDt = objSm.innerText;
    }
    var res = value;
    if (userSmDt != "") {
        res = CaData_KXC(value, userSmDt); //20211022
        setFormValue("sgm", "1");
    }
    return res;
}

function setFormValue(name, val) {
    var obj = document.getElementsByName(name);
    if (obj.length == 1) {
        document.getElementById(name).value = val;
    } else {
        var n = 0;
        for (n = 0; n < obj.length; n++) {
            obj[n].value = val;
        }
    }
}

var errorTipDate = 30;
var symbol1 = "/";
var symbol2 = ":";
var symbol3 = " ";

/**
 * 查看证书有效期
 *
 */
function getValidTimeFromCert(param) {
    EportClient.isInstalledTest(EportClient.cusSpcGetValidTimeFromCert, function (msg, msgJson) {
        if (msg && !msg.Result) {
            clearinfo();
            return;
        }
        var szEndTime = msg.Data[0].szEndTime;//20190120195948

        var endTime = changeDateForm(szEndTime);//"2019/01/20 10:15:00";
        if (!endTime) {
            clearinfo();
            return;
        }

        var overDueDate = getOverDueDate(endTime);
        if (overDueDate > 0 && overDueDate <= errorTipDate) {
            alert("您的IC卡还有" + overDueDate + "天到期，请尽快延期!");
            startLogin(param);
            return;
        }
        if (overDueDate <= 0) {
            alert("您的IC卡已过期!");
            clearinfo();
            return;
        }
        startLogin(param);
    });
}

/**
 * 改变日期格式
 *"2019/01/20 10:15:00";
 */
function changeDateForm(endTime) {//20190114195948
    if (endTime.length != 14) {
        return "";
    }
    var year = endTime.substring(0, 4);
    var month = endTime.substring(4, 6);
    var date = endTime.substring(6, 8);
    var hour = endTime.substring(8, 10);
    var minute = endTime.substring(10, 12);
    var second = endTime.substring(12, 14);

    return year + symbol1 + month + symbol1 + date + symbol3 + hour + symbol2 + minute + symbol2 + second;

}

/**
 * 获取过期 天数
 */
function getOverDueDate(endTime) {//"2018-01-20 10:15:00"
    //结束时间
    // 2019/01/20 10:15:00
    endStr = (endTime).replace(/-/g, "/");//一般得到的时间的格式都是：yyyy-MM-dd hh24:mi:ss，所以我就用了这个做例子，是/的格式，就不用replace了。
    var endDate = new Date(endStr);//将字符串转化为时间
    //开始时间
    var staDate = new Date();
    var num = (endDate - staDate) / (1000 * 3600 * 24);//求出两个时间的时间差，这个是天数
    var days = parseInt(Math.ceil(num));//转化为整天（小于零的话就不用转了）
    return days;
}


/**
 *20191210 添加判断白读卡器新增接口  by lyy
 */


/**
 * 判读读卡器类型
 */
function RAIs903Reader(msg, msgJson, strPwd) {
    if (msg.Result) {
        if ((msg.Data)[0] == "1") {
            identifyFirstStep(strPwd);
        } else if ((msg.Data)[0] == "0") {
            identifyOldStep(strPwd);
        }
    } else {
        identifyOldStep(strPwd);
    }
}

/**
 * 原流程
 */
function identifyOldStep(strPwd) {
    EportClient.isInstalledTest(EportClient.spcSignRandom, strPwd, document.getElementById("random").value, function (msg) {
        HandleSignResult(msg);
    });
}

/**
 * 白读卡器身份认证第一步
 */
function identifyFirstStep(strPwd) {
    //身份认证第一步
    EportClient.isInstalledTest(EportClient.raidentifyFirstStep, document.getElementById("random").value, strPwd, function (msg, msgJson) {
        RAIs903Result(msg, msgJson);
    });
}

function RAIs903Result(msg, msgJson) {
    if (msg.Result) {
        document.getElementById("signData").value = (msg.Data)[1];
        VerifyPinAndGetCardUserInfo();
    } else {
        isLoginButtonPressed = false;
        alert(msg.Error);
    }
}

function VerifyPinAndGetCardUserInfo() {
    //校验密码获取当前卡信息
    EportClient.isInstalledTest(EportClient.raVerifyPinAndGetCardUserInfo, "1", $V("password"), function (msg, msgJson) {
        RAIs903GetCert(msg, msgJson);
    });
}

/**
 * 903获取证书
 */
function RAIs903GetCert(msg, msgJson) {
    if (msg.Result) {
        if ((msg.Data)[1].VerifyPINSuccess) {
            var ary = [];
            var user = (msg.Data)[0];
            ary = user.split("||");
            var szEndTime = ary[12];//20190120195948
            var endTime = changeDateForm(szEndTime);//"2019/01/20 10:15:00";
            if (!endTime) {
                clearinfo();
                return;
            }

            var overDueDate = getOverDueDate(endTime);
            if (overDueDate > 0 && overDueDate <= errorTipDate) {
                alert("您的IC卡还有" + overDueDate + "天到期，请尽快延期!");
                startLogin(msg);
                return;
            }
            if (overDueDate <= 0) {
                alert("您的IC卡已过期!");
                clearinfo();
                return;
            }
            startLogin(msg);
        } else {
            isLoginButtonPressed = false;
            alert((msg.Data)[1].Error);
        }
    } else {
        isLoginButtonPressed = false;
        alert(msg.Error);
    }
}

