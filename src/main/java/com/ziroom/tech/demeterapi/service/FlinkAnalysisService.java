package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PersonReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.PersonResp;
import java.util.concurrent.ExecutionException;

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
}
