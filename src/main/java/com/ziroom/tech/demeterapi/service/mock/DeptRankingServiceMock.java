package com.ziroom.tech.demeterapi.service.mock;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.service.RankingListService;

import java.util.List;

public class DeptRankingServiceMock implements RankingListService {

    @Override
    public RankingResp[] getAllskillmapIndiactorInfo(RankingReq rankingReq) {
        return new RankingResp[0];
    }

    @Override
    public List<DeptRankingResp> getDeptSkillPoint(RankingReq rankingReq) {
        return null;
    }

    @Override
    public List<DeptRankingResp> getDeptSkill(RankingReq rankingReq) {
        return null;
    }

}
