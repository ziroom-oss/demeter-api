package com.ziroom.tech.demeterapi.controller;

import com.ziroom.gelflog.spring.logger.LogHttpService;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.service.RankingListService;
import groovy.util.logging.Slf4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangxintong
 * 排行榜
 */
@Slf4j
@RestController
@RequestMapping(value = "api/rankinglist")
@LogHttpService
public class RankingListController {

    @Resource
   private RankingListService rankingListService;

    /**
     *zhangxt3
     */
    @PostMapping("authSkillpointnum")
    @ApiOperation(value = "认证技能点数量", httpMethod = "POST")
    public Resp<RankingResp> authSkillpointnum(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.authSkillpointnum(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("authSkillnum")
    @ApiOperation(value = "认证技能数量", httpMethod = "POST")
    public Resp<RankingResp> authSkillnum(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.authSkillnum(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("hotSkillpoint")
    @ApiOperation(value = "热门技能点", httpMethod = "POST")
    public Resp<RankingResp> hotSkillpoint(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.hotSkillpoint(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("hotSkill")
    @ApiOperation(value = "热门技能", httpMethod = "POST")
    public Resp<RankingResp> hotSkill(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.hotSkill(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("devEquiv")
    @ApiOperation(value = "开发当量", httpMethod = "POST")
    public Resp<RankingResp> devEquiv(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.devEquiv(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("devEvalue")
    @ApiOperation(value = "开发价值", httpMethod = "POST")
    public Resp<RankingResp> devEvalue(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.devEvalue(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("devQuality")
    @ApiOperation(value = "开发质量", httpMethod = "POST")
    public Resp<RankingResp> devQuality(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.devQuality(rankingReq));
    }

    /**
     *zhangxt3
     */
    @PostMapping("devEfficiency")
    @ApiOperation(value = "开发效率", httpMethod = "POST")
    public Resp<RankingResp> devEfficiency(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.authSkillpointnum(rankingReq));
    }

}
