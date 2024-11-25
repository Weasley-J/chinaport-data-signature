package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子口岸X509证书
 *
 * @author lwj
 * @version 1.1.0
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport")
public class EportCertController {

    @Autowired
    private SignHandler signHandler;

    /**
     * 下载证书
     *
     * @apiNote 证书文件格式: 证书编号.cer, 遇到项目启动的首页下载证书出现文件名为 unknown.cer 的情况将下载链接复制到浏览器中打开，有公司反馈x509证书导出还存在问题， <a href="http://tool.qdhuaxun.cn/">先去这里导</a> ：海关179号公告对接 => ukey证书导出工具
     * @download
     * @since 20241115
     */
    @GetMapping("/cert/download")
    public void downloadX509Certificate(HttpServletResponse response) throws IOException {
        Args certPEMArgs = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcGetEnvCertAsPEM", new HashMap<>()));
        String x509Certificate = certPEMArgs.getData().get(0);
        Args certNoArgs = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcGetCertNo", new HashMap<>()));
        String certName = certNoArgs.getData().get(0);
        response.setContentType("application/x-x509-ca-cert");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + certName + ".cer\"");
        OutputStream stream = response.getOutputStream();
        stream.write(x509Certificate.getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();
    }

}
