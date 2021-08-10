package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.FlinkAnalysisComponent;
import com.ziroom.tech.demeterapi.po.dto.req.email.UserEmailDto;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PersonReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CostResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.EfficientResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.PersonOverview;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.PersonResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.QualityResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.PersonalByDay;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.DeptProportion;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.DeptTendency;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.DeptTendencyItem;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.EmployeeProportion;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.EmployeeTendency;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.EmployeeTendencyItem;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.LevelProportion;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.LevelTendency;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.LevelTendencyItem;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.Metric;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.NameValue;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.TeamOverviewResp;
import com.ziroom.tech.demeterapi.service.FlinkAnalysisService;
import com.ziroom.tech.demeterapi.service.UserEmailService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class FlinkAnalysisServiceImpl implements FlinkAnalysisService {

    @Resource
    private FlinkAnalysisComponent flinkAnalysisComponent;
    @Resource
    private EhrComponent ehrComponent;
    @Resource
    private UserEmailService userEmailService;

    private String convertEmail2Adcode(String email) {
        return email.split("@")[0];
    }

    private Set<EhrUserDetailResp> getUserSetFromDept(String deptId) {
        Set<EhrUserResp> users = ehrComponent.getUsers(deptId, 101);
        List<String> userCodes = users.stream().map(EhrUserResp::getUserCode).collect(Collectors.toList());

        Set<EhrUserDetailResp> userRespSet = new HashSet<>(512);
        List<List<String>> partition = Lists.partition(userCodes, 10);
        partition.forEach(codeGroup -> {
            String codeString = String.join(",", codeGroup);
            List<EhrUserDetailResp> ehrUserDetail = ehrComponent.getEhrUserDetail(codeString);
            if (CollectionUtils.isNotEmpty(ehrUserDetail)) {
                userRespSet.addAll(ehrUserDetail);
            }
        });
        return userRespSet;
    }

    private Set<EhrUserDetailResp> getUserSetFromAdList(List<String> adCodeList) {
        List<List<String>> partition = Lists.partition(adCodeList, 10);
        List<CompletableFuture<List<EhrUserDetailResp>>> list = new ArrayList<>();
        Set<EhrUserDetailResp> userRespSet = new HashSet<>(16);
        partition.forEach(codeGroup -> {
            String codeString = String.join(",", codeGroup);
            List<EhrUserDetailResp> ehrUserDetail = ehrComponent.getEhrUserDetail(codeString);
            if (CollectionUtils.isNotEmpty(ehrUserDetail)) {
                userRespSet.addAll(ehrUserDetail);
            }
        });
        return userRespSet;
    }

    @Override
    public CtoResp getCtoResp(CTOReq ctoReq) {

        Set<EhrUserDetailResp> userRespSet = getUserSetFromDept(ctoReq.getDeptId());

        Map<String, EhrUserDetailResp> userMap =
                userRespSet.stream().collect(Collectors.toMap(EhrUserDetailResp::getEmail, Function.identity(), (value1, value2) -> value2));
        List<String> emailList = userRespSet.stream().map(EhrUserDetailResp::getEmail).filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        List<UserEmailDto> userEmailDtoList = userEmailService.batchSelectEmail(emailList);
        List<String> subPrefixList = userEmailDtoList.stream().map(UserEmailDto::getSubEmail).map(this::convertEmail2Adcode)
                .collect(Collectors.toList());
        List<String> adCodeList = userRespSet.stream().map(EhrUserDetailResp::getEmail).filter(StringUtils::isNotEmpty).map(this::convertEmail2Adcode)
                .collect(Collectors.toList());
//        adCodeList.addAll(subPrefixList);


        Calendar qoqStart = getQoQStart(ctoReq.getStartDate());
        Calendar qoqEnd = getQoQEnd(ctoReq.getStartDate());
        Calendar yoyStart = getYoYStart(ctoReq.getStartDate());
        Calendar yoyEnd = getYoyEnd(ctoReq.getStartDate());

        CompletableFuture<List<AnalysisResp>> currentFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(ctoReq.getStartDate(), ctoReq.getEndDate(), adCodeList))
                .exceptionally(e -> {
                    log.error("", e);
                    return new ArrayList<>();
        });
        CompletableFuture<List<AnalysisResp>> qoqFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(qoqStart.getTime(), qoqEnd.getTime(), adCodeList))
                .exceptionally(e -> {
                    log.error("", e);
                    return new ArrayList<>();
                });
        CompletableFuture<List<AnalysisResp>> yoyFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(yoyStart. getTime(), yoyEnd.getTime(), adCodeList))
                .exceptionally(e -> {
                    log.error("", e);
                    return new ArrayList<>();
                });
        List<List<AnalysisResp>> collect = Stream.of(currentFuture, qoqFuture, yoyFuture).map(CompletableFuture::join)
                .collect(Collectors.toList());

        CompletableFuture<StabilityResp> stabilityFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getStabilityResp(ctoReq.getStartDate(), ctoReq.getEndDate(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new StabilityResp();
                });
        CompletableFuture<StabilityResp> qoqStabilityFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getStabilityResp(qoqStart.getTime(), qoqEnd.getTime(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new StabilityResp();
                });
        CompletableFuture<StabilityResp> yoyStabilityFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getStabilityResp(yoyStart.getTime(), yoyStart.getTime(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new StabilityResp();
                });
        List<StabilityResp> collect1 =
                Stream.of(stabilityFuture, qoqStabilityFuture, yoyStabilityFuture).map(CompletableFuture::join)
                        .collect(Collectors.toList());

        CompletableFuture<QualityResp> qualityFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getQualityResp(ctoReq.getStartDate(), ctoReq.getEndDate(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new QualityResp();
                });
        CompletableFuture<QualityResp> qoqQualityFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getQualityResp(qoqStart.getTime(), qoqEnd.getTime(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new QualityResp();
                });
        CompletableFuture<QualityResp> yoyQualityFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getQualityResp(yoyStart.getTime(), yoyStart.getTime(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new QualityResp();
                });
        List<QualityResp> collect2 =
                Stream.of(qualityFuture, qoqQualityFuture, yoyQualityFuture).map(CompletableFuture::join)
                        .collect(Collectors.toList());

        CompletableFuture<CostResp> costFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getCostResp(ctoReq.getStartDate(), ctoReq.getEndDate(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new CostResp();
                });
        CompletableFuture<CostResp> qoqCostFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getCostResp(qoqStart.getTime(), qoqEnd.getTime(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new CostResp();
                });
        CompletableFuture<CostResp> yoyCostFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getCostResp(yoyStart.getTime(), yoyStart.getTime(), ctoReq.getDeptId()))
                .exceptionally(e -> {
                    log.error("", e);
                    return new CostResp();
                });
        List<CostResp> collect3 =
                Stream.of(costFuture, qoqCostFuture, yoyCostFuture).map(CompletableFuture::join)
                        .collect(Collectors.toList());

        CompletableFuture<EfficientResp> efficientFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getEfficientResponse(ctoReq.getStartDate(), ctoReq.getEndDate(), adCodeList))
                .exceptionally(e -> {
                    log.error("", e);
                    return new EfficientResp();
                });
        CompletableFuture<EfficientResp> qoqEfficientFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getEfficientResponse(qoqStart.getTime(), qoqEnd.getTime(), adCodeList))
                .exceptionally(e -> {
                    log.error("", e);
                    return new EfficientResp();
                });
        CompletableFuture<EfficientResp> yoyEfficientFuture
                = CompletableFuture
                .supplyAsync(() -> flinkAnalysisComponent.getEfficientResponse(yoyStart.getTime(), yoyEnd.getTime(), adCodeList))
                .exceptionally(e -> {
                    log.error("", e);
                    return new EfficientResp();
                });
        List<EfficientResp> collect4 =
                Stream.of(efficientFuture, qoqEfficientFuture, yoyEfficientFuture).map(CompletableFuture::join)
                        .collect(Collectors.toList());

        List<AnalysisResp> analysisData = collect.get(0);
        List<AnalysisResp> qoqAnalysisData = collect.get(1);
        List<AnalysisResp> yoyAnalysisData = collect.get(2);

        StabilityResp stabilityResp = collect1.get(0);
        StabilityResp qoqStabilityResp = collect1.get(1);
        StabilityResp yoyStabilityResp = collect1.get(2);

        QualityResp qualityResp = collect2.get(0);
        QualityResp qoqQualityResp = collect2.get(1);
        QualityResp yoyQualityResp = collect2.get(2);

        CostResp costResp = collect3.get(0);
        CostResp qoqCostResp = collect3.get(1);
        CostResp yoyCostResp = collect3.get(2);

        EfficientResp efficientResp = collect4.get(0);
        EfficientResp qoqEfficientResp = collect4.get(1);
        EfficientResp yoyEfficientResp = collect4.get(2);

        analysisData.forEach(o -> {
            EhrUserDetailResp ehrUserDetailResp = userMap.get(o.getUid() + "@ziroom.com");
            o.setLevel(ehrUserDetailResp.getLevelName());
        });

        long bugNums = analysisData.stream().mapToLong(AnalysisResp::getFixBugCount).sum();
        long qoqBugNums = qoqAnalysisData.stream().mapToLong(AnalysisResp::getFixBugCount).sum();
        long yoyBugNums = yoyAnalysisData.stream().mapToLong(AnalysisResp::getFixBugCount).sum();
        long equivalents = analysisData.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long qoqEquivalents = qoqAnalysisData.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long yoyEquivalents = yoyAnalysisData.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long insertions = analysisData.stream().mapToLong(AnalysisResp::getInsertions).sum();
        long qoqInsertions = qoqAnalysisData.stream().mapToLong(AnalysisResp::getInsertions).sum();
        long yoyInsertions = yoyAnalysisData.stream().mapToLong(AnalysisResp::getInsertions).sum();
        long deletions = analysisData.stream().mapToLong(AnalysisResp::getDeletions).sum();
        long qoqDeletions = qoqAnalysisData.stream().mapToLong(AnalysisResp::getDeletions).sum();
        long yoyDeletions = yoyAnalysisData.stream().mapToLong(AnalysisResp::getDeletions).sum();

        /**
         * 核心数据指标
         */
        List<TeamOverviewResp> overviewRespList = new ArrayList<>(16);
        List<Metric> qoqProductionMetric = new ArrayList<>(16);
        List<Metric> yoyProductionMetric = new ArrayList<>(16);

        getCalculateData("开发当量", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getDevEquivalent, qoqProductionMetric, yoyProductionMetric);
        double capiteEquivalents = equivalents * 1.0 / costResp.getDepartmentTotal();
        double capiteQoQEquivalents = qoqEquivalents * 1.0 / costResp.getDepartmentTotal();
        double capiteYoYEquivalents = yoyEquivalents * 1.0 / costResp.getDepartmentTotal();
        qoqProductionMetric.add(Metric.builder()
                .name("人均当量")
                .value(String.format("%.0f", capiteEquivalents))
                .oldValue(String.format("%.0f", capiteQoQEquivalents))
                .rate(String.format("%.2f", Math.abs(capiteEquivalents - capiteQoQEquivalents) * 100.0 / capiteQoQEquivalents))
                .tendency(isEqual(capiteEquivalents, capiteQoQEquivalents) ? 0 : capiteEquivalents > capiteQoQEquivalents ? 1 : 2).build());
        yoyProductionMetric.add(Metric.builder()
                .name("人均当量")
                .value(String.format("%.0f", capiteEquivalents))
                .oldValue(String.format("%.0f", capiteYoYEquivalents))
                .rate(String.format("%.2f", Math.abs(capiteEquivalents - capiteYoYEquivalents) * 100.0 / capiteYoYEquivalents))
                .tendency(isEqual(capiteEquivalents, capiteYoYEquivalents) ? 0 : capiteEquivalents > capiteYoYEquivalents ? 1 : 2).build());
        qoqProductionMetric.add(Metric.builder()
                .name("<span style=\"font-weight: bold\">代码增加行数</span>/删除行数")
                .value("<span style=\"font-weight: bold\">" + insertions + "</span>/" + deletions)
                .oldValue("<span style=\"font-weight: bold\">" + qoqInsertions + "</span>/" + qoqDeletions)
                .rate(String.format("%.2f", Math.abs(insertions - qoqInsertions) * 100.0 / qoqInsertions))
                .tendency(insertions == qoqInsertions ? 0 : insertions > qoqInsertions ? 1 : 2)
                .build());
        yoyProductionMetric.add(Metric.builder()
                .name("<span style=\"font-weight: bold\">代码增加行数</span>/删除行数")
                .value("<span style=\"font-weight: bold\">" + insertions + "</span>/" + deletions)
                .oldValue("<span style=\"font-weight: bold\">" + yoyInsertions + "</span>/" + yoyDeletions)
                .rate(String.format("%.2f", Math.abs(insertions - yoyInsertions) * 100.0 / yoyInsertions))
                .tendency(insertions == yoyInsertions ? 0 : insertions > qoqInsertions ? 1 : 2)
                .build());
        getCalculateData("代码提交次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqProductionMetric, yoyProductionMetric);
        qoqProductionMetric.add(Metric.builder()
                .name("项目数/功能数/修复bug数")
                .value(Optional.ofNullable(efficientResp.getCompleteProjectCount()).orElse(0) + "/"
                        + Optional.ofNullable(efficientResp.getProcessDemandCount()).orElse(0) + "/"
                        + Optional.ofNullable(efficientResp.getFixBugCount()).orElse(0))
                .oldValue(Optional.ofNullable(qoqEfficientResp.getCompleteProjectCount()).orElse(0) + "/"
                        + Optional.ofNullable(qoqEfficientResp.getProcessDemandCount()).orElse(0) + "/"
                        + Optional.ofNullable(qoqEfficientResp.getFixBugCount()).orElse(0)).rate("-")
                .tendency(0).build());
        yoyProductionMetric.add(Metric.builder()
                .name("项目数/功能数/修复bug数")
                .value(Optional.ofNullable(efficientResp.getCompleteProjectCount()).orElse(0) + "/"
                        + Optional.ofNullable(efficientResp.getProcessDemandCount()).orElse(0) + "/"
                        + Optional.ofNullable(efficientResp.getFixBugCount()).orElse(0))
                .oldValue(Optional.ofNullable(yoyEfficientResp.getCompleteProjectCount()).orElse(0) + "/"
                        + Optional.ofNullable(yoyEfficientResp.getProcessDemandCount()).orElse(0) + "/"
                        + Optional.ofNullable(yoyEfficientResp.getFixBugCount()).orElse(0)).rate("-")                .tendency(0).build());
        qoqProductionMetric.add(Metric.builder().name("开发价值/价值密度").value("-").oldValue("-").rate("-").tendency(0).build());
        yoyProductionMetric.add(Metric.builder().name("开发价值/价值密度").value("-").oldValue("-").rate("-").tendency(0).build());
        TeamOverviewResp production = TeamOverviewResp.builder()
                .id(1)
                .name("产出类")
                .qoqMetricList(qoqProductionMetric)
                .yoyMetricList(yoyProductionMetric)
                .style("border-right: dashed #A9A9A9; border-bottom: dashed #A9A9A9; border-width: 1px; margin-top: 15px")
                .build();

        List<Metric> qoqEfficiencyMetric = new ArrayList<>();
        List<Metric> yoyEfficiencyMetric = new ArrayList<>();
        double projectV1 = Double.parseDouble(Optional.ofNullable(efficientResp.getProjectAverageTime()).orElse("0"));
        double projectV2 = Double.parseDouble(Optional.ofNullable(qoqEfficientResp.getProjectAverageTime()).orElse("0"));
        double projectV3 = Double.parseDouble(Optional.ofNullable(yoyEfficientResp.getProjectAverageTime()).orElse("0"));
        double functionV1 = Double.parseDouble(Optional.ofNullable(efficientResp.getFunctionAverageTime()).orElse("0"));
        double functionV2 = Double.parseDouble(Optional.ofNullable(qoqEfficientResp.getFunctionAverageTime()).orElse("0"));
        double functionV3 = Double.parseDouble(Optional.ofNullable(yoyEfficientResp.getFunctionAverageTime()).orElse("0"));
        double bugV1 = Double.parseDouble(Optional.ofNullable(efficientResp.getBugAverageFixTime()).orElse("0"));
        double bugV2 = Double.parseDouble(Optional.ofNullable(qoqEfficientResp.getBugAverageFixTime()).orElse("0"));
        double bugV3 = Double.parseDouble(Optional.ofNullable(yoyEfficientResp.getBugAverageFixTime()).orElse("0"));
        qoqEfficiencyMetric.add(Metric.builder()
                .name("项目平均开发周期")
                .value(projectV1 + "h")
                .oldValue(projectV2 + "h")
                .rate(String.format("%.2f", Math.abs(projectV1 - projectV2) * 100 / projectV2))
                .tendency(isEqual(projectV1, projectV2) ? 0 : projectV1 > projectV2 ? 1 : 2)
                .build());
        qoqEfficiencyMetric.add(Metric.builder()
                .name("功能平均开发周期")
                .value(functionV1 + "h")
                .oldValue(functionV2 + "h")
                .rate(String.format("%.2f", Math.abs(functionV1 - functionV2) * 100 / functionV2))
                .tendency(isEqual(functionV1, functionV2) ? 0 : functionV1 > functionV2 ? 1 : 2)
                .build());
        qoqEfficiencyMetric.add(Metric.builder()
                .name("bug平均修复时间")
                .value(bugV1 + "h")
                .oldValue(bugV2 + "h")
                .rate(String.format("%.2f", Math.abs(bugV1 - bugV2) * 100 / bugV2))
                .tendency(isEqual(bugV1, bugV2) ? 0 : bugV1 > bugV2 ? 1 : 2)
                .build());
        yoyEfficiencyMetric.add(Metric.builder()
                .name("项目平均开发周期")
                .value(projectV1 + "h")
                .oldValue(projectV3 + "h")
                .rate(String.format("%.2f", Math.abs(projectV1 - projectV3) * 100 / projectV3))
                .tendency(isEqual(projectV1, projectV3) ? 0 : projectV1 > projectV3 ? 1 : 2)
                .build());
        yoyEfficiencyMetric.add(Metric.builder()
                .name("功能平均开发周期")
                .value(functionV1 + "h")
                .oldValue(functionV3 + "h")
                .rate(String.format("%.2f", Math.abs(functionV1 - functionV3) * 100 / functionV3))
                .tendency(isEqual(functionV1, functionV3) ? 0 : functionV1 > functionV3 ? 1 : 2)
                .build());
        yoyEfficiencyMetric.add(Metric.builder()
                .name("bug平均修复时间")
                .value(bugV1 + "h")
                .oldValue(bugV3 + "h")
                .rate(String.format("%.2f", Math.abs(bugV1 - bugV3) * 100 / bugV3))
                .tendency(isEqual(bugV1, bugV3) ? 0 : bugV1 > bugV3 ? 1 : 2)
                .build());
        TeamOverviewResp efficiency = TeamOverviewResp.builder()
                .id(2)
                .name("效率类")
                .style("border-bottom: dashed #A9A9A9; border-width: 1px; margin-top: 15px")
                .qoqMetricList(qoqEfficiencyMetric)
                .yoyMetricList(yoyEfficiencyMetric)
                .build();

        List<Metric> qoqPublicationMetric = new ArrayList<>();
        List<Metric> yoyPublicationMetric = new ArrayList<>();
        getCalculateData("发布次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getPublishNum, qoqPublicationMetric, yoyPublicationMetric);
        getCalculateData("编译次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCompileNum, qoqPublicationMetric, yoyPublicationMetric);
        getCalculateData("上线次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getOnlineNum, qoqPublicationMetric, yoyPublicationMetric);
        getCalculateData("重启次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getRestartNum, qoqPublicationMetric, yoyPublicationMetric);
        getCalculateData("回滚次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getRollbackNum, qoqPublicationMetric, yoyPublicationMetric);
        TeamOverviewResp publication = TeamOverviewResp.builder()
                .id(3)
                .name("发布类")
                .qoqMetricList(qoqPublicationMetric)
                .yoyMetricList(yoyPublicationMetric)
                .style("border-right: dashed #A9A9A9; border-bottom: dashed #A9A9A9; border-width: 1px;")
                .build();

        List<Metric> qoqQualityMetric = new ArrayList<>();
        List<Metric> yoyQualityMetric = new ArrayList<>();

        // 千行bug率 = bug数 / 开发当量
        double value = bugNums * 100.0 / equivalents;
        double qoqValue = qoqBugNums * 100.0 / qoqEquivalents;
        double yoyValue = yoyBugNums * 100.0 / yoyEquivalents;
        qoqQualityMetric.add(Metric.builder()
                .name("千行bug率")
                .value(String.format("%.2f", value))
                .oldValue(String.format("%.2f", qoqValue))
                .rate(String.format("%.2f", Math.abs(value - qoqValue) * 100 / qoqValue))
                .tendency(isEqual(value, qoqValue) ? 0 : value > qoqValue ? 1 : 2)
                .build());
        yoyQualityMetric.add(Metric.builder()
                .name("千行bug率")
                .value(String.format("%.2f", value))
                .oldValue(String.format("%.2f", yoyValue))
                .rate(String.format("%.2f", Math.abs(value - yoyValue) * 100 / yoyValue))
                .tendency(isEqual(value, yoyValue) ? 0 : value > yoyValue ? 1 : 2)
                .build());
        TeamOverviewResp quality = TeamOverviewResp.builder()
                .id(4)
                .name("质量类")
                .qoqMetricList(qoqQualityMetric)
                .yoyMetricList(yoyQualityMetric)
                .style("border-bottom: dashed #A9A9A9; border-width: 1px;")
                .build();
//        getCalculateData("注释覆盖度", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
//        getCalculateData("测试覆盖度", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
//        getCalculateData("代码模块性", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
        qoqQualityMetric.add(Metric.builder()
                .name("注释覆盖度")
                .value(String.format("%.2f", qualityResp.getDocCoverage() * 100) + "%")
//                .oldValue(String.format("%.2f", qoqQualityResp.getDocCoverage()))
//                .rate(String.format("%.2f", (qualityResp.getDocCoverage() - qoqQualityResp.getDocCoverage()) * 100 / qoqQualityResp.getDocCoverage()))
//                .tendency(qualityResp.getDocCoverage() - qoqQualityResp.getDocCoverage() > 0 ? 1 : 2)
                .build());
        yoyQualityMetric.add(Metric.builder()
                .name("注释覆盖度")
                .value(String.format("%.2f", qualityResp.getDocCoverage() * 100) + "%")
                .oldValue(String.format("%.2f", yoyQualityResp.getDocCoverage()))
//                .rate(String.format("%.2f", (qualityResp.getDocCoverage() - yoyQualityResp.getDocCoverage()) * 100 / yoyQualityResp.getDocCoverage()))
//                .tendency(qualityResp.getDocCoverage() - yoyQualityResp.getDocCoverage() > 0 ? 1 : 2)
                .build());
        qoqQualityMetric.add(Metric.builder()
                .name("测试覆盖度")
                .value(String.format("%.2f", qualityResp.getStaticTestCoverage() * 100) + "%")
                .oldValue(String.format("%.2f", qoqQualityResp.getStaticTestCoverage() * 100) + "%")
//                .rate(String.format("%.2f", (qualityResp.getStaticTestCoverage() - qoqQualityResp.getStaticTestCoverage()) * 100 / qoqQualityResp.getStaticTestCoverage()))
//                .tendency(qualityResp.getStaticTestCoverage() - qoqQualityResp.getStaticTestCoverage() > 0 ? 1 : 2)
                .build());
        yoyQualityMetric.add(Metric.builder()
                .name("测试覆盖度")
                .value(String.format("%.2f", qualityResp.getStaticTestCoverage() * 100) + "%")
                .oldValue(String.format("%.2f", yoyQualityResp.getStaticTestCoverage() * 100) + "%")
//                .rate(String.format("%.2f", (qualityResp.getStaticTestCoverage() - yoyQualityResp.getStaticTestCoverage()) * 100 / yoyQualityResp.getStaticTestCoverage()))
//                .tendency(qualityResp.getStaticTestCoverage() - yoyQualityResp.getStaticTestCoverage() > 0 ? 1 : 2)
                .build());
        qoqQualityMetric.add(Metric.builder()
                .name("代码模块性")
                .value(String.format("%.2f", qualityResp.getModularity() * 100) + "%")
                .oldValue(String.format("%.2f", qoqQualityResp.getModularity() * 100) + "%")
//                .rate(String.format("%.2f", (qualityResp.getModularity() - qoqQualityResp.getModularity()) * 100 / qoqQualityResp.getModularity()))
//                .tendency(qualityResp.getModularity() - qoqQualityResp.getModularity() > 0 ? 1 : 2)
                .build());
        yoyQualityMetric.add(Metric.builder()
                .name("代码模块性")
                .value(String.format("%.2f", qualityResp.getModularity() * 100) + "%")
                .oldValue(String.format("%.2f", yoyQualityResp.getModularity() * 100) + "%")
//                .rate(String.format("%.2f", (qualityResp.getModularity() - yoyQualityResp.getModularity()) * 100 / yoyQualityResp.getModularity()))
//                .tendency(qualityResp.getModularity() - yoyQualityResp.getModularity() > 0 ? 1 : 2)
                .build());

        List<Metric> qoqCostMetric = new ArrayList<>();
        List<Metric> yoyCostMetric = new ArrayList<>();
        qoqCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">总人数</span>/研发人数/开发人员占比</span>")
                .value("<span style=\"font-weight: bold\">" + costResp.getDepartmentTotal() + "</span></span>/" + costResp.getDevelopmentNumber() + "/" + costResp.getDeveloperPercentage() * 100 +"%")
                .oldValue("<span style=\"font-weight: bold\">" + costResp.getDepartmentTotal() + "</span></span>/" + costResp.getDevelopmentNumber() + "/" + costResp.getDeveloperPercentage() * 100 +"%")
                .rate("0")
                .tendency(0).build());
        qoqCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">休假天数</span>/人均休假天数</span>")
                .value("<span style=\"font-weight: bold\">465</span></span>/6")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/5")
                .rate("12.67")
                .tendency(2).build());
        qoqCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">出勤工时</span>/开发工时/工时饱和度</span>")
                .value("<span style=\"font-weight: bold\">465</span></span>/312/67.09%")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/472/87.32%")
                .rate("12.67")
                .tendency(2).build());

        yoyCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">总人数</span>/研发人数/开发人员占比</span>")
                .value("<span style=\"font-weight: bold\">" + costResp.getDepartmentTotal() + "</span></span>/" + costResp.getDevelopmentNumber() + "/" + costResp.getDeveloperPercentage() * 100 +"%")
                .oldValue("<span style=\"font-weight: bold\">" + costResp.getDepartmentTotal() + "</span></span>/" + costResp.getDevelopmentNumber() + "/" + costResp.getDeveloperPercentage() * 100 +"%")
                .rate("0")
                .tendency(0).build());
        qoqCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">休假天数</span>/人均休假天数</span>")
                .value("<span style=\"font-weight: bold\">465</span></span>/6")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/5")
                .rate("12.67")
                .tendency(2).build());
        yoyCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">出勤工时</span>/开发工时/工时饱和度</span>")
                .value("<span style=\"font-weight: bold\">465</span></span>/312/67.09%")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/472/87.32%")
                .rate("12.67")
                .tendency(2).build());
        TeamOverviewResp cost = TeamOverviewResp.builder()
                .id(5)
                .name("成本类")
                .qoqMetricList(qoqCostMetric)
                .yoyMetricList(yoyCostMetric)
                .style("border-right: dashed #A9A9A9;")
                .build();

        List<Metric> qoqSlaMetric = new ArrayList<>();
        List<Metric> yoySlaMetric = new ArrayList<>();
        double robustness = stabilityResp.getRobustness();
        double qoqRobustness = qoqStabilityResp.getRobustness();
        double yoyRobustness = yoyStabilityResp.getRobustness();
        double stability = stabilityResp.getStability();
        double qoqStability = qoqStabilityResp.getStability();
        double yoyStability = yoyStabilityResp.getStability();
        qoqSlaMetric.add(Metric.builder()
                .name("团队鲁棒性")
                .value(String.format("%.2f", robustness))
                .oldValue(String.format("%.2f", qoqRobustness))
                .rate(String.format("%.2f", Math.abs(robustness - qoqRobustness) * 100 / qoqRobustness))
                .tendency(isEqual(robustness, qoqRobustness) ? 0 : robustness > qoqRobustness ? 1 : 2).build());
        qoqSlaMetric.add(Metric.builder()
                .name("团队稳定性")
                .value(String.format("%.2f", stability))
                .oldValue(String.format("%.2f", qoqStability))
                .rate(String.format("%.2f", Math.abs(stability - qoqStability) * 100 / qoqStability))
                .tendency(isEqual(stability, qoqStability) ? 0 : stability > qoqStability ? 1 : 2).build());
        yoySlaMetric.add(Metric.builder()
                .name("团队鲁棒性")
                .value(String.format("%.2f", robustness))
                .oldValue(String.format("%.2f", yoyRobustness))
                .rate(String.format("%.2f", Math.abs(robustness - yoyRobustness) * 100 / yoyRobustness))
                .tendency(isEqual(robustness, yoyRobustness) ? 0 : robustness > qoqRobustness ? 1 : 2).build());
        yoySlaMetric.add(Metric.builder()
                .name("团队稳定性")
                .value(String.format("%.2f", stability))
                .oldValue(String.format("%.2f", yoyStability))
                .rate(String.format("%.2f", Math.abs(stability - yoyStability) * 100 / yoyStability))
                .tendency(isEqual(stability, yoyStability) ? 0 : stability > yoyStability ? 1 : 2).build());
        TeamOverviewResp sla = TeamOverviewResp.builder()
                .id(6)
                .name("稳定性类")
                .qoqMetricList(qoqSlaMetric)
                .yoyMetricList(yoySlaMetric)
                .style("")
                .build();

        overviewRespList.add(production);
        overviewRespList.add(efficiency);
        overviewRespList.add(publication);
        overviewRespList.add(quality);
        overviewRespList.add(cost);
        overviewRespList.add(sla);

        /**
         * 部门工程指标-部门占比统计
         */
        Map<String, List<AnalysisResp>> deptMap =
                analysisData.stream().filter(x -> StringUtils.isNotEmpty(x.getDepartmentName()))
                        .collect(Collectors.groupingBy(AnalysisResp::getDepartmentName));
        DeptProportion deptProportion = this.getDeptProportion(deptMap);

        /**
         * 部门工程指标-部门趋势统计
         */
        DeptTendency deptTendency = this.getDeptTendency(deptMap);
        List<String> monthList = this.generateMonthList(ctoReq.getStartDate(), ctoReq.getEndDate());
        deptTendency.setMonthList(monthList);

        /**
         * 部门效率与稳定性统计：开发当量离散系数 = 标准差 / 均值
         * (人均当量，开发当量离散系数，部门名)
         */
        List<List<Object>> deptEffAndStab = new ArrayList<>(16);
        deptMap.forEach((depName, deptList) -> {
            List<Double> devEquivalent = new ArrayList<>(16);
            List<Object> l = new ArrayList<>(16);
            deptList.forEach(o -> o.setStatisticTime(dateToISODate(o.getStatisticTime())));
            Map<Date, List<AnalysisResp>> collect5 =
                    deptList.stream().collect(Collectors.groupingBy(AnalysisResp::getStatisticTime));
            collect5.forEach((k, v) -> {
                double sum = v.stream().mapToDouble(AnalysisResp::getDevEquivalent).sum();
                devEquivalent.add(sum);
            });
            double variance = Variance(devEquivalent);
            double mean = Mean(devEquivalent);
            double coefficient = variance / mean;
            l.add(String.format("%.0f", mean));
            l.add(String.format("%.2f", coefficient));
            l.add(depName);
            deptEffAndStab.add(l);
        });

        /**
         * 员工工程指标统计
         */
        Map<String, List<AnalysisResp>> empMap;
        if (CollectionUtils.isNotEmpty(ctoReq.getSelectUserCode())) {
            Set<EhrUserDetailResp> userSetFromDept = getUserSetFromAdList(ctoReq.getSelectUserCode());
            List<String> subAdCodeList = userSetFromDept.stream().map(EhrUserDetailResp::getEmail).filter(StringUtils::isNotEmpty).map(this::convertEmail2Adcode)
                    .collect(Collectors.toList());
            List<AnalysisResp> analysisResp =
                    flinkAnalysisComponent.getAnalysisResp(ctoReq.getStartDate(), ctoReq.getEndDate(), subAdCodeList);
            empMap = analysisResp.stream().filter(x -> StringUtils.isNotEmpty(x.getUid()))
                    .collect(Collectors.groupingBy(AnalysisResp::getUid));
        } else if (StringUtils.isNotEmpty(ctoReq.getSubDeptId())) {
            Set<EhrUserDetailResp> userSetFromDept = getUserSetFromDept(ctoReq.getSubDeptId());
            List<String> subAdCodeList = userSetFromDept.stream().map(EhrUserDetailResp::getEmail).filter(StringUtils::isNotEmpty).map(this::convertEmail2Adcode)
                    .collect(Collectors.toList());
            List<AnalysisResp> analysisResp =
                    flinkAnalysisComponent.getAnalysisResp(ctoReq.getStartDate(), ctoReq.getEndDate(), subAdCodeList);
            empMap = analysisResp.stream().filter(x -> StringUtils.isNotEmpty(x.getUid()))
                    .collect(Collectors.groupingBy(AnalysisResp::getUid));
        } else {
            empMap = analysisData.stream().filter(x -> StringUtils.isNotEmpty(x.getUid()))
                    .collect(Collectors.groupingBy(AnalysisResp::getUid));
        }
        EmployeeProportion employeeProportion = getEmployeeProportion(empMap, userMap);
        EmployeeTendency employeeTendency = getEmployeeTendency(empMap, employeeProportion, userMap);
        employeeTendency.setMonthList(monthList);

        /**
         * 职级工程指标统计-职级占比统计
         */
        Map<String, List<AnalysisResp>> levelMap =
                analysisData.stream().filter(x -> StringUtils.isNotEmpty(x.getUid()))
                        .collect(Collectors.groupingBy(AnalysisResp::getLevel));
        LevelProportion levelProportion = getLevelProportion(levelMap);

        /**
         * 职级工程指标统计-职级趋势统计
         */
        LevelTendency levelTendency = getLevelTendency(levelMap);
        levelTendency.setMonthList(monthList);

        return CtoResp.builder()
                .teamOverviewResp(overviewRespList)
                .deptProportion(deptProportion)
                .deptTendency(deptTendency)
                .deptEffAndStab(deptEffAndStab)
                .employeeProportion(employeeProportion)
                .employeeTendency(employeeTendency)
                .levelProportion(levelProportion)
                .levelTendency(levelTendency)
                .build();
    }

    private DeptProportion getDeptProportion(Map<String, List<AnalysisResp>> deptMap) {

        return DeptProportion.builder()
                .devEquivalentList(buildNameValueList(deptMap, AnalysisResp::getDevEquivalent))
                .codeLineList(buildNameValueList(deptMap, AnalysisResp::getInsertions))
                .commitList(buildNameValueList(deptMap, AnalysisResp::getCommitCount))
                .publishList(buildNameValueList(deptMap, AnalysisResp::getPublishNum))
                .compileList(buildNameValueList(deptMap, AnalysisResp::getCompileNum))
                .onlineList(buildNameValueList(deptMap, AnalysisResp::getOnlineNum))
                .rollbackList(buildNameValueList(deptMap, AnalysisResp::getRollbackNum))
                .restartList(buildNameValueList(deptMap, AnalysisResp::getRestartNum))
                .build();
    }

    private List<NameValue> buildNameValueList(Map<String, List<AnalysisResp>> deptMap, ToLongFunction<AnalysisResp> mapper) {
        List<NameValue> nameValueList = new ArrayList<>(16);
        deptMap.forEach((name, list) -> {
            long sum = list.stream().mapToLong(mapper).sum();
            NameValue build = NameValue.builder()
                    .name(name)
                    .value(String.valueOf(sum))
                    .build();
            nameValueList.add(build);
        });
        return nameValueList;
    }

    private DeptTendency getDeptTendency(Map<String, List<AnalysisResp>> deptMap) {

        return DeptTendency.builder()
                .devEquivalentTendencyList(buildDeptItemList(deptMap, AnalysisResp::getDevEquivalent))
                .codeListTendencyList(buildDeptItemList(deptMap, AnalysisResp::getInsertions))
                .commitTendencyList(buildDeptItemList(deptMap, AnalysisResp::getCommitCount))
                .publishTendencyList(buildDeptItemList(deptMap, AnalysisResp::getPublishNum))
                .compileTendencyList(buildDeptItemList(deptMap, AnalysisResp::getCompileNum))
                .onlineTendencyList(buildDeptItemList(deptMap, AnalysisResp::getOnlineNum))
                .restartTendencyList(buildDeptItemList(deptMap, AnalysisResp::getRestartNum))
                .rollbackTendencyList(buildDeptItemList(deptMap, AnalysisResp::getRollbackNum))
                .build();
    }

    private List<DeptTendencyItem> buildDeptItemList(Map<String, List<AnalysisResp>> deptMap, ToLongFunction<AnalysisResp> mapper) {
        List<DeptTendencyItem> tendencyList = new ArrayList<>(16);
        deptMap.forEach((name, list) -> {
            Map<String, List<AnalysisResp>> dateMap = list.stream().collect(
                    Collectors.groupingBy(o -> this.parse_yyyyMMdd(this.dateToLocalDateTime(o.getStatisticTime()))));
            long[] dataArray = new long[32];
            dateMap.forEach((date, record) -> {
                int day = Integer.parseInt(date.substring(date.length() - 2));
                long sum = record.stream().mapToLong(mapper).sum();
                dataArray[day] = sum;
            });
            DeptTendencyItem deptTendencyItem = DeptTendencyItem.builder()
                    .name(name)
                    .type("line")
                    .data(Arrays.stream(dataArray).boxed().collect(Collectors.toList()))
                    .build();
            tendencyList.add(deptTendencyItem);
        });
        return tendencyList;
    }

    private EmployeeProportion getEmployeeProportion(Map<String, List<AnalysisResp>> map, Map<String, EhrUserDetailResp> userMap) {

        return EmployeeProportion.builder()
                .devEquivalentList(buildNameValueSortedLimitList(map, AnalysisResp::getDevEquivalent, userMap))
                .codeLineList(buildNameValueSortedLimitList(map, AnalysisResp::getInsertions, userMap))
                .commitList(buildNameValueSortedLimitList(map, AnalysisResp::getCommitCount, userMap))
                .publishList(buildNameValueSortedLimitList(map, AnalysisResp::getPublishNum, userMap))
                .compileList(buildNameValueSortedLimitList(map, AnalysisResp::getCompileNum, userMap))
                .onlineList(buildNameValueSortedLimitList(map, AnalysisResp::getOnlineNum, userMap))
                .rollbackList(buildNameValueSortedLimitList(map, AnalysisResp::getRollbackNum, userMap))
                .restartList(buildNameValueSortedLimitList(map, AnalysisResp::getRestartNum, userMap))
                .build();
    }

    private List<NameValue> buildNameValueSortedLimitList(Map<String, List<AnalysisResp>> map, ToLongFunction<AnalysisResp> mapper, Map<String, EhrUserDetailResp> userMap) {

        List<NameValue> nameValueList = new ArrayList<>();
        map.forEach((uid, list) -> {
            long sum = list.stream().mapToLong(mapper).sum();
            EhrUserDetailResp ehrUserDetailResp = userMap.get(uid + "@ziroom.com");
            NameValue devNV = NameValue.builder()
                    .name(ehrUserDetailResp.getName())
                    .value(String.valueOf(sum))
                    .build();
            nameValueList.add(devNV);
        });
        nameValueList.sort(((o1, o2) -> Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue())));
