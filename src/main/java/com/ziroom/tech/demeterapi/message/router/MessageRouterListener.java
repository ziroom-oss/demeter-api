package com.ziroom.tech.demeterapi.message.router;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 消息监听路由
 * @author xuzeyu
 */
@Slf4j
@Component
@AllArgsConstructor
public class MessageRouterListener {

    @Autowired(required = false)
    private List<MessageSendHandler> messageHandlers;

    public static Map<Integer, MessageSendHandler> messageHandlerMap = new HashMap<>();

    @PostConstruct
    public void init() {
        messageHandlers.forEach(handler -> {
            Integer type = handler.support();
            messageHandlerMap.put(type, handler);
        });
    }

    @EventListener
    public void handleMsgEvent(MessageChannelEvent messageChannelEvent) {
        log.info("[MessageRouterListener] handleMsgEvent param is {}", JSON.toJSONString(messageChannelEvent));
        Integer type = messageChannelEvent.getChannlelType();
        MessageSendHandler messageSendHandler = messageHandlerMap.get(type);
        if(Objects.nonNull(messageSendHandler)){
            messageSendHandler.sendMessage(messageChannelEvent.getData());
        }
    }
}
