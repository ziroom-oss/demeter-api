package com.ziroom.tech.demeterapi;

import com.ziroom.tech.demeterapi.common.EhrApiService;
import com.ziroom.tech.demeterapi.common.api.EhrEndPoint;
import com.ziroom.tech.demeterapi.common.message.WorkWechatSender;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req.EhrApiSimpleReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.service.DeptRankingService;
import com.ziroom.tech.demeterapi.service.MapService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import javax.annotation.Resource;

import com.ziroom.tech.demeterapi.service.impl.DeptRankingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    @Resource
    private DeptRankingService deptRankingService;

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
    public void testEhr() throws ParseException {
        RankingReq rankingReq = new RankingReq();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rankingReq.setStartTime(simpleDateFormat.parse("2021-08-01 00:01:00"));
        rankingReq.setEndTime(simpleDateFormat.parse("2021-08-31 00:00:00"));
        System.out.println("测试！");
        List<DeptRankingResp> deptSkill = deptRankingService.getDeptSkill(rankingReq);
        System.out.println(deptSkill);
    }


}
