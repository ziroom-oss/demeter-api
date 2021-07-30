package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class AnalysisReq {

    private String startTime;

    private String endTime;

    private String uid;

    private List<String> uids;
}
