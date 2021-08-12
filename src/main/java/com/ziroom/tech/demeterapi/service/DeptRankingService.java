package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import java.util.List;

public interface DeptRankingService {
    //获取
    public List<DeptRankingResp> getDeptSkillPoint(RankingReq rankingReq);
    //获取
    public List<DeptRankingResp> getDeptSkill(RankingReq rankingReq);

}
