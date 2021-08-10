package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.service.FlinkAnalysisService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import com.ziroom.tech.demeterapi.service.RankingListService;
import groovy.util.logging.Slf4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangxintong
 * 排行榜
 */
@Slf4j
@RestController
@RequestMapping(value = "api/rankinglist")
public class RankingListController {

    @Resource
   private RankingListService rankingListService;
    @Resource
    private FlinkAnalysisService flinkAnalysisService;

    /**
     * @zhangxt3
     * 所有需要排行的放入数组
     */
    @PostMapping("getAllskillmapIndiactorInfo")
    @ApiOperation(value = "技能图谱相关", httpMethod = "POST")
    public Resp<RankingResp[]> getAllskillmapIndiactorInfo(@RequestBody RankingReq rankingReq){
        return Resp.success(rankingListService.getAllskillmapIndiactorInfo(rankingReq));
    }
 /**
  *
  * @param rankingReq
  * @return
  */
    @PostMapping("getAllIndividualProjectIndiactorInfo")
    @ApiOperation(value = "個人工程指标排行", httpMethod = "POST")
    public Resp<List<RankResp>> getAllIndividualProjectIndiactorInfo(@RequestBody RankingReq rankingReq){
       return Resp.success(flinkAnalysisService.getAllIndividualProjectIndiactorInfo(rankingReq));
    }

@PostMapping("getAlldeptProjectIndiactorInfo")
@ApiOperation(value = "部門工程指标排行", httpMethod = "POST")
public Resp<List<RankResp>> getAlldeptProjectIndiactorInfo(@RequestBody RankingReq rankingReq){
    return Resp.success(flinkAnalysisService.getAlldeptProjectIndiactorInfo(rankingReq));
}

}
