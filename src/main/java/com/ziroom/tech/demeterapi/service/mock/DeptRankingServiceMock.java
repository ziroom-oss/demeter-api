package com.ziroom.tech.demeterapi.service.mock;

import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingInfo;
import com.ziroom.tech.demeterapi.service.DeptRankingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeptRankingServiceMock implements DeptRankingService {

    @Override
    public List<DeptRankingInfo> getDeptEquiv(Date from, Date to) {
        List<DeptRankingInfo> deptRankingInfos = new ArrayList<>();
        return deptRankingInfos;
    }
}
