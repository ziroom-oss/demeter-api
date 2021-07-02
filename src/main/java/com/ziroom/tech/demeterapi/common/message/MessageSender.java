package com.ziroom.tech.demeterapi.common.message;

import java.util.List;

/**
 * Created by liangrk on 2021/3/19.
 */
public interface MessageSender {

    void send(String content, List<String> userList);

}
