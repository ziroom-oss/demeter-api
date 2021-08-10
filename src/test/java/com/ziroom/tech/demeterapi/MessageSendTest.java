package com.ziroom.tech.demeterapi;

import com.ziroom.tech.demeterapi.common.EhrApiService;
import com.ziroom.tech.demeterapi.common.message.WorkWechatSender;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req.EhrApiSimpleReq;
import com.ziroom.tech.demeterapi.service.MapService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest(classes = DemeterApiApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MessageSendTest {

    @Autowired
    WorkWechatSender workWechatSender;

    @Autowired
    EhrApiService ehrApiService;

    @Resource
    private PortraitService portraitService;

    @Resource
    private MapService mapService;

    @Test
    public void test() {
        workWechatSender.send("测试", Arrays.asList("daijk"));
    }

    @Test
    public void testWorktop() throws Exception {
        CTOReq ctoReq = new CTOReq();
        portraitService.getWorktopOverview(ctoReq);
    }

    @Test
    public void testCglib() {
        log.info("{}", portraitService.getClass().getName());
    }

    @Test
    public void testSummaryData() {
        mapService.getSkillGraphData();
    }

    @Test
    public void testEhr() {
        EhrApiSimpleReq req = new EhrApiSimpleReq();
        req.setAdCode("daijk");
        ehrApiService.getEmpSimple(req);
    }

}
