package com.ziroom.tech.demeterapi.common;

import com.ziroom.tech.demeterapi.common.api.FlinkAnalysisEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
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

import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import ziroom.com.google.common.util.concurrent.UncheckedTimeoutException;

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
    @Resource
    private EhrComponent ehrComponent;
    }

    /**
     * zhangxt3
     * @param startTime
     * @param endTime
     * @param uids
     * @return
     */
    @Cacheable(value = "getProjectIndiactorInfo", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2] + #root.args[3]")
    public List<RankResp> getIndividualProjectIndiactorInfo(Date startTime, Date endTime, String uid, List<String> uids) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        //排行榜页面参数
        AnalysisReq analysisReq = AnalysisReq.builder()
                .startTime(start)
                .endTime(end)
                .uid(uid)
                .uids(uids)
                .build();

        List<RankResp> rankingResp = new ArrayList();
        //获取开发当量、开发价值、开发质量、开发效率作为数组、、、
        Integer rankingInfoindividevEquiv = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individevEquivsort(analysisReq)).getData();
        List<InfoRanking> rankingInfoEquiv = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEquivsearch(analysisReq)).getData();
        List<RankingInfo> rankingEquiv = rankingInfoEquiv.stream().map((infoRanking) -> {
            System.out.println("測試測試測試");
            String userDetailBysimple = ehrComponent.getUserDetailBysimple(infoRanking.getUid());
            return RankingInfo.builder().name(userDetailBysimple)
                    .num(Integer.parseInt(infoRanking.getCount()))
                    .build();
        }).collect(Collectors.toList());

        RankResp respEquiv = RankResp.builder().myRanking(rankingInfoindividevEquiv).rankingList(rankingEquiv).build();
        rankingResp.add(respEquiv);

        Integer rankingInfoindividevEvalue = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individevEvaluesort(analysisReq)).getData();
        List<InfoRanking> rankingInfoEvalue = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEvaluesearch(analysisReq)).getData();
        List<RankingInfo> rankingEvalue = rankingInfoEquiv.stream().map((infoRanking) -> {
            System.out.println("測試測試測試");
            String userDetailBysimple = ehrComponent.getUserDetailBysimple(infoRanking.getUid());
            System.out.println(userDetailBysimple);
            return RankingInfo.builder().name(userDetailBysimple)
                    .num(Integer.parseInt(infoRanking.getCount()))
                    .build();
        }).collect(Collectors.toList());
        RankResp respEvalue = RankResp.builder().myRanking(rankingInfoindividevEvalue).rankingList(rankingEvalue).build();
        rankingResp.add(respEvalue);


//        Integer rankingInfoindividevQuality = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevQualitysort(analysisReq)).getData();
//        List<InfoRanking> rankingInfoQuality = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevQualitysearch(analysisReq)).getData();
//        RankResp respQuality = RankResp.builder().myRanking(rankingInfoindividevQuality).rankingList(rankingInfoQuality).build();
//        rankingResp.add(respQuality);

//        Integer rankingInfoindividevEfficiency = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEfficiencysort(analysisReq)).getData();
//        List<InfoRanking> rankingInfoEfficiency = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.individualdevEfficiencysearch(analysisReq)).getData();
//        RankResp respEfficiency = RankResp.builder().myRanking(rankingInfoindividevEfficiency).rankingList(rankingInfoEfficiency).build();
//        rankingResp.add(respEfficiency);

        return rankingResp;
    }


    @Cacheable(value = "getProjectIndiactorInfo", key = "#root.methodName + #root.args[0] + #root.args[1] + #root.args[2] + #root.args[3]")
    public List<RankResp> getdeptProjectIndiactorInfo(Date startTime, Date endTime, String uid, List<String> uids) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = formatter.format(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        String end = formatter.format(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
        //排行榜页面参数
        AnalysisReq analysisReq = AnalysisReq.builder()
                .startTime(start)
                .endTime(end)
                .uid(uid)
                .uids(uids)
                .build();

        List<RankResp> rankingResp = new ArrayList();
        //获取开发当量、开发价值、开发质量、开发效率作为数组、、、
        Integer rankingInfoindividevEquiv = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.deptdevEquivsort(analysisReq)).getData();
        List<InfoRanking> rankingInfoEquiv = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.deptdevEquivsearch(analysisReq)).getData();
        List<RankingInfo> rankingEquiv = rankingInfoEquiv.stream().map((infoRanking) -> {
            System.out.println("cececeshishishi");
            System.out.println(infoRanking.getUid());
            return RankingInfo.builder().name(ehrComponent.getUserDetail(infoRanking.getUid()).getUserName())
                    .num(Integer.parseInt(infoRanking.getCount()))
                    .build();
        }).collect(Collectors.toList());
        System.out.println("測試測試測試");
        System.out.println(rankingEquiv);
        RankResp respEquiv = RankResp.builder().myRanking(rankingInfoindividevEquiv).rankingList(rankingEquiv).build();
        rankingResp.add(respEquiv);

        Integer rankingInfoindividevEvalue = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.deptdevEvaluesort(analysisReq)).getData();
        List<InfoRanking> rankingInfoEvalue = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.deptdevEvaluesearch(analysisReq)).getData();
        List<RankingInfo> rankingEvalue = rankingInfoEvalue.stream().map((infoRanking) -> {
            return RankingInfo.builder().name(ehrComponent.getUserDetail(infoRanking.getUid()).getUserName())
                    .num(Integer.parseInt(infoRanking.getCount()))
                    .build();
        }).collect(Collectors.toList());
        RankResp respEvalue = RankResp.builder().myRanking(rankingInfoindividevEvalue).rankingList(rankingEvalue).build();
        rankingResp.add(respEvalue);


//        Integer rankingInfoindividevQuality = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.deptQuality(analysisReq)).getData();
//        List<InfoRanking> rankingInfoQuality = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.devQuality(analysisReq)).getData();
//        RankResp respQuality = RankResp.builder().myRanking(rankingInfoindividevQuality).rankingList(rankingInfoQuality).build();
//        rankingResp.add(respQuality);

//        Integer rankingInfoindividevEfficiency = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.deptEfficiency(analysisReq)).getData();
//        List<InfoRanking> rankingInfoEfficiency = RetrofitCallAdaptor.execute(flinkAnalysisEndPoint.devEfficiency(analysisReq)).getData();
//        RankResp respEfficiency = RankResp.builder().myRanking(rankingInfoindividevEfficiency).rankingList(rankingInfoEfficiency).build();
//        rankingResp.add(respEfficiency);

        return rankingResp;
    }



}
