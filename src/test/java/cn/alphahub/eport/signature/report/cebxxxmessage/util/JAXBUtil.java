package cn.alphahub.eport.signature.report.cebxxxmessage.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Jaxb XML 工具类
 */
@Slf4j
public class JAXBUtil {
    /**
     * JavaBean转换成xml(默认编码UTF-8)
     */
    public static String convertToXml(Object obj) {
        return convertToXml(obj, "UTF-8");
    }

    /**
     * Bean转换成XML
     */
    public static String convertToXml(Object obj, String encoding) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            //格式化xml格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            //去掉生成xml的默认报文头
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            // 由于不能优雅的去掉 standalone="yes" 所以只能去掉整个头部，然后手动插入一个符合条件的头部, 该行为不优雅需要进行升级
            writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("Bean转换成XML异常，obj {}, {}", obj, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * xml转换成JavaBean
     */
    public static <T> T convertToObj(String xml, Class<T> entityClass) {
        try {
            JAXBContext context = JAXBContext.newInstance(entityClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            @SuppressWarnings("unchecked")
            T unmarshal = (T) unmarshaller.unmarshal(new StringReader(xml));
            return unmarshal;
        } catch (Exception e) {
            log.error("xml转换成JavaBean异常 {}, {}", xml, e.getLocalizedMessage(), e);
            return null;
        }
    }
}
