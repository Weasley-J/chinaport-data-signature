/**
 * package-info.java
 * 放在和实体同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ceb", namespaceURI = NAMESPACE_CEB_URI),
        @XmlNs(prefix = "xsi", namespaceURI = NAMESPACE_XSI_URI),
        @XmlNs(prefix = "ds", namespaceURI = "http://www.w3.org/2000/09/xmldsig#"),
})
package cn.alphahub.eport.signature.report.cebxxxmessage.entity;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_CEB_URI;
import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_XSI_URI;



