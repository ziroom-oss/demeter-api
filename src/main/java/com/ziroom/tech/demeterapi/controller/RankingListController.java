package com.ziroom.tech.demeterapi.controller;

import com.ziroom.gelflog.spring.logger.LogHttpService;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.service.FlinkAnalysisService;
import com.ziroom.tech.demeterapi.service.RankingListService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxintong
 * 排行榜
 */
@RestController
@RequestMapping(value = "api/rankinglist")
@LogHttpService
public class RankingListController {

    @Resource
   private RankingListService rankingListService;
    @Resource
    private FlinkAnalysisService flinkAnalysisService;

    /**
     * @zhangxt3
     * 所有需要排行的放入数组
     */
    @PostMapping("getIndividualSkillmapInfo")
    @ApiOperation(value = "技能图谱相关", httpMethod = "POST")
    public Resp<RankingResp[]> getAllskillmapIndiactorInfo(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.getAllIndividualSkillmap(rankingReq));
    }
 /**
  * @param rankingReq
  * @return
  */
    @PostMapping("getAllIndividualProjectIndiactorInfo")
    @ApiOperation(value = "個人工程指标排行", httpMethod = "POST")
    public Resp<List<RankingResp>> getAllIndividualProjectIndiactorInfo(@RequestBody RankingReq rankingReq){
       return Resp.success(flinkAnalysisService.getAllIndividualProjectIndiactorInfo(rankingReq));
    }

    @PostMapping("getDeptSkillmapInfo")
    @ApiOperation(value = "部门技能图谱相关", httpMethod = "POST")
    public Resp<List<RankingResp>> getAllskillmapDept(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.getAllDeptSkillmap(rankingReq));
    }
    /**
     * @param rankingReq
     * @return
     */
    @PostMapping("getAllDeptrojectIndiactorInfo")
    @ApiOperation(value = "部门工程指标排行", httpMethod = "POST")
    public Resp<List<RankingResp>> getAllDeptProjectIndiactorInfo(@RequestBody RankingReq rankingReq){
        return Resp.success(flinkAnalysisService.getDeptProjectIndiactorInfo(rankingReq));
    }

}
