package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.FlinkAnalysisComponent;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityResp;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import lombok.Builder;
import lombok.Data;
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

    private String convertEmail2Adcode(String email) {
        return email.split("@")[0];
    }

    @Override
    public CtoResp getCtoResp(CTOReq ctoReq) throws ExecutionException, InterruptedException {
        Set<EhrUserResp> users = ehrComponent.getUsers(ctoReq.getDeptId(), 101);
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
        Map<String, EhrUserDetailResp> userMap =
                userRespSet.stream().collect(Collectors.toMap(EhrUserDetailResp::getEmail, Function.identity(), (value1, value2) -> value2));
        List<String> adCodeList = userRespSet.stream().map(EhrUserDetailResp::getEmail).filter(StringUtils::isNotEmpty).map(this::convertEmail2Adcode)
                .collect(Collectors.toList());

        Calendar qoqStart = Calendar.getInstance();
        qoqStart.setTime(ctoReq.getStartDate());
        qoqStart.add(Calendar.MONTH, -1);
        qoqStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar qoqEnd = Calendar.getInstance();
        qoqEnd.setTime(ctoReq.getEndDate());
        qoqEnd.set(Calendar.DAY_OF_MONTH, 0);

        Calendar yoyStart = Calendar.getInstance();
        yoyStart.setTime(ctoReq.getStartDate());
        yoyStart.add(Calendar.YEAR, -1);
        yoyStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar yoyEnd = Calendar.getInstance();
        yoyEnd.setTime(ctoReq.getEndDate());
        yoyEnd.add(Calendar.YEAR, -1);
        yoyEnd.add(Calendar.MONTH, 1);
        yoyEnd.set(Calendar.DAY_OF_MONTH, 0);

        CompletableFuture<List<AnalysisResp>> currentFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(ctoReq.getStartDate(), ctoReq.getEndDate(), adCodeList));

        CompletableFuture<List<AnalysisResp>> qoqFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(qoqStart.getTime(), qoqEnd.getTime(), adCodeList));

        CompletableFuture<List<AnalysisResp>> yoyFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(yoyStart.getTime(), yoyEnd.getTime(), adCodeList));
