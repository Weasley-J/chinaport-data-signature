package cn.alphahub.eport.signature.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * u-key证书DTO
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpcValidTime implements Serializable {
    /**
     * u-key生效日期（签发日期）
     */
    private LocalDateTime szStartTime;
    /**
     * u-key失效日期
     */
    private LocalDateTime szEndTime;
}
