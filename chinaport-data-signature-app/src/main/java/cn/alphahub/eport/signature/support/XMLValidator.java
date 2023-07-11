package cn.alphahub.eport.signature.support;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * XML校验器
 *
 * @author weasley
 * @since 1.1.0
 */
public final class XMLValidator {
    private static DocumentBuilder documentBuilder;

    static {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); // 防止DTD漏洞
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false); // 防止XXE漏洞
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); // 防止XXE漏洞
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); // 启用安全处理特性
            documentBuilderFactory.setIgnoringElementContentWhitespace(true); // 忽略元素内容中的空白
            documentBuilderFactory.setIgnoringComments(true); // 忽略注释
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // no dump
        }
    }

    private XMLValidator() {
    }

    /**
     * 校验字符串字符串是不是XML
     *
     * @param xmlString XML字符串
     * @return 输入的字符串是否为有效的XML格式
     */
    public static boolean isValidXML(String xmlString) {
        try {
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            Document document = documentBuilder.parse(inputSource);
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

}
