package com.ziroom.tech.demeterapi;

import com.ziroom.tech.demeterapi.common.message.WorkWechatSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageSendTest {

    @Autowired
    WorkWechatSender workWechatSender;

    @Test
    public void test() {
        workWechatSender.send("测试", Arrays.asList("daijk"));
    }

}
