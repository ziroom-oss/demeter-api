package com.ziroom.tech.demeterapi.message.channel.console.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 控制台测试消息
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class ConsoleMessageModel {

    /**
     * 消息接收人
     */
    private List<String> toUser;

    /**
     * 消息内容
     */
    private String context;


}
