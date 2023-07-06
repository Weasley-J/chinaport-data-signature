package o.github.weasleyj.eport.sign.model.customs179;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Goods implements Serializable {
    /**
     * 商品名称
     */
    private String gname;

    /**
     * 商品展示链接地址
     */
    private String itemLink;
}
