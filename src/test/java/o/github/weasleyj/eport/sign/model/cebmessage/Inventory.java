package o.github.weasleyj.eport.sign.model.cebmessage;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_CEB_URI;


@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Inventory ", namespace = NAMESPACE_CEB_URI)
public class Inventory {

    @XmlElement(name = "InventoryHead", namespace = NAMESPACE_CEB_URI)
    private InventoryHead inventoryHead;

    @XmlElement(name = "InventoryList", namespace = NAMESPACE_CEB_URI)
    private List<InventoryList> inventoryListList;
}
