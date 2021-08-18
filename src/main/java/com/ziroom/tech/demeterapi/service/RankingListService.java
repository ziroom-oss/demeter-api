package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;

import java.util.List;


public interface RankingListService {

    //search individual ranking about
    RankingResp[] getAllIndividualSkillmap(RankingReq rankingReq);

    //search dept ranking about
    List<RankingResp> getAllDeptSkillmap(RankingReq rankingReq);

}
