package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.DeptRankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("rankingMock")
public class MockDeptController {

    @GetMapping("/getDeptBySkillMap")
    public List<DeptRankingResp> getDeptRanking(@RequestBody DeptRankingReq deptRankingReq){
            return null;
    }

}