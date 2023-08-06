/**
 * 和Signature xml 节点数据模型同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ds", namespaceURI = NameSpace.NAMESPACE_DS_URI),
})
package io.github.weasleyj.china.eport.sign.model.signature;

import io.github.weasleyj.china.eport.sign.constants.NameSpace;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;




