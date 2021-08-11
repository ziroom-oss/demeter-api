package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingInfo;

import java.util.Date;
import java.util.List;

public interface DeptRankingService {
    public List<DeptRankingInfo> getDeptEquiv(Date from, Date to);
}