//        return nameValueList.stream().limit(15L).collect(Collectors.toList());
        return nameValueList;
    }

    private EmployeeTendency getEmployeeTendency(Map<String, List<AnalysisResp>> empMap, EmployeeProportion employeeProportion, Map<String, EhrUserDetailResp> userMap) {

        return EmployeeTendency.builder()
                .devEquivalentTendencyList(buildEmpTendency(employeeProportion.getDevEquivalentList(), empMap, AnalysisResp::getDevEquivalent, userMap))
                .codeListTendencyList(buildEmpTendency(employeeProportion.getCodeLineList(), empMap, AnalysisResp::getInsertions, userMap))
                .commitTendencyList(buildEmpTendency(employeeProportion.getCommitList(), empMap, AnalysisResp::getCommitCount, userMap))
                .publishTendencyList(buildEmpTendency(employeeProportion.getPublishList(), empMap, AnalysisResp::getPublishNum, userMap))
                .compileTendencyList(buildEmpTendency(employeeProportion.getCompileList(), empMap, AnalysisResp::getCompileNum, userMap))
                .onlineTendencyList(buildEmpTendency(employeeProportion.getOnlineList(), empMap, AnalysisResp::getOnlineNum, userMap))
                .restartTendencyList(buildEmpTendency(employeeProportion.getRestartList(), empMap, AnalysisResp::getRestartNum, userMap))
                .rollbackTendencyList(buildEmpTendency(employeeProportion.getRollbackList(), empMap, AnalysisResp::getRollbackNum, userMap))
                .build();
    }

    private List<EmployeeTendencyItem> buildEmpTendency(List<NameValue> nameValueList, Map<String, List<AnalysisResp>> empMap, ToLongFunction<AnalysisResp> mapper, Map<String, EhrUserDetailResp> userMap) {
        List<String> nameList = nameValueList.stream().map(NameValue::getName).limit(20L).collect(Collectors.toList());
        /**
         * 员工工程指标统计-员工趋势统计
         */
        List<EmployeeTendencyItem> employeeTendencyItemList = new ArrayList<>(16);
        empMap.forEach((uid, list) -> {
            Map<String, List<AnalysisResp>> dateMap = list.stream().collect(
                    Collectors.groupingBy(o -> this.parse_yyyyMMdd(this.dateToLocalDateTime(o.getStatisticTime()))));
            long[] dataArray = new long[32];
            dateMap.forEach((date, record) -> {
                int day = Integer.parseInt(date.substring(date.length() - 2));
                long sum = record.stream().mapToLong(mapper).sum();
                dataArray[day] = sum;
            });

            EhrUserDetailResp ehrUserDetailResp = userMap.get(uid + "@ziroom.com");
            if (nameList.contains(ehrUserDetailResp.getName())) {
                EmployeeTendencyItem deptTendencyItem = EmployeeTendencyItem.builder()
                        .name(ehrUserDetailResp.getName())
                        .type("line")
                        .data(Arrays.stream(dataArray).boxed().collect(Collectors.toList()))
                        .build();
                employeeTendencyItemList.add(deptTendencyItem);
            }
        });
//        return employeeTendencyItemList.stream().limit(20).collect(Collectors.toList());
        return employeeTendencyItemList;
    }

    private LevelProportion getLevelProportion(Map<String, List<AnalysisResp>> map) {

        return LevelProportion.builder()
                .devEquivalentList(buildNameValueListSorted(map, AnalysisResp::getDevEquivalent))
                .codeLineList(buildNameValueListSorted(map, AnalysisResp::getInsertions))
                .commitList(buildNameValueListSorted(map, AnalysisResp::getCommitCount))
                .publishList(buildNameValueListSorted(map, AnalysisResp::getPublishNum))
                .compileList(buildNameValueListSorted(map, AnalysisResp::getCompileNum))
                .onlineList(buildNameValueListSorted(map, AnalysisResp::getOnlineNum))
                .rollbackList(buildNameValueListSorted(map, AnalysisResp::getRollbackNum))
                .restartList(buildNameValueListSorted(map, AnalysisResp::getRestartNum))
                .build();
    }

    private List<NameValue> buildNameValueListSorted(Map<String, List<AnalysisResp>> map, ToLongFunction<AnalysisResp> mapper) {
        List<NameValue> nameValueList = new ArrayList<>(16);
        map.forEach((name, list) -> {
            long sum = list.stream().mapToLong(mapper).sum();
            NameValue build = NameValue.builder()
                    .name(name)
                    .value(String.valueOf(sum))
                    .build();
            nameValueList.add(build);
        });
        nameValueList.sort(((o1, o2) -> Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue())));
        return nameValueList;
    }

    private LevelTendency getLevelTendency(Map<String, List<AnalysisResp>> levelMap) {

        return LevelTendency.builder()
                .devEquivalentTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getDevEquivalent))
                .codeListTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getInsertions))
                .commitTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getCommitCount))
                .publishTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getPublishNum))
                .compileTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getCompileNum))
                .onlineTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getOnlineNum))
                .restartTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getRestartNum))
                .rollbackTendencyList(buildLevelTendencyList(levelMap, AnalysisResp::getRollbackNum))
                .build();
    }

    private List<LevelTendencyItem> buildLevelTendencyList(Map<String, List<AnalysisResp>> levelMap, ToLongFunction<AnalysisResp> mapper) {
        List<LevelTendencyItem> levelTendencyItemList = new ArrayList<>(16);
        levelMap.forEach((levelName, list) -> {
            Map<String, List<AnalysisResp>> dateMap = list.stream().collect(
                    Collectors.groupingBy(o -> this.parse_yyyyMMdd(this.dateToLocalDateTime(o.getStatisticTime()))));
            long[] dataArray = new long[32];
            dateMap.forEach((date, record) -> {
                int day = Integer.parseInt(date.substring(date.length() - 2));
                long sum = record.stream().mapToLong(mapper).sum();
                dataArray[day] = sum;
            });

            LevelTendencyItem levelTendencyItem = LevelTendencyItem.builder()
                    .name(levelName)
                    .type("line")
                    .data(Arrays.stream(dataArray).boxed().collect(Collectors.toList()))
                    .build();
            levelTendencyItemList.add(levelTendencyItem);
        });
        return levelTendencyItemList;
    }


    private String parse_yyyyMMdd(LocalDateTime time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return df.format(time);
    }

    private String parseDate_yyyyMMdd(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    private Date removeDateTime(Date dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(dateTime);
        return null;
    }

    private Date dateToISODate(Date dateStr) {
        Date parse = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            parse = format.parse(format.format(dateStr));
        } catch (ParseException e) {
            log.info("e",e);
        }
        return parse;
    }

    private String parse_dd(LocalDateTime time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd");
        return df.format(time);
    }

    public LocalDateTime dateToLocalDateTime(Date time){
        Instant it = time.toInstant();
        ZoneId zid = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(it, zid);
    }

    private LocalDateTime string2LocalDateTime(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private List<String> generateMonthList(Date start, Date end) {
        List<String> result = new ArrayList<>(32);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = Instant.ofEpochMilli(start.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(end.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        for (LocalDate i = startDate; i.isBefore(endDate.plusDays(1L)); i = i.plusDays(1L)) {
            result.add(i.format(dtf));
        }
        return result;
    }

    private void getCalculateData(
            String name,
            List<AnalysisResp> analysisData,
            List<AnalysisResp> qoqAnalysisData,
            List<AnalysisResp> yoyAnalysisData,
            ToLongFunction<AnalysisResp> mapper,
            List<Metric> qoqMetricList,
            List<Metric> yoyMetricList) {
        long sum = analysisData.stream().mapToLong(mapper).sum();
        long qoqSum = qoqAnalysisData.stream().mapToLong(mapper).sum();
        long yoySum = yoyAnalysisData.stream().mapToLong(mapper).sum();
        double qoqRate = Math.abs((sum - qoqSum) * 1.0 / qoqSum) * 100;
        double yoyRate = Math.abs((sum - yoySum) * 1.0 / yoySum) * 100;
        Metric qoqMetric = Metric.builder()
                .name(name)
                .value(Long.toString(sum))
                .oldValue(Long.toString(qoqSum))
                .rate(String.format("%.2f", qoqRate))
                .tendency(sum == qoqSum ? 0 : sum > qoqSum ? 1 : 2).build();
        Metric yoyMetric = Metric.builder()
                .name(name)
                .value(Long.toString(sum))
                .oldValue(Long.toString(yoySum))
                .rate(String.format("%.2f", yoyRate))
                .tendency(sum == qoqSum ? 0 : sum > yoySum ? 1 : 2).build();
        qoqMetricList.add(qoqMetric);
        yoyMetricList.add(yoyMetric);
    }

    private Calendar getQoQStart(Date date) {
        Calendar qoqStart = Calendar.getInstance();
        qoqStart.setTime(date);
        qoqStart.add(Calendar.MONTH, -1);
        qoqStart.set(Calendar.DAY_OF_MONTH, 1);
        return qoqStart;
    }

    private Calendar getQoQEnd(Date date) {
        Calendar qoqEnd = Calendar.getInstance();
        qoqEnd.setTime(date);
        qoqEnd.set(Calendar.DAY_OF_MONTH, 0);
        return qoqEnd;
    }

    private Calendar getYoYStart(Date date) {
        Calendar yoyStart = Calendar.getInstance();
        yoyStart.setTime(date);
        yoyStart.add(Calendar.YEAR, -1);
        yoyStart.set(Calendar.DAY_OF_MONTH, 1);
        return yoyStart;
    }

    private Calendar getYoyEnd(Date date) {
        Calendar yoyEnd = Calendar.getInstance();
        yoyEnd.setTime(date);
        yoyEnd.add(Calendar.YEAR, -1);
        yoyEnd.add(Calendar.MONTH, 1);
        yoyEnd.set(Calendar.DAY_OF_MONTH, 0);
        return yoyEnd;
    }

    /**
     * 计算标准差
     * @param xx
     * @return
     */
    private double Variance(List<Double> xx) {
        Double[] x = xx.toArray(new Double[0]);
        int m = x.length;
        double sum = 0;
        for (double value : x) {
            sum += value;
        }
        double dAve = sum / m;
        double dVar = 0;
        for (double v : x) {
            dVar += (v - dAve) * (v - dAve);
        }
        return Math.sqrt(dVar / m);
    }

    /**
     * 计算平均值
     * @param xx
     * @return
     */
    private double Mean(List<Double> xx) {
        Double[] x = xx.toArray(new Double[0]);
        int m = x.length;
        double sum = 0;
        for (double value : x) {
            sum += value;
        }
        return sum / m;
    }

    /**
     * 判断浮点数是否相等
     * @param a
     * @param b
     * @return
     */
    public boolean isEqual(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return Math.abs(a - b) < 0.001d;
    }


    @Override
    public PersonResp getPersonData(PersonReq personReq) {

        UserDetailResp userDetail = ehrComponent.getUserDetail(personReq.getUid());
        String adCode;
        if (Objects.nonNull(userDetail)) {
            adCode = convertEmail2Adcode(userDetail.getEmail());
        } else {
            return PersonResp.builder().build();
        }

        List<AnalysisResp> analysisResp
                = flinkAnalysisComponent.getAnalysisResp(personReq.getStartTime(), personReq.getEndTime(),
                        Lists.newArrayList(adCode));
        long devEquivalentS = analysisResp.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long insertionsS = analysisResp.stream().mapToLong(AnalysisResp::getInsertions).sum();
        long deletionsS = analysisResp.stream().mapToLong(AnalysisResp::getDeletions).sum();
        long commitCountS = analysisResp.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long processDemandS = analysisResp.stream().mapToLong(AnalysisResp::getProcessDemandCount).sum();
        long bugFixS = analysisResp.stream().mapToLong(AnalysisResp::getFixBugCount).sum();
        long completeProjectCountS = analysisResp.stream().mapToLong(AnalysisResp::getCompleteProjectCount).sum();

        long publishCountS = analysisResp.stream().mapToLong(AnalysisResp::getPublishNum).sum();
        long compileS = analysisResp.stream().mapToLong(AnalysisResp::getCompileNum).sum();
        long onlineS = analysisResp.stream().mapToLong(AnalysisResp::getOnlineNum).sum();
        long rollbackS = analysisResp.stream().mapToLong(AnalysisResp::getRollbackNum).sum();
        long restartS = analysisResp.stream().mapToLong(AnalysisResp::getRestartNum).sum();

        PersonOverview personOverview = PersonOverview.builder()
                .devEquivalent(devEquivalentS)
                .codeLine(insertionsS + "/" + deletionsS)
                .commitCount(commitCountS)
                .summaryData(completeProjectCountS + "/" + processDemandS + "/" + bugFixS)
                .devValue("")
                .valueDensity("")
                .publishNum(publishCountS)
                .compileNum(compileS)
                .onlineNum(onlineS)
                .rollbackNum(rollbackS)
                .restartNum(restartS)
                .build();

        List<PersonalByDay> personalDevEquivalentByDays = new ArrayList<>(32);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        analysisResp.forEach(resp -> {
            PersonalByDay personalByDay = PersonalByDay.builder()
                    .day(simpleDateFormat.format(resp.getStatisticTime()))
                    .devEquivalent(resp.getDevEquivalent().intValue())
                    .insertions(resp.getInsertions().intValue())
                    .build();
            personalDevEquivalentByDays.add(personalByDay);
        });

        return PersonResp.builder()
                .personOverview(personOverview)
                .personalByDays(personalDevEquivalentByDays)
                .build();
    }
}

