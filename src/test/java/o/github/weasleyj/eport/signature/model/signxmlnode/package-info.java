/**
 * 和Signature xml 节点数据模型同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ds", namespaceURI = NAMESPACE_DS_URI),
})
package o.github.weasleyj.eport.signature.model.signxmlnode;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static o.github.weasleyj.eport.signature.model.cebmessage.NameSpace.NAMESPACE_DS_URI;




