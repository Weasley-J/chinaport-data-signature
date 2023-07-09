package o.github.weasleyj.china.eport.sign.model.cebmessage;

import lombok.Data;
import lombok.experimental.Accessors;
import o.github.weasleyj.china.eport.sign.constants.NameSpace;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;


@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Inventory ", namespace = NameSpace.NAMESPACE_CEB_URI)
public class Inventory implements Serializable {

    @XmlElement(name = "InventoryHead", namespace = NameSpace.NAMESPACE_CEB_URI)
    private InventoryHead inventoryHead;

    @XmlElement(name = "InventoryList", namespace = NameSpace.NAMESPACE_CEB_URI)
    private List<InventoryList> inventoryListList;
}
