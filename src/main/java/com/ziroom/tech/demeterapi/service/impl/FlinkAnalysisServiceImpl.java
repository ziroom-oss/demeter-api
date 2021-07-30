package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.FlinkAnalysisComponent;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
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

        Calendar startCal = Calendar.getInstance();
        startCal.add(Calendar.MONTH, -1);
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DAY_OF_MONTH, 0);

        Calendar yoyStart = Calendar.getInstance();
        yoyStart.add(Calendar.YEAR, -1);
        yoyStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar yoyEnd = Calendar.getInstance();
        yoyEnd.add(Calendar.MONTH, 1);
        yoyEnd.set(Calendar.DAY_OF_MONTH, 0);

        CompletableFuture<List<AnalysisResp>> currentFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(ctoReq.getStartDate(), ctoReq.getEndDate(), adCodeList));

        CompletableFuture<List<AnalysisResp>> qoqFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(startCal.getTime(), endCal.getTime(), adCodeList));

        CompletableFuture<List<AnalysisResp>> yoyFuture
                = CompletableFuture.supplyAsync(() -> flinkAnalysisComponent.getAnalysisResp(yoyStart.getTime(), yoyEnd.getTime(), adCodeList));

        List<List<AnalysisResp>> collect = Stream.of(currentFuture, qoqFuture, yoyFuture).map(CompletableFuture::join)
                .collect(Collectors.toList());


        List<AnalysisResp> analysisData = collect.get(0);
        List<AnalysisResp> qoqAnalysisData = collect.get(1);
        List<AnalysisResp> yoyAnalysisData = collect.get(2);
        analysisData.forEach(o -> {
            EhrUserDetailResp ehrUserDetailResp = userMap.get(o.getUid() + "@ziroom.com");
            o.setLevel(ehrUserDetailResp.getLevelName());
        });

        /**
         * 核心数据指标
         */
        List<TeamOverviewResp> overviewRespList = new ArrayList<>(16);

        long devEquivalentS = analysisData.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long qoqDevEquivalentS = qoqAnalysisData.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        long yoyDevEquivalentS = yoyAnalysisData.stream().mapToLong(AnalysisResp::getDevEquivalent).sum();
        double qoqDevEquivalentRate = Math.abs((devEquivalentS - qoqDevEquivalentS) * 1.0 / qoqDevEquivalentS) * 100;
        double yoyDevEquivalentRate = Math.abs((devEquivalentS - yoyDevEquivalentS) * 1.0 / yoyDevEquivalentS) * 100;

        long insertionS = analysisData.stream().mapToLong(AnalysisResp::getInsertions).sum();
        long qoqInsertionsS = qoqAnalysisData.stream().mapToLong(AnalysisResp::getInsertions).sum();
        long yoyInsertionsS = yoyAnalysisData.stream().mapToLong(AnalysisResp::getInsertions).sum();
        double qoqInsertionsRate = Math.abs((insertionS - qoqInsertionsS) * 1.0 / qoqInsertionsS) * 100;
        double yoyInsertionsRate = Math.abs((insertionS - yoyInsertionsS) * 1.0 / yoyInsertionsS) * 100;

        long commitS = analysisData.stream().mapToLong(AnalysisResp::getCommitCount).sum();
        long qoqCommitS = qoqAnalysisData.stream().mapToLong(AnalysisResp::getCommitCount).sum();
        long yoyCommitS = yoyAnalysisData.stream().mapToLong(AnalysisResp::getCommitCount).sum();
        double qoqCommitsRate = Math.abs((commitS - qoqCommitS) * 1.0 / qoqCommitS) * 100;
        double yoyCommitsRate = Math.abs((commitS - yoyCommitS) * 1.0 / yoyCommitS) * 100;
        long fixBugS = analysisData.stream().mapToLong(AnalysisResp::getFixBugCount).sum();
        List<Metric> qoqProductionMetric = new ArrayList<>(16);
        List<Metric> yoyProductionMetric = new ArrayList<>(16);
        qoqProductionMetric.add(Metric.builder()
                .name("开发当量")
                .value(Long.toString(devEquivalentS))
                .oldValue(Long.toString(qoqDevEquivalentS))
                .rate(String.format("%.2f", qoqDevEquivalentRate))
                .tendency(devEquivalentS - qoqDevEquivalentS > 0 ? 1 : 2).build());
        yoyProductionMetric.add(Metric.builder()
                .name("开发当量")
                .value(Long.toString(devEquivalentS))
                .oldValue(Long.toString(yoyDevEquivalentS))
                .rate(String.format("%.2f", yoyDevEquivalentRate))
                .tendency(devEquivalentS - yoyDevEquivalentS > 0 ? 1 : 2).build());
        qoqProductionMetric.add(Metric.builder().name("人均当量").value("-").oldValue("-").rate("0").tendency(1).build());
        qoqProductionMetric.add(Metric.builder()
                .name("代码行数")
                .value(Long.toString(insertionS))
                .oldValue(Long.toString(qoqInsertionsS))
                .rate(String.format("%.2f", qoqInsertionsRate))
                .tendency(insertionS - qoqInsertionsS > 0 ? 1 : 2).build());
        yoyProductionMetric.add(Metric.builder()
                .name("代码行数")
                .value(Long.toString(insertionS))
                .oldValue(Long.toString(yoyInsertionsS))
                .rate(String.format("%.2f", yoyInsertionsRate))
                .tendency(insertionS - yoyInsertionsS > 0 ? 1 : 2).build());

        qoqProductionMetric.add(Metric.builder()
                .name("代码提交次数")
                .value(Long.toString(commitS))
                .oldValue(Long.toString(qoqCommitS))
                .rate(String.format("%.2f", qoqCommitsRate))
                .tendency(commitS - qoqCommitS > 0 ? 1 : 2).build());
        yoyProductionMetric.add(Metric.builder()
                .name("代码提交次数")
                .value(Long.toString(commitS))
                .oldValue(Long.toString(yoyCommitS))
                .rate(String.format("%.2f", yoyCommitsRate))
                .tendency(commitS - yoyCommitS > 0 ? 1 : 2).build());
