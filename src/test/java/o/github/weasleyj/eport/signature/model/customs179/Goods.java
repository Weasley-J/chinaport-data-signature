package o.github.weasleyj.eport.signature.model.customs179;

import lombok.Data;

import java.io.Serializable;

@Data
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
