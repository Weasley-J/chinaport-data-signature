package cn.alphahub.eport.signature.report.cebxxxmessage.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_CEB_URI;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Inventory ", namespace = NAMESPACE_CEB_URI)
public class Inventory {

    @XmlElement(name = "InventoryHead", namespace = NAMESPACE_CEB_URI)
    private InventoryHead inventoryHead;

    @XmlElement(name = "InventoryList", namespace = NAMESPACE_CEB_URI)
    private List<InventoryList> inventoryListList;
}
