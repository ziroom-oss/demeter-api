package com.ziroom.tech.demeterapi.open.message.router;

/**
 * @author xuzeyu
 */
public interface MessageSendHandler {

    Integer support();

    void sendMessage(String data);
}
