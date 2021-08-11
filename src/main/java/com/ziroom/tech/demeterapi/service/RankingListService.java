package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;


public interface RankingListService {

    //search individual ranking about
    RankingResp[] getAllskillmapIndiactorInfo(RankingReq rankingReq);
}
