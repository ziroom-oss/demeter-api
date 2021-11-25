package com.ziroom.tech.demeterapi.message.event;

import com.ziroom.tech.demeterapi.message.enums.MessageChannelEnum;
import com.ziroom.tech.demeterapi.message.router.MessageChannelEvent;
import lombok.Data;

/**
 * 控制台测试消息事件
 * @author xuzeyu
 */
@Data
public class ConsoleMessageChannelEvent extends MessageChannelEvent {

    public ConsoleMessageChannelEvent() {
        super(MessageChannelEnum.CONSOLE_CHANNEL.getType());
    }
}
