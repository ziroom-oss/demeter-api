package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;

public interface RankingListService {

    //search individual ranking about
    RankingResp authSkillpointnum(RankingReq rankingReq);
    RankingResp authSkillnum(RankingReq rankingReq);
    RankingResp hotSkillpoint(RankingReq rankingReq);
    RankingResp hotSkill(RankingReq rankingReq);
    RankingResp devEquiv(RankingReq rankingReq);
    RankingResp devEvalue(RankingReq rankingReq);
    RankingResp devQuality(RankingReq rankingReq);
    RankingResp devEfficiency(RankingReq rankingReq);
}
