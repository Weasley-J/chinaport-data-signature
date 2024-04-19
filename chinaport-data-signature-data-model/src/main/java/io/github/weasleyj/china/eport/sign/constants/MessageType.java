package io.github.weasleyj.china.eport.sign.constants;

import io.github.weasleyj.china.eport.sign.IMessageType;
import lombok.Getter;

/**
 * CEB Message Type
 *
 * @author weasley
 * @version 1.0.0
 * @apiNote 进口单报文类型：16 类，出口单报文类型：23 类
 */
@SuppressWarnings("all")
public enum MessageType implements IMessageType {
    /* 进口单报文类型 */
    CEB311Message("CEB311Message", IMPORT_MESSAGE),
    CEB312Message("CEB312Message", IMPORT_MESSAGE),
    CEB411Message("CEB411Message", IMPORT_MESSAGE),
    CEB412Message("CEB412Message", IMPORT_MESSAGE),
    CEB511Message("CEB511Message", IMPORT_MESSAGE),
    CEB512Message("CEB512Message", IMPORT_MESSAGE),
    CEB513Message("CEB513Message", IMPORT_MESSAGE),
    CEB514Message("CEB514Message", IMPORT_MESSAGE),
    CEB621Message("CEB621Message", IMPORT_MESSAGE),
    CEB622Message("CEB622Message", IMPORT_MESSAGE),
    CEB623Message("CEB623Message", IMPORT_MESSAGE),
    CEB624Message("CEB624Message", IMPORT_MESSAGE),
    CEB625Message("CEB625Message", IMPORT_MESSAGE),
    CEB626Message("CEB626Message", IMPORT_MESSAGE),
    CEB711Message("CEB711Message", IMPORT_MESSAGE),
    CEB712Message("CEB712Message", IMPORT_MESSAGE),

    /* 出口单报文类型 */
    CEB213Message("CEB213Message", EXPORT_MESSAGE),
    CEB214Message("CEB214Message", EXPORT_MESSAGE),
    CEB215Message("CEB215Message", EXPORT_MESSAGE),
    CEB216Message("CEB216Message", EXPORT_MESSAGE),
    CEB303Message("CEB303Message", EXPORT_MESSAGE),
    CEB304Message("CEB304Message", EXPORT_MESSAGE),
    CEB403Message("CEB403Message", EXPORT_MESSAGE),
    CEB404Message("CEB404Message", EXPORT_MESSAGE),
    CEB505Message("CEB505Message", EXPORT_MESSAGE),
    CEB506Message("CEB506Message", EXPORT_MESSAGE),
    CEB507Message("CEB507Message", EXPORT_MESSAGE),
    CEB508Message("CEB508Message", EXPORT_MESSAGE),
    CEB509Message("CEB509Message", EXPORT_MESSAGE),
    CEB510Message("CEB510Message", EXPORT_MESSAGE),
    CEB603Message("CEB603Message", EXPORT_MESSAGE),
    CEB604Message("CEB604Message", EXPORT_MESSAGE),
    CEB605Message("CEB605Message", EXPORT_MESSAGE),
    CEB606Message("CEB606Message", EXPORT_MESSAGE),
    CEB607Message("CEB607Message", EXPORT_MESSAGE),
    CEB608Message("CEB608Message", EXPORT_MESSAGE),
    CEB701Message("CEB701Message", EXPORT_MESSAGE),
    CEB702Message("CEB702Message", EXPORT_MESSAGE),
    CEB792Message("CEB792Message", EXPORT_MESSAGE),
    ;

    /**
     * 报文类型
     */
    private final String type;

    /**
     * 报文描述
     */
    @Getter
    private final String desc;

    MessageType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    @Override
    public String getType() {
        return type;
    }
}
