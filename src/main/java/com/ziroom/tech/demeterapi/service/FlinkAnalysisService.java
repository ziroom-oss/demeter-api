package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;

import java.util.List;

/**
 * @author daijiankun
 */
public interface FlinkAnalysisService {

    /**
     * getCtoResp
     * @param ctoReq
     * @return
     */
    CtoResp getCtoResp(CTOReq ctoReq);

    List<RankResp> getAllIndividualProjectIndiactorInfo(RankingReq rankingReq);

    List<RankResp> getAlldeptProjectIndiactorInfo(RankingReq rankingReq);

}
