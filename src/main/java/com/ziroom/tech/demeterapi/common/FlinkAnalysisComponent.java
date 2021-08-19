package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.EhrEndPoint;
import com.ziroom.tech.demeterapi.common.api.FlinkAnalysisEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req.EhrApiSimpleReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CostReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CostResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.EfficientResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.QualityReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.QualityResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.Resp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityResp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.InfoRanking;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    @Resource
    private EhrApiService ehrApiService;


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

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2]")
    public StabilityResp getStabilityResp(Date startTime, Date endTime, String departmentCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        StabilityReq stabilityReq = StabilityReq.builder()
                .startTime(start)
                .endTime(end)
                .departmentCode(departmentCode)
                .build();
        Call<Resp<List<StabilityResp>>> call = flinkAnalysisEndPoint.getStabilityData(stabilityReq);
        Resp<List<StabilityResp>> response = RetrofitCallAdaptor.execute(call);
        List<StabilityResp> data = response.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            return data.get(0);
        }
        return new StabilityResp();
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2]")
    public QualityResp getQualityResp(Date startTime, Date endTime, String departmentCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        QualityReq qualityReq = QualityReq.builder()
                .startTime(start)
                .endTime(end)
                .departmentCode(departmentCode)
                .build();
        Call<Resp<QualityResp>> call = flinkAnalysisEndPoint.getQualityData(qualityReq);
        Resp<QualityResp> response = RetrofitCallAdaptor.execute(call);
        QualityResp data = response.getData();
        if (Objects.nonNull(data)) {
            return data;
        } else {
            return new QualityResp();
        }
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2]")
    public CostResp getCostResp(Date startTime, Date endTime, String departmentCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        CostReq costReq = CostReq.builder()
//                .startTime(start)
//                .endTime(end)
                .deptId(departmentCode)
                .build();
        Call<Resp<CostResp>> call = flinkAnalysisEndPoint.getCostData(costReq);
        Resp<CostResp> response = RetrofitCallAdaptor.execute(call);
        CostResp data = response.getData();
        if (Objects.nonNull(data)) {
            return data;
        } else {
            return new CostResp();
        }
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2]")
    public EfficientResp getEfficientResponse(Date startTime, Date endTime, List<String> adCodes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        AnalysisReq analysisReq = AnalysisReq.builder()
                .startTime(start)
                .endTime(end)
                .uids(adCodes)
                .build();

        Call<Resp<EfficientResp>> call = flinkAnalysisEndPoint.getEfficientData(analysisReq);
        Resp<EfficientResp> response = RetrofitCallAdaptor.execute(call);
        EfficientResp data = response.getData();
        if (Objects.nonNull(data)) {
            return data;
        }
        return new EfficientResp();
    }


    /**
     * zhangxt3
     * @param startTime
     * @param endTime
     * @param uids
     * @return
     */
    @Cacheable(value = "getIndividual", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2] + #root.args[3]")
    public List<RankingResp> getIndividualProjectIndiactorInfo(Date startTime, Date endTime, String uid, List<String> uids) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        //排行榜页面参数
        AnalysisReq analysisReq = AnalysisReq.builder().startTime(start).endTime(end).uid(uid).uids(uids).build();

        List<RankingResp> rankingResp = new ArrayList();
        //获取开发当量、开发价值、开发质量、开发效率作为数组、、、
        Integer rankingInfoindividevEquiv = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individevEquivsort(analysisReq)).getData();
        List<InfoRanking> rankingInfoEquiv = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEquivsearch(analysisReq)).getData();
        List<RankingInfo> rankingEquiv = rankingInfoEquiv.stream().limit(10).map((infoRanking) -> {
            System.out.println("測試測試測試");
            EhrApiSimpleReq ehrApiSimpleReq = new EhrApiSimpleReq();
                        ehrApiSimpleReq.setAdCode(infoRanking.getUid());
            return RankingInfo.builder().name(ehrApiService.getEmpSimple(ehrApiSimpleReq).getEmpName())
                    .num(infoRanking.getCount())
                    .build();
        }).collect(Collectors.toList());

        RankingResp respEquiv = RankingResp.builder().myRanking(rankingInfoindividevEquiv).rankingList(rankingEquiv).build();
        rankingResp.add(respEquiv);
        //价值
        Integer rankingInfoindividevEvalue = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individevEvaluesort(analysisReq)).getData();
        List<InfoRanking> rankingInfoEvalue = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEvaluesearch(analysisReq)).getData();
        List<RankingInfo> rankingEvalue = rankingInfoEvalue.stream().limit(10).map((infoRanking) -> {
            //System.out.println("測試測試測試");
            EhrApiSimpleReq ehrApiSimpleReq = new EhrApiSimpleReq();
                        ehrApiSimpleReq.setAdCode(infoRanking.getUid());
            return RankingInfo.builder().name(ehrApiService.getEmpSimple(ehrApiSimpleReq).getEmpName())
                    .num(infoRanking.getCount())
                    .build();
        }).collect(Collectors.toList());
        RankingResp respEvalue = RankingResp.builder().myRanking(rankingInfoindividevEvalue).rankingList(rankingEvalue).build();
        rankingResp.add(respEvalue);
