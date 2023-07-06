package o.github.weasleyj.eport.sign.model.cebmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_CEB_URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Order ", namespace = NAMESPACE_CEB_URI)
public class Order implements Serializable {
    @XmlElement(name = "OrderHead", namespace = NAMESPACE_CEB_URI)
    private OrderHead orderHead;

    @XmlElement(name = "OrderList", namespace = NAMESPACE_CEB_URI)
    private List<OrderList> orderList;
}
