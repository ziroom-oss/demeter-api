package com.ziroom.tech.demeterapi.message.enums;

/**
 * 消息渠道枚举
 * @author xuzeyu
 */
public enum MessageChannelEnum {
    CONSOLE_CHANNEL(0, "控制台测试"),
    WECHAT_CHANNEL(1, "企业微信"),
    DINGDING_CHANNEL(2, "钉钉机器人"),
    ;
    private final Integer type;
    private final String desc;

    MessageChannelEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }


}