//
        CompletableFuture<List<StabilityResp>> stabilityFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getStabilityResp(ctoReq.getStartDate(), ctoReq.getEndDate(), ctoReq.getDeptId()));

        List<List<AnalysisResp>> collect = Stream.of(currentFuture, qoqFuture, yoyFuture).map(CompletableFuture::join)
                .collect(Collectors.toList());

        List<AnalysisResp> analysisData = collect.get(0);
        List<AnalysisResp> qoqAnalysisData = collect.get(1);
        List<AnalysisResp> yoyAnalysisData = collect.get(2);
        List<StabilityResp> stabilityResp = stabilityFuture.get();
        analysisData.forEach(o -> {
            EhrUserDetailResp ehrUserDetailResp = userMap.get(o.getUid() + "@ziroom.com");
            o.setLevel(ehrUserDetailResp.getLevelName());
        });

        /**
         * 核心数据指标
         */
        List<TeamOverviewResp> overviewRespList = new ArrayList<>(16);

        List<Metric> qoqProductionMetric = new ArrayList<>(16);
        List<Metric> yoyProductionMetric = new ArrayList<>(16);
        getCalculateData("开发当量", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getDevEquivalent, qoqProductionMetric, yoyProductionMetric);
        qoqProductionMetric.add(Metric.builder().name("人均当量").value("-").oldValue("-").rate("0").tendency(1).build());
        getCalculateData("代码行数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getInsertions, qoqProductionMetric, yoyProductionMetric);
        getCalculateData("代码提交次数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqProductionMetric, yoyProductionMetric);
//        productionMetric.add(Metric.builder().name("项目数").value("-").oldValue("-").rate("23").tendency(0).build());
        getCalculateData("修复bug数", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getFixBugCount, qoqProductionMetric, yoyProductionMetric);
        qoqProductionMetric.add(Metric.builder().name("开发价值").value("-").oldValue("-").rate("1.4").tendency(0).build());
//        qoqProductionMetric.add(Metric.builder().name("价值密度").value("2333").oldValue("23333").rate("110").tendency(1).build());
        TeamOverviewResp production = TeamOverviewResp.builder()
                .id(1)
                .name("产出类")
                .qoqMetricList(qoqProductionMetric)
                .yoyMetricList(yoyProductionMetric)
                .style("border-right: dashed #A9A9A9; border-bottom: dashed #A9A9A9; margin-top: 15px")
                .build();

        List<Metric> qoqEfficiencyMetric = new ArrayList<>();
        List<Metric> yoyEfficiencyMetric = new ArrayList<>();
        getCalculateData("项目平均开发周期", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getFixBugCount, qoqEfficiencyMetric, yoyEfficiencyMetric);
        getCalculateData("bug平均修复时间", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getFixBugCount, qoqEfficiencyMetric, yoyEfficiencyMetric);
        getCalculateData("bug平均修复时间", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getFixBugCount, qoqEfficiencyMetric, yoyEfficiencyMetric);
        getCalculateData("bug平均修复时间", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getFixBugCount, qoqEfficiencyMetric, yoyEfficiencyMetric);
        TeamOverviewResp efficiency = TeamOverviewResp.builder()
                .id(2)
                .name("效率类")
                .style("border-bottom: dashed #A9A9A9; margin-top: 15px")
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
                .style("border-right: dashed #A9A9A9; border-bottom: dashed #A9A9A9;")
                .build();

        List<Metric> qoqQualityMetric = new ArrayList<>();
        List<Metric> yoyQualityMetric = new ArrayList<>();
        getCalculateData("注释覆盖度", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
        getCalculateData("测试覆盖度", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
        getCalculateData("代码模块性", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
        getCalculateData("千行bug率", analysisData, qoqAnalysisData, yoyAnalysisData, AnalysisResp::getCommitCount, qoqQualityMetric, yoyQualityMetric);
        TeamOverviewResp quality = TeamOverviewResp.builder()
                .id(4)
                .name("质量类")
                .qoqMetricList(qoqQualityMetric)
                .yoyMetricList(yoyQualityMetric)
                .style("border-bottom: dashed #A9A9A9;")
                .build();

        List<Metric> qoqCostMetric = new ArrayList<>();
        List<Metric> yoyCostMetric = new ArrayList<>();
        qoqCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">总人数</span>/研发人数/开发人员占比</span>")
                .value("<span style=\"font-weight: bold\">465</span></span>/312/67.09%")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/472/87.32%")
                .rate("12.67")
                .tendency(2).build());
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
                .value("<span style=\"font-weight: bold\">465</span></span>/312/67.09%")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/472/87.32%")
                .rate("12.67")
                .tendency(2).build());
        yoyCostMetric.add(Metric.builder()
                .name("休假天数")
                .value("456")
                .oldValue("673")
                .rate("12.67")
                .tendency(2).build());
        yoyCostMetric.add(Metric.builder()
                .name("<span><span style=\"font-weight: bold\">出勤工时</span>/开发工时/工时饱和度</span>")
                .value("<span style=\"font-weight: bold\">465</span></span>/312/67.09%")
                .oldValue("<span style=\"font-weight: bold\">565</span></span>/472/87.32%")
                .rate("12.67")
                .tendency(2).build());
        yoyCostMetric.add(Metric.builder()
                .name("人均休假天数")
                .value("456")
                .oldValue("673")
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
        qoqSlaMetric.add(Metric.builder().name("团队鲁棒性").value("99.99%").oldValue("99.98%").rate("1").tendency(1).build());
        qoqSlaMetric.add(Metric.builder().name("团队稳定性").value("99.99%").oldValue("99.99%").rate("2").tendency(1).build());
        yoySlaMetric.add(Metric.builder().name("团队鲁棒性").value("99.99%").oldValue("99.98%").rate("1").tendency(1).build());
        yoySlaMetric.add(Metric.builder().name("团队稳定性").value("99.99%").oldValue("99.99%").rate("2").tendency(1).build());
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
        List<NameValue> deptDevEquivalentList = new ArrayList<>(16);
        List<NameValue> codeLineList = new ArrayList<>(16);
        Map<String, List<AnalysisResp>> deptMap =
                analysisData.stream().filter(x -> StringUtils.isNotEmpty(x.getDepartmentName()))
                        .collect(Collectors.groupingBy(AnalysisResp::getDepartmentName));
        deptMap.forEach((name, list) -> {
            long devS = list.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
            long codeS = list.stream().mapToLong(AnalysisResp::getInsertions).sum();
            NameValue devNV = NameValue.builder()
                    .name(name)
                    .value(String.valueOf(devS))
                    .build();
            deptDevEquivalentList.add(devNV);
            NameValue codeNV = NameValue.builder()
                    .name(name)
                    .value(String.valueOf(codeS))
                    .build();
            codeLineList.add(codeNV);
        });
        DeptProportion deptProportion = DeptProportion.builder()
                .devEquivalentList(deptDevEquivalentList)
                .codeLineList(codeLineList)
                .build();

        /**
         * 部门工程指标-部门趋势统计
         */
        List<DeptTendencyItem> deptTendencyItemList = new ArrayList<>(16);
        deptMap.forEach((name, list) -> {
            Map<String, List<AnalysisResp>> dateMap = list.stream().collect(
                    Collectors.groupingBy(o -> this.parse_yyyyMMdd(this.dateToLocalDateTime(o.getStatisticTime()))));
            long[] dataArray = new long[32];
            dateMap.forEach((date, record) -> {
                int day = Integer.parseInt(date.substring(date.length() - 2));
                long sum = record.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
                dataArray[day] = sum;
            });

            DeptTendencyItem deptTendencyItem = DeptTendencyItem.builder()
                    .name(name)
                    .type("line")
                    .data(Arrays.stream(dataArray).boxed().collect(Collectors.toList()))
                    .build();
            deptTendencyItemList.add(deptTendencyItem);
        });
        List<String> monthList = this.generateMonthList(ctoReq.getStartDate(), ctoReq.getEndDate());
        DeptTendency deptTendency = DeptTendency.builder()
                .monthList(monthList)
                .deptTendencyItemList(deptTendencyItemList)
                .build();


        /**
         * 员工工程指标统计
         */
        Map<String, List<AnalysisResp>> empMap =
                analysisData.stream().filter(x -> StringUtils.isNotEmpty(x.getUid()))
                        .collect(Collectors.groupingBy(AnalysisResp::getUid));
        List<NameValue> empDevEquivalentList = new ArrayList<>(16);
        List<NameValue> empCodeLineList = new ArrayList<>(16);

        empMap.forEach((uid, list) -> {
            long devS = list.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
            long codeS = list.stream().mapToLong(AnalysisResp::getInsertions).sum();
            EhrUserDetailResp ehrUserDetailResp = userMap.get(uid + "@ziroom.com");
            NameValue devNV = NameValue.builder()
                    .name(ehrUserDetailResp.getName())
                    .value(String.valueOf(devS))
                    .build();
            empDevEquivalentList.add(devNV);
            NameValue codeNV = NameValue.builder()
                    .name(ehrUserDetailResp.getName())
                    .value(String.valueOf(codeS))
                    .build();
            empCodeLineList.add(codeNV);
        });
        empDevEquivalentList.sort(((o1, o2) -> Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue())));
        empCodeLineList.sort(((o1, o2) -> Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue())));
        List<NameValue> sortedDevEquivalentList = empDevEquivalentList.stream().limit(20L).collect(Collectors.toList());
        List<NameValue> sortedEmpCodeLineList = empCodeLineList.stream().limit(20L).collect(Collectors.toList());
        EmployeeProportion employeeProportion = EmployeeProportion.builder()
                .devEquivalentList(sortedDevEquivalentList)
                .codeLineList(sortedEmpCodeLineList)
                .build();

        List<String> nameList = sortedDevEquivalentList.stream().map(NameValue::getName).collect(Collectors.toList());

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
                long sum = record.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
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
        EmployeeTendency employeeTendency = EmployeeTendency.builder()
                .monthList(monthList)
                .employeeTendencyItemList(employeeTendencyItemList)
                .build();

        /**
         * 职级工程指标统计-职级占比统计
         */
        Map<String, List<AnalysisResp>> levelMap =
                analysisData.stream().filter(x -> StringUtils.isNotEmpty(x.getUid()))
                        .collect(Collectors.groupingBy(AnalysisResp::getLevel));
        List<NameValue> levelDevEquivalentList = new ArrayList<>(16);
        List<NameValue> levelCodeLineList = new ArrayList<>(16);
        levelMap.forEach((levelName, list) -> {
            long devS = list.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
            long codeS = list.stream().mapToLong(AnalysisResp::getInsertions).sum();
            NameValue devNV = NameValue.builder()
                    .name(levelName)
                    .value(String.valueOf(devS))
                    .build();
            levelDevEquivalentList.add(devNV);
            NameValue codeNV = NameValue.builder()
                    .name(levelName)
                    .value(String.valueOf(codeS))
                    .build();
            levelCodeLineList.add(codeNV);
        });
        levelDevEquivalentList.sort(((o1, o2) -> Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue())));
        levelCodeLineList.sort(((o1, o2) -> Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue())));
        LevelProportion levelProportion = LevelProportion.builder()
                .devEquivalentList(levelDevEquivalentList)
                .codeLineList(levelCodeLineList)
                .build();


        /**
         * 职级工程指标统计-职级趋势统计
         */
        List<LevelTendencyItem> levelTendencyItemList = new ArrayList<>(16);
        levelMap.forEach((levelName, list) -> {
            Map<String, List<AnalysisResp>> dateMap = list.stream().collect(
                    Collectors.groupingBy(o -> this.parse_yyyyMMdd(this.dateToLocalDateTime(o.getStatisticTime()))));
            long[] dataArray = new long[32];
            dateMap.forEach((date, record) -> {
                int day = Integer.parseInt(date.substring(date.length() - 2));
                long sum = record.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
                dataArray[day] = sum;
            });

            LevelTendencyItem levelTendencyItem = LevelTendencyItem.builder()
                    .name(levelName)
                    .type("line")
                    .data(Arrays.stream(dataArray).boxed().collect(Collectors.toList()))
                    .build();
            levelTendencyItemList.add(levelTendencyItem);
        });
        LevelTendency levelTendency = LevelTendency.builder()
                .monthList(monthList)
                .levelTendencyItemList(levelTendencyItemList)
                .build();

        return CtoResp.builder()
                .teamOverviewResp(overviewRespList)
                .deptProportion(deptProportion)
                .deptTendency(deptTendency)
                .employeeProportion(employeeProportion)
                .employeeTendency(employeeTendency)
                .levelProportion(levelProportion)
                .levelTendency(levelTendency)
                .build();
    }

    private String parse_yyyyMMdd(LocalDateTime time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return df.format(time);
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
                .tendency(sum - qoqSum > 0 ? 1 : 2).build();
        Metric yoyMetric = Metric.builder()
                .name(name)
                .value(Long.toString(sum))
                .oldValue(Long.toString(yoySum))
                .rate(String.format("%.2f", yoyRate))
                .tendency(sum - qoqSum > 0 ? 1 : 2).build();
        qoqMetricList.add(qoqMetric);
        yoyMetricList.add(yoyMetric);
    }
}

@Data
@Builder
class Result {
    long sum;
    long qoqSum;
    long yoySum;
    double qoqRate;
    double yoyRate;
}
