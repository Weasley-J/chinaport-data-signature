package cn.alphahub.eport.signature.report.cebxxxmessage.util;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class GUIDUtil {
    public static final String ORDERPUSH = "311";
    public static final String INVENTORY = "621";

    public static final String CEB311MESSAGE = "CEB311Message";
    public static final String CEB621MESSAGE = "CEB621Message";

    /**
     * 获取指定时间毫秒值
     */
    public static Long getMxNum(int length) {
        String s = "0";
        for (int i = 0; i < length; i++) {
            s += "9";
        }
        return Long.valueOf(s);
    }

    /**
     * 补全自增的数据
     */
    public static String getSequence(long seq, int length) {
        String str = String.valueOf(seq);
        int len = str.length();
        // 取决于业务规模,应该不会到达4
        if (len >= length) {
            return str;
        }
        int rest = length - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 获取GUID
     *
     * @param prefix 拼接头
     * @param key    key
     * @param length 生成数字位数，最大18位
     * @return
     */
    public String getDayIncrCode(String prefix, String key, int length) {
        if (length >= 19) {
            throw new RuntimeException("已超出最大位数");
        }
        String code = "CEB" + prefix + "_HNZB_FXJK_";
        String formatDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long liveNum = getMxNum(length);
        Long incre = IdUtil.getSnowflake().getDataCenterId(666);
        String sequence = getSequence(incre, length);
        code = code + formatDay + "_" + sequence;
        return code;
    }

}
