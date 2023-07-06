package o.github.weasleyj.eport.sign.model.customs179;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayExchangeInfoBody implements Serializable {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商品信息
      */
    private List<Goods> goodsInfo;

    /**
     * 收款账号
     */
    private String recpAccount;

    /**
     * 收款企业代码
     */
    private String recpCode;

    /**
     * 收款企业名称
     */
    private String recpName;
}
