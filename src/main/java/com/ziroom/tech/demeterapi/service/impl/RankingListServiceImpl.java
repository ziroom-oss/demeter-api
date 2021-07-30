package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.service.MapService;
import com.ziroom.tech.demeterapi.service.RankingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class RankingListServiceImpl implements RankingListService {

    @Resource
    private MapService mapService;

    @Override
    public RankingResp authSkillpointnum(RankingReq rankingReq) {

        return null;

    }

    @Override
    public RankingResp authSkillnum(RankingReq rankingReq) {
        return null;
    }

    @Override
    public RankingResp hotSkillpoint(RankingReq rankingReq) {
        return null;
    }

    @Override
    public RankingResp hotSkill(RankingReq rankingReq) {
        return null;
    }

    @Override
    public RankingResp devEquiv(RankingReq rankingReq) {
        return null;
    }

    @Override
    public RankingResp devEvalue(RankingReq rankingReq) {
        return null;
    }

    @Override
    public RankingResp devQuality(RankingReq rankingReq) {
        return null;
    }

    @Override
    public RankingResp devEfficiency(RankingReq rankingReq) {
        return null;
    }
}
