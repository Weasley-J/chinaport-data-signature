package cn.alphahub.eport.signature.report.dock179.entity;

import java.io.Serializable;
import lombok.Data;

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
