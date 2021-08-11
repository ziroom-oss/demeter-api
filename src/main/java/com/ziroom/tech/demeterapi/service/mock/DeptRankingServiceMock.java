package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.http.POST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeptRankingServiceMock implements  DeptRankingService{

    @Override
    public List<DeptRankingInfo> getDeptEquiv(Date from, Date to) {
        List<DeptRankingInfo> deptRankingInfos = new ArrayList<>();
        return deptRankingInfos;
    }
}
