package cn.alphahub.eport.signature.entity;

import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ukey Response Args Wrapper
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UkeyResponseArgsWrapper {
    private Thread thread;
    private Args responseArgs;
}
