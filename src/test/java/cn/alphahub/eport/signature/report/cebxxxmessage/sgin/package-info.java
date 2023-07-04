/**
 * package-info.java
 * 放在和实体同一个包路径下
 */
@XmlSchema(xmlns = {
        @XmlNs(prefix = "ds", namespaceURI = NAMESPACE_DS_URI),
})
package cn.alphahub.eport.signature.report.cebxxxmessage.sgin;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_DS_URI;




