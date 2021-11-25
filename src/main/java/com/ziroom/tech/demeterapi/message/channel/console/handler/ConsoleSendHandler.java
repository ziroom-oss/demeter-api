package com.ziroom.tech.demeterapi.message.channel.console.handler;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.message.channel.console.model.ConsoleMessageModel;
import com.ziroom.tech.demeterapi.message.enums.MessageChannelEnum;
import com.ziroom.tech.demeterapi.message.router.MessageSendHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 控制台测试消息发送
 * @author xuzeyu
 */
@Slf4j
@Component
public class ConsoleSendHandler implements MessageSendHandler {


    @Override
    public Integer support() {
        return MessageChannelEnum.CONSOLE_CHANNEL.getType();
    }

    @Override
    public void sendMessage(String data) {
        log.info("[ConsoleSendHandler] param is {}", JSON.toJSONString(data));
        ConsoleMessageModel consoleMessageModel = JSON.parseObject(data, ConsoleMessageModel.class);
        List<String> toUserList = consoleMessageModel.getToUser();
        for(String toUser: toUserList){
            System.out.println(toUser+"你好"+","+consoleMessageModel.getContext());
        }
    }
}
