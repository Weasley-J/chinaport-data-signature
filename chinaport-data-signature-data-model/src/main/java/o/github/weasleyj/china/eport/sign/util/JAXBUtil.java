package o.github.weasleyj.china.eport.sign.util;

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
public final class JAXBUtil {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"%s\"?>\n";

    private JAXBUtil() {
    }

    /**
     * Bean转换成xml()
     *
     * @apiNote 默认编码: UTF-8
     */
    public static String toXml(Object obj) {
        return toXml(obj, "UTF-8");
    }

    /**
     * Bean转换成XML
     */
    public static String toXml(Object obj, String encoding) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            writer.append(String.format(XML_HEADER, encoding));
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
    public static <T> T toBean(String xml, Class<T> entityClass) {
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
