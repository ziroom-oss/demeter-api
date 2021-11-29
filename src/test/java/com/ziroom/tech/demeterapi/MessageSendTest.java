package com.ziroom.tech.demeterapi;

import com.ziroom.tech.demeterapi.service.MapService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DemeterApiApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MessageSendTest {

    @Resource
    private PortraitService portraitService;

    @Resource
    private MapService mapService;


    @Test
    public void testCglib() {
        log.info("{}", portraitService.getClass().getName());
    }

    @Test
    public void testSummaryData() {
        mapService.getSkillGraphData();
    }



}
