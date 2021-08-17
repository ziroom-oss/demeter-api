package com.ziroom.tech.demeterapi.service.mock;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.service.DeptRankingService;
import org.springframework.stereotype.Service;
import java.util.List;

public class DeptRankingServiceMock implements DeptRankingService {

    @Override
    public List<DeptRankingResp> getDeptSkillPoint(RankingReq rankingReq) {
        return null;
    }

    @Override
    public List<DeptRankingResp> getDeptSkill(RankingReq rankingReq) {
        return null;
    }

}
