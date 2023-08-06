package io.github.weasleyj.china.eport.sign;

import java.io.Serializable;

/**
 * The interface for get message type: CEB311Message|CEB621Message|...
 *
 * @apiNote 获取各类进口单、出口单消息类型
 */
public interface IMessageType extends Serializable {
    /**
     * 进口单报文
     */
    String IMPORT_MESSAGE = "进口单报文";
    /**
     * 出口单报文
     */
    String EXPORT_MESSAGE = "出口单报文";

    /**
     * 消息类型：CEB311Message|CEB621Message|...
     */
    String getType();
}
