package cn.alphahub.eport.signature.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 海关 179 数据抓取
 * <p>
 * 成功数据格式:
 * <pre>
 * {
 *   "code": "10000",
 *   "message": "",
 *   "serviceTime": 1567050097628
 * }
 * </pre>
 *
 * @author weasley
 * @version 1.1.0
 */
@Data
public class Capture179DataResponse implements Serializable {
    private String code;
    private long total;
    private String message;
    private long serviceTime;
}