//        Integer rankingInfoindividevQuality = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevQualitysort(analysisReq)).getData();
//        List<InfoRanking> rankingInfoQuality = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevQualitysearch(analysisReq)).getData();
//        RankingResp respQuality = RankingResp.builder().myRanking(rankingInfoindividevQuality).rankingList(rankingInfoQuality).build();
//        rankingResp.add(respQuality);

//        Integer rankingInfoindividevEfficiency = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEfficiencysort(analysisReq)).getData();
//        List<InfoRanking> rankingInfoEfficiency = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEfficiencysearch(analysisReq)).getData();
//        RankingResp respEfficiency = RankingResp.builder().myRanking(rankingInfoindividevEfficiency).rankingList(rankingInfoEfficiency).build();
//        rankingResp.add(respEfficiency);
        return rankingResp;
    }

    /**
     * zhangxt3  部门相关
     * @param startTime
     * @param endTime
     * @param uids
     * @return
     */
    @Cacheable(value = "getDept", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2] + #root.args[3]")
    public List<RankingResp> getDeptProjectIndiactorInfo(Date startTime, Date endTime, @Ignore String uid, @Ignore List<String> uids) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = format.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = format.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));

        //排行榜页面参数
        AnalysisReq analysisReq = AnalysisReq.builder().startTime(start).endTime(end).uid(uid).uids(uids).build();

        List<RankingResp> rankingResp = new ArrayList();

        Call<Resp<List<InfoRanking>>> equivCall = flinkAnalysisEndPoint.deptdevEquivSearch(analysisReq);
        Call<Resp<List<InfoRanking>>> valueCall = flinkAnalysisEndPoint.deptdevEvalueSearch(analysisReq);
//        Call<Resp<List<DeptRankingResp>>> equivCall = flinkAnalysisEndPoint.deptdevEquivSearch(analysisReq);
//        Call<Resp<List<DeptRankingResp>>> valueCall = flinkAnalysisEndPoint.deptdevEvalueSearch(analysisReq);

        List<InfoRanking> equivData = RetrofitCallAdaptor.execute(equivCall).getData();
        List<RankingInfo> collectEquiv = equivData.stream().map(data -> {
            return RankingInfo.builder().name(data.getDepartmentName()).num(data.getCount()).build();
        }).limit(10).collect(Collectors.toList());
        RankingResp equiv = new RankingResp(collectEquiv);
        rankingResp.add(equiv);
        List<InfoRanking> valueData = RetrofitCallAdaptor.execute(valueCall).getData();
        List<RankingInfo> collectValue = valueData.stream().map(data -> {
            return RankingInfo.builder().name(data.getDepartmentName()).num(data.getCount()).build();
        }).limit(10).collect(Collectors.toList());
        RankingResp value = new RankingResp(collectValue);
        rankingResp.add(value);
//        List<DeptRankingResp> equivData = RetrofitCallAdaptor.execute(equivCall).getData();
//        List<DeptRankingResp> valueData = RetrofitCallAdaptor.execute(valueCall).getData();

        return rankingResp;
    }











}
