/**
 * package-info.java
 * 放在和实体同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ceb", namespaceURI = NAMESPACE_CEB_URI),
        @XmlNs(prefix = "xsi", namespaceURI = NAMESPACE_XSI_URI)
})
package cn.alphahub.eport.signature.report.ceb311message.entity;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static cn.alphahub.eport.signature.report.ceb311message.entity.NameSpace.NAMESPACE_CEB_URI;
import static cn.alphahub.eport.signature.report.ceb311message.entity.NameSpace.NAMESPACE_XSI_URI;



