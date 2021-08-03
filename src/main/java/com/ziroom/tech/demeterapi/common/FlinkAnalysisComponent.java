package com.ziroom.tech.demeterapi.common;

import com.ziroom.tech.demeterapi.common.api.FlinkAnalysisEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.Resp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityResp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import retrofit2.Call;

/**
 * @author daijiankun
 */
@Component
public class FlinkAnalysisComponent {

    @Resource
    private FlinkAnalysisEndPoint flinkAnalysisEndPoint;

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2]")
    public List<AnalysisResp> getAnalysisResp(Date startTime, Date endTime, List<String> adCodes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        AnalysisReq analysisReq = AnalysisReq.builder()
                .startTime(start)
                .endTime(end)
                .uids(adCodes)
                .build();

        Call<Resp<List<AnalysisResp>>> call = flinkAnalysisEndPoint.getAnalysisResp(analysisReq);
        Resp<List<AnalysisResp>> response = RetrofitCallAdaptor.execute(call);
        return response.getData();
    }

    public List<StabilityResp> getStabilityResp(Date startTime, Date endTime, String departmentCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        StabilityReq stabilityReq = StabilityReq.builder()
                .startTime(start)
                .endTime(end)
                .departmentCode(departmentCode)
                .build();
        Call<Resp<List<StabilityResp>>> call = flinkAnalysisEndPoint.getStabilityData(stabilityReq);
        Resp<List<StabilityResp>> response = RetrofitCallAdaptor.execute(call);
        return response.getData();
    }
}
