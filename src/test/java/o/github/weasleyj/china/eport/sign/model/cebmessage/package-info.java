/**
 * 和CEBXxxMessage实体同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ceb", namespaceURI = NameSpace.NAMESPACE_CEB_URI),
        @XmlNs(prefix = "xsi", namespaceURI = NameSpace.NAMESPACE_XSI_URI),
        @XmlNs(prefix = "ds", namespaceURI = NameSpace.NAMESPACE_DS_URI),
})
package o.github.weasleyj.china.eport.sign.model.cebmessage;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;



