package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 电子口岸X509证书Controller
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
    @Autowired
    private CertificateHandler certificateHandler;

    /**
     * 下载X509证书
     *
     * @apiNote 证书文件格式: 获取证书编号.cer
     */
    @GetMapping("/cert/download")
    public void downloadX509Certificate(HttpServletResponse response) throws IOException {
        Args certNoArgs = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcGetCertNo", new HashMap<>()));
        Args certPEMArgs = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", new HashMap<>()));
        String certName = certNoArgs.getData().get(0);
        String certPomOfUkey = certPEMArgs.getData().get(0);
        String certificateContent = "-----BEGIN CERTIFICATE-----\n"
                .concat(certificateHandler.buildX509Certificate(certPomOfUkey)).concat("\n")
                .concat("-----END CERTIFICATE-----");
        response.setContentType("application/x-x509-ca-cert");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + certName + ".cer\"");
        OutputStream stream = response.getOutputStream();
        stream.write(certificateContent.getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();
    }

}
