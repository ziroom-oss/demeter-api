package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PersonReq;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.PersonResp;
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
    CtoResp getCtoResp(CTOReq ctoReq) ;

    PersonResp getPersonData(PersonReq personReq);

    List<RankingResp> getAllIndividualProjectIndiactorInfo(RankingReq rankingReq);

    List<RankingResp> getDeptProjectIndiactorInfo(RankingReq rankingReq);

}
