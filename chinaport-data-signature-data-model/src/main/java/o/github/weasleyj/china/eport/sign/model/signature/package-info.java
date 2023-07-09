/**
 * 和Signature xml 节点数据模型同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ds", namespaceURI = NAMESPACE_DS_URI),
})
package o.github.weasleyj.china.eport.sign.model.signature;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static o.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_DS_URI;