//        productionMetric.add(Metric.builder().name("项目数").value("-").oldValue("-").rate("23").tendency(0).build());
        qoqProductionMetric.add(Metric.builder().name("修复bug数").value(Long.toString(fixBugS)).oldValue("-").rate("0").tendency(0).build());
        yoyProductionMetric.add(Metric.builder().name("修复bug数").value(Long.toString(fixBugS)).oldValue("-").rate("0").tendency(0).build());
//        productionMetric.add(Metric.builder().name("开发价值").value("-").oldValue("-").rate("1.4").tendency(0).build());
//        productionMetric.add(Metric.builder().name("价值密度").value("2333").oldValue("23333").rate("110").tendency(1).build());
        TeamOverviewResp production = TeamOverviewResp.builder()
                .id(1)
                .name("产出类")
                .qoqMetricList(qoqProductionMetric)
                .yoyMetricList(yoyProductionMetric)
                .style("margin-top: 15px")
                .build();

        List<Metric> qoqEfficiencyMetric = new ArrayList<>();
        List<Metric> yoyEfficiencyMetric = new ArrayList<>();
        qoqEfficiencyMetric.add(Metric.builder().name("项目平均开发周期").value("189h").oldValue("214h").rate("8").tendency(2).build());
        yoyEfficiencyMetric.add(Metric.builder().name("项目平均开发周期").value("189h").oldValue("214h").rate("8").tendency(2).build());
        qoqEfficiencyMetric.add(Metric.builder().name("功能平均开发周期").value("-").oldValue("-").rate("0").tendency(0).build());
        yoyEfficiencyMetric.add(Metric.builder().name("功能平均开发周期").value("-").oldValue("-").rate("0").tendency(0).build());
        qoqEfficiencyMetric.add(Metric.builder().name("bug平均修复时间").value("3.5h").oldValue("0.8h").rate("43").tendency(2).build());
        yoyEfficiencyMetric.add(Metric.builder().name("bug平均修复时间").value("1h").oldValue("0.8h").rate("67").tendency(2).build());
        qoqEfficiencyMetric.add(Metric.builder().name("bug平均修复时间").value("1.9h").oldValue("0.8h").rate("34").tendency(2).build());
        yoyEfficiencyMetric.add(Metric.builder().name("bug平均修复时间").value("0.2h").oldValue("0.8h").rate("1").tendency(2).build());

        TeamOverviewResp efficiency = TeamOverviewResp.builder()
                .id(2)
                .name("效率类")
                .style("border-right: dashed #A9A9A9; border-left: dashed #A9A9A9; margin-top: 15px")
                .qoqMetricList(qoqEfficiencyMetric)
                .yoyMetricList(yoyEfficiencyMetric)
                .build();

        long publishS = analysisData.stream().mapToLong(AnalysisResp::getPublishNum).sum();
        long compileS = analysisData.stream().mapToLong(AnalysisResp::getCompileNum).sum();
        long onlineS = analysisData.stream().mapToLong(AnalysisResp::getOnlineNum).sum();
        long rollbackS = analysisData.stream().mapToLong(AnalysisResp::getRollbackNum).sum();
        long restartS = analysisData.stream().mapToLong(AnalysisResp::getRestartNum).sum();
        List<Metric> qoqPublicationMetric = new ArrayList<>();
        List<Metric> yoyPublicationMetric = new ArrayList<>();
        qoqPublicationMetric.add(Metric.builder().name("发布次数").value(String.valueOf(publishS)).oldValue("233").rate("0").tendency(1).build());
        qoqPublicationMetric.add(Metric.builder().name("编译次数").value(String.valueOf(compileS)).oldValue("233").rate("0").tendency(1).build());
        qoqPublicationMetric.add(Metric.builder().name("上线次数").value(String.valueOf(onlineS)).oldValue("233").rate("0").tendency(2).build());
        qoqPublicationMetric.add(Metric.builder().name("重启次数").value(String.valueOf(rollbackS)).oldValue("233").rate("0").tendency(1).build());
        qoqPublicationMetric.add(Metric.builder().name("回滚次数").value(String.valueOf(restartS)).oldValue("233").rate("0").tendency(2).build());

        yoyPublicationMetric.add(Metric.builder().name("发布次数").value(String.valueOf(publishS)).oldValue("233").rate("0").tendency(1).build());
        yoyPublicationMetric.add(Metric.builder().name("编译次数").value(String.valueOf(compileS)).oldValue("233").rate("0").tendency(1).build());
        yoyPublicationMetric.add(Metric.builder().name("上线次数").value(String.valueOf(onlineS)).oldValue("233").rate("0").tendency(2).build());
        yoyPublicationMetric.add(Metric.builder().name("重启次数").value(String.valueOf(rollbackS)).oldValue("233").rate("0").tendency(1).build());
        yoyPublicationMetric.add(Metric.builder().name("回滚次数").value(String.valueOf(restartS)).oldValue("233").rate("0").tendency(2).build());
        TeamOverviewResp publication = TeamOverviewResp.builder()
                .id(3)
                .name("发布类")
                .qoqMetricList(qoqPublicationMetric)
                .yoyMetricList(yoyPublicationMetric)
                .style("margin-top: 15px")
                .build();

        List<Metric> qoqQualityMetric = new ArrayList<>();
        List<Metric> yoyQualityMetric = new ArrayList<>();
        qoqQualityMetric.add(Metric.builder().name("注释覆盖度").value("100%").oldValue("90%").rate("10").tendency(1).build());
        qoqQualityMetric.add(Metric.builder().name("测试覆盖度").value("0.01%").oldValue("0.01%").rate("10").tendency(2).build());
        qoqQualityMetric.add(Metric.builder().name("代码模块性").value("0.01%").oldValue("0.01%").rate("10").tendency(1).build());
        qoqQualityMetric.add(Metric.builder().name("千行bug率").value("0.01%").oldValue("0.017%").rate("4").tendency(2).build());

        yoyQualityMetric.add(Metric.builder().name("注释覆盖度").value("100%").oldValue("90%").rate("10").tendency(1).build());
        yoyQualityMetric.add(Metric.builder().name("测试覆盖度").value("0.01%").oldValue("0.01%").rate("10").tendency(2).build());
        yoyQualityMetric.add(Metric.builder().name("代码模块性").value("0.01%").oldValue("0.01%").rate("10").tendency(1).build());
        yoyQualityMetric.add(Metric.builder().name("千行bug率").value("0.01%").oldValue("0.017%").rate("4").tendency(2).build());

        TeamOverviewResp quality = TeamOverviewResp.builder()
                .id(4)
                .name("质量类")
                .qoqMetricList(qoqQualityMetric)
                .yoyMetricList(yoyQualityMetric)
                .style("margin-top: 15px")
                .build();

        List<Metric> costMetric = new ArrayList<>();
        costMetric.add(Metric.builder().name("总人数").value("2333").oldValue("2333").rate("0").tendency(2).build());
        costMetric.add(Metric.builder().name("研发人数").value("2333").oldValue("2333").rate("0").tendency(2).build());
        costMetric.add(Metric.builder().name("开发人员占比").value("2333").oldValue("2333").rate("33").tendency(1).build());
        costMetric.add(Metric.builder().name("休假天数").value("2333").oldValue("2333").rate("0").tendency(0).build());
        costMetric.add(Metric.builder().name("出勤工时").value("4312h").oldValue("4117h").rate("2").tendency(1).build());
        costMetric.add(Metric.builder().name("开发工时").value("-").oldValue("-").rate("0").tendency(0).build());
        costMetric.add(Metric.builder().name("工时饱和度").value("2333").oldValue("2333").rate("23").tendency(1).build());
        costMetric.add(Metric.builder().name("人均休假天数").value("2333").oldValue("2333").rate("33").tendency(2).build());
        TeamOverviewResp cost = TeamOverviewResp.builder()
                .id(5)
                .name("成本类")
                .qoqMetricList(costMetric)
                .style("border-right: dashed #A9A9A9; border-left: dashed #A9A9A9; margin-top: 15px")
                .build();

        List<Metric> slaMetric = new ArrayList<>();
        slaMetric.add(Metric.builder().name("团队鲁棒性").value("99.99%").oldValue("99.98%").rate("1").tendency(1).build());
        slaMetric.add(Metric.builder().name("团队稳定性").value("99.99%").oldValue("99.99%").rate("2").tendency(1).build());
        slaMetric.add(Metric.builder().name("团队稳定性").value("99.99%").oldValue("99.99%").rate("2").tendency(2).build());
        slaMetric.add(Metric.builder().name("团队稳定性").value("99.99%").oldValue("99.99%").rate("1").tendency(1).build());
        TeamOverviewResp sla = TeamOverviewResp.builder()
                .id(6)
                .name("稳定性类")
                .qoqMetricList(slaMetric)
                .style("margin-top: 15px")
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
}
