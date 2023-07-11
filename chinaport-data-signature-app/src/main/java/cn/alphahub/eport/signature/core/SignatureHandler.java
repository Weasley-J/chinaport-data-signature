package cn.alphahub.eport.signature.core;

import cn.alphahub.dtt.plus.util.SpringUtil;
import cn.alphahub.eport.signature.base.exception.SignException;
import cn.alphahub.eport.signature.config.SignatureAlgorithmProperties;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.util.XMLUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.utils.IgnoreAllErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * 末三加密核心方法
 * <p>华讯云的商业机密，价值5000RMB的解决方案</p>
 *
 * @author 刘雯静
 * @version 1.0
 * @date 2022/2/11
 */
@Slf4j
public final class SignatureHandler {

    @SuppressWarnings("restriction")
    private static final IgnoreAllErrorHandler IGNORE_ALL_ERROR_HANDLER = new IgnoreAllErrorHandler();

    private static final DocumentBuilderFactory documentBuilderFactory;

    private static Transformer transFormer;

    static {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(true);
        try {
            //spring中已在org.springframework.boot.ApplicationRunner子类初始化过，防止普通类调用出错
            org.apache.xml.security.Init.init();
            transFormer = TransformerFactory.newInstance().newTransformer();
        } catch (Exception e) {
            log.error("TransFormer init false. Error {}", e.getMessage(), e);
        }
    }

    private SignatureHandler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取发送给U-KEY加签的xml的ds:Signature节点内容
     *
     * @param request 原文信息
     * @return SignatureValue的发送给U-KEY加签的xml
     * @see SignatureHandler#getDigestValueOfCEBXxxMessage(String) 不含ds:Signature节点的digestValue的计算方法
     */
    @SneakyThrows
    public static String getSignatureValueBeforeSend(SignRequest request) {

        //completely disable external entities declarations:
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.setErrorHandler(IGNORE_ALL_ERROR_HANDLER);
        Document document = documentBuilder.parse(new InputSource(new StringReader(request.getData())));

        // 原始xml摘要
        DOMSource ds = new DOMSource(document);
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transFormer.transform(ds, sr);

        Canonicalizer canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
        canon.canonicalize(sw.toString().getBytes(), new ByteArrayOutputStream(), true);

        String digestValue = getDigestValueOfCEBXxxMessage(request.getData());

        // document增加ds:Signature节点
        createSignatureElement(document, digestValue);

        // 读取签名段的报文
        NodeList nodeList = document.getElementsByTagName("ds:SignedInfo");
        Node node = nodeList.item(0);
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        canon.canonicalizeSubtree(node, writer);
        String canonicalizeStr = writer.toString(StandardCharsets.UTF_8);

        log.warn("总署XML加签报文:\n{}", canonicalizeStr);

        return canonicalizeStr;
    }

    /**
     * sign digest value with c14n
     *
     * @param sourceXml 源CEBXxxMessage,不含ds:Signature节点的<ceb:CEBXxxMessage></ceb:CEBXxxMessage>源xml
     * @return digest value
     */
    public static String getDigestValueOfCEBXxxMessage(@NotBlank String sourceXml) {
        Document doc;
        try (InputStream is = new ByteArrayInputStream(sourceXml.getBytes(StandardCharsets.UTF_8))) {
            doc = XMLUtils.read(is, false);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            org.apache.xml.security.utils.XMLUtils.outputDOMc14nWithComments(doc, bos);
            String res = bos.toString(StandardCharsets.UTF_8);
            byte[] sha1 = DigestUtils.sha1(res);
            String base64String = Base64.encodeBase64String(sha1);
            log.info("c14n source xml result, sha1: {}, base64: {}", sha1, base64String);
            return base64String;
        } catch (Exception e) {
            log.error("使用c14n格式化xml异常 {}", e.getMessage(), e);
            throw new SignException("使用c14n格式化xml异常: " + e.getMessage());
        }
    }

    /**
     * 创建签名节点，初始化摘要信息
     *
     * @param document    xml document
     * @param digestValue digestValue
     */
    private static void createSignatureElement(Document document, String digestValue) {
        Element canonicalizationMethodElement = document.createElement("ds:CanonicalizationMethod");
        canonicalizationMethodElement.setAttribute("Algorithm", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
        canonicalizationMethodElement.setTextContent("");
        Element signatureMethodElement = document.createElement("ds:SignatureMethod");
        signatureMethodElement.setAttribute("Algorithm", getSignatureMethodAlgorithm());
        signatureMethodElement.setTextContent("");

        Element transformElement = document.createElement("ds:Transform");
        transformElement.setAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        transformElement.setTextContent("");

        Element transformsElement = document.createElement("ds:Transforms");
        transformsElement.appendChild(transformElement);

        //初始化摘要信息
        Element digestValueElement = document.createElement("ds:DigestValue");
        digestValueElement.setTextContent(digestValue);

        Element digestMethodElement = document.createElement("ds:DigestMethod");
        digestMethodElement.setAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
        Element referenceElement = document.createElement("ds:Reference");
        referenceElement.setAttribute("URI", "");
        referenceElement.appendChild(transformsElement);
        referenceElement.appendChild(digestMethodElement);
        referenceElement.appendChild(digestValueElement);

        Element signedInfoElement = document.createElement("ds:SignedInfo");
        signedInfoElement.setAttribute("xmlns:ceb", "http://www.chinaport.gov.cn/ceb");
        signedInfoElement.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        signedInfoElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

        signedInfoElement.appendChild(canonicalizationMethodElement);
        signedInfoElement.appendChild(signatureMethodElement);
        signedInfoElement.appendChild(referenceElement);

        Element signatureValueElement = document.createElement("ds:SignatureValue");
        signatureValueElement.setTextContent("signatureValue");

        Element x509Certificate = document.createElement("ds:X509Certificate");
        x509Certificate.setTextContent("");

        Element x509DataElement = document.createElement("ds:X509Data");
        x509DataElement.appendChild(x509Certificate);

        Element keyNameElement = document.createElement("ds:KeyName");
        keyNameElement.setTextContent("");

        Element keyInfoElement = document.createElement("ds:KeyInfo");
        keyInfoElement.appendChild(keyNameElement);
        keyInfoElement.appendChild(x509DataElement);

        Element signatureElement = document.createElement("ds:Signature");
        signatureElement.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        signatureElement.appendChild(signedInfoElement);
        signatureElement.appendChild(signatureValueElement);
        signatureElement.appendChild(keyInfoElement);

        Element rootElement = document.getDocumentElement();
        rootElement.appendChild(signatureElement);
    }

    /**
     * 动态推断XML代码段中<ds:SignatureMethod></ds:SignatureMethod>算法的值
     * <pre>
     *  {@code <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#${algorithmType}"/>}
     * </pre>
     *
     * @return XML代码段中s:SignatureMethod的算法值
     * @apiNote 优先取配置文件的算法值，若无配置，自动推断签名算法兜底
     */
    public static String getSignatureMethodAlgorithm() {
        CertificateHandler certificateHandler = SpringUtil.getBean(CertificateHandler.class);
        SignatureAlgorithmProperties algorithmProperties = SpringUtil.getBean(SignatureAlgorithmProperties.class);
        if (null != algorithmProperties.getAlgorithm()) {
            return algorithmProperties.getAlgorithm().getXmlAlgorithmValue();
        }
        return certificateHandler.getAlgorithm().getXmlAlgorithmValue();
    }
}
