/**
 * 和CEBXxxMessage实体同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ceb", namespaceURI = NAMESPACE_CEB_URI),
        @XmlNs(prefix = "xsi", namespaceURI = NAMESPACE_XSI_URI),
        @XmlNs(prefix = "ds", namespaceURI = NAMESPACE_DS_URI),
})
package o.github.weasleyj.eport.sign.model.cebmessage;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_CEB_URI;
import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_DS_URI;
import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_XSI_URI;



