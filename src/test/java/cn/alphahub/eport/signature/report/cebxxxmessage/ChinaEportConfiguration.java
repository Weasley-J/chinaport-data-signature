package cn.alphahub.eport.signature.report.cebxxxmessage;

import cn.alphahub.eport.signature.report.cebxxxmessage.ChinaEportConfiguration.ChinaEportProperties;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * China Eport Configuration
 */
@Configuration
@EnableConfigurationProperties(ChinaEportProperties.class)
public class ChinaEportConfiguration {
    public final static String PREFIX = "third.bridge.china.eport.config";

    /**
     * 跑测试类的时候注意适当修改下这个3个参数
     *
     * @apiNote 缺失{@link ChinaEportProperties}Bean的时候用这个默认的，需要更具自己的实际情况修改
     */
    @Bean
    @ConditionalOnMissingBean({ChinaEportProperties.class})
    public ChinaEportProperties chinaEportProperties() {
        ChinaEportProperties properties = new ChinaEportProperties();
        //传输企业代码-报文传输的企业代码(需要与接入客户端的企 业身份一致)
        properties.setCopCode("传输企业代码");
        //传输企业名称-报文传输的企业名称
        properties.setCopName("传输企业名称");
        //报文传输编号-向中国电子口岸数据中心申请数据交换平台 的用户编号
        properties.setDxpId("报文传输编号");
        return properties;
    }

    @Data
    @ConfigurationProperties(prefix = PREFIX)
    public static class ChinaEportProperties {
        /**
         * 传输企业代码  报文传输的企业代码(需要与接入客户端的企 业身份一致)
         */
        public String copCode;
        /**
         * 传输企业名称  报文传输的企业名称
         */
        public String copName;
        /**
         * 报文传输编号 向中国电子口岸数据中心申请数据交换平台 的用户编号
         */
        public String dxpId;
        public String url;
        public String emsNo;
        public String areaName;
        /**
         * e贸接口域名 推送订单
         */
        private String hainanXCUrl;
        /**
         * 电商平台代码 电商平台的海关注册登记编 号或统一社会信用代码。
         */
        private String ebpCode;
        /**
         * 电商平台名称 电商平台的登记名称。
         */
        private String ebpName;
        /**
         * 电商企业代码 电商企业的海关注册登记编
         * 号或统一社会信用代码，对应 清单的收发货人。
         */
        private String ebcCode;
        /**
         * 电商企业名称 电商企业的登记名称，对应清 单的收发货人。
         */
        private String ebcName;
        /**
         * 币制
         * 海关标准的参数代码 《JGS-20 海关业务代码集》- 货币代码
         * 142 人民币
         */
        private String currency = "142";
        /**
         * 电子订单类型:I进口
         */
        private String orderType = "I";
        /**
         * 担保企业编号
         */
        private String assureCode;
        /**
         * 物流企业代号
         */
        private String logisticsCode;
        private String logisticsName;
        /**
         * 申报企业代码（关）
         */
        private String agentCodeCus;
        /**
         * 申报企业代码（检）
         */
        private String agentCodeCiq;
        /**
         * 贸易方式
         */
        private String tradeMode = "1210";
        private String trafMode;
        /**
         * 运输工具编号
         */
        private String trafNo = "汽车";
        /**
         * 起运国   142中国
         */
        private String country = "142";
        /**
         * 币制（关）
         */
        private String currencyCus = "142";
        /**
         * 币制（检）
         */
        private String currencyCiq = "156";
        /**
         * 区内企业代码
         */
        private String areaCode;
        /**
         * 加密url
         */
        private String signUrl;
        private String payCode;

        private String payName;

        private String payTransactionId;

        /**
         * 查询域名
         */
        private String qeuryUrl;

        /**
         * 311 查询
         */
        private String ceb312Uri = "ceb312msg?dxpid=DXPENT0000458763&qryid=";

        /**
         * 622 查询
         */
        private String ceb622Uri = "ceb312msg?dxpid=DXPENT0000458763&qryid=";

        /**
         * 口岸海关代码
         *
         * @apiNote 商品实际进出我国关境口岸海关 的关区代码，参照JGS/T 18《海关 关区代码》。
         */
        private String portCode;
    }
}
