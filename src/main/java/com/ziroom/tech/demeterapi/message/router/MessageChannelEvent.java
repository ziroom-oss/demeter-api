package com.ziroom.tech.demeterapi.message.router;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息事件基类
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public abstract class MessageChannelEvent {

    /**
     * 消息渠道类型
     */
    private Integer channlelType;

    /**
     * 消息体
     */
    private String data;

    public MessageChannelEvent(Integer channlelType) {
        this.channlelType = channlelType;
    }

}
