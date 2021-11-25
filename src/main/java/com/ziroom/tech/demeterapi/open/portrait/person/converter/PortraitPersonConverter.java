package com.ziroom.tech.demeterapi.open.portrait.person.converter;

import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitDevlopReportDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonGrowingupDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonProjectDto;
import com.ziroom.tech.demeterapi.open.portrait.person.vo.PortraitDevlopRespVO;
import com.ziroom.tech.demeterapi.open.portrait.person.vo.PortraitPersonGrowingupRespVO;
import com.ziroom.tech.demeterapi.open.portrait.person.vo.PortraitPersonProjectRespVO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 个人画像 model 转换器
 * @author xuzeyu
 */
public class PortraitPersonConverter {

    /**
     * 个人成长指标model转换
     * @return
     */
    public static Function<PortraitPersonGrowingupDto, PortraitPersonGrowingupRespVO> PortraitPersonGrowingupConverter() {
        return portraitPersonGrowingupDto -> {
            PortraitPersonGrowingupRespVO portraitPersonGrowingupRespVO = new PortraitPersonGrowingupRespVO();
            portraitPersonGrowingupRespVO.setTitle(portraitPersonGrowingupDto.getTitle());
            portraitPersonGrowingupRespVO.setType(portraitPersonGrowingupDto.getType());
            List<PortraitPersonGrowingupDto.Point> points = portraitPersonGrowingupDto.getPoints();
            if(CollectionUtils.isNotEmpty(points)){
                List<PortraitPersonGrowingupRespVO.Point> pointList = points.stream().map(m -> {
                    PortraitPersonGrowingupRespVO.Point point = new PortraitPersonGrowingupRespVO.Point();
                    point.setCoreName(m.getCoreName());
                    point.setCoreData(m.getCoreData());
                    return point;
                }).collect(Collectors.toList());
                portraitPersonGrowingupRespVO.setPoints(pointList);
            }
            return portraitPersonGrowingupRespVO;
        };
    }

    /**
     * 个人开发表现指标model转换
     * @return
     */
    public static Function<PortraitDevlopReportDto, PortraitDevlopRespVO> PortraitPersonDevlopConverter() {
        return portraitPersonDevlopReportDto -> {
            PortraitDevlopRespVO portraitDevlopRespVO = new PortraitDevlopRespVO();
            portraitDevlopRespVO.setDevEquivalent(portraitPersonDevlopReportDto.getDevEquivalent());
            portraitDevlopRespVO.setInsertions(portraitPersonDevlopReportDto.getInsertions());
            portraitDevlopRespVO.setDeletions(portraitPersonDevlopReportDto.getDeletions());
            portraitDevlopRespVO.setCommitCount(portraitPersonDevlopReportDto.getCommitCount());
            portraitDevlopRespVO.setProjectNum(portraitPersonDevlopReportDto.getProjectNum());
            portraitDevlopRespVO.setDemandNum(portraitPersonDevlopReportDto.getDemandNum());
            portraitDevlopRespVO.setBugNum(portraitPersonDevlopReportDto.getBugNum());
            portraitDevlopRespVO.setDevValue(portraitPersonDevlopReportDto.getDevValue());
            portraitDevlopRespVO.setProjectAveDevPeriod(portraitPersonDevlopReportDto.getProjectAveDevPeriod());
            portraitDevlopRespVO.setFunctionAveDevPeriod(portraitPersonDevlopReportDto.getFunctionAveDevPeriod());
            portraitDevlopRespVO.setBugAveFixTime(portraitPersonDevlopReportDto.getBugAveFixTime());
            portraitDevlopRespVO.setPublishNum(portraitPersonDevlopReportDto.getPublishNum());
            portraitDevlopRespVO.setCompileNum(portraitPersonDevlopReportDto.getCompileNum());
            portraitDevlopRespVO.setOnlineNum(portraitPersonDevlopReportDto.getOnlineNum());
            portraitDevlopRespVO.setRestartNum(portraitPersonDevlopReportDto.getRestartNum());
            portraitDevlopRespVO.setRollbackNum(portraitPersonDevlopReportDto.getRollbackNum());
            portraitDevlopRespVO.setDocCoverage(portraitPersonDevlopReportDto.getDocCoverage());
            portraitDevlopRespVO.setStaticTestCoverage(portraitPersonDevlopReportDto.getStaticTestCoverage());
            portraitDevlopRespVO.setFunImpact(portraitPersonDevlopReportDto.getFunImpact());
            portraitDevlopRespVO.setBugProbability(portraitPersonDevlopReportDto.getBugProbability());
            portraitDevlopRespVO.setWorkHours(portraitPersonDevlopReportDto.getWorkHours());
            portraitDevlopRespVO.setDevlopHours(portraitPersonDevlopReportDto.getDevlopHours());
            portraitDevlopRespVO.setWorkSaturability(portraitPersonDevlopReportDto.getWorkSaturability());
            portraitDevlopRespVO.setVacationDays(portraitPersonDevlopReportDto.getVacationDays());
            return portraitDevlopRespVO;
        };
    }


    /**
     * 个人项目指标数据展现
     * @return
     */
    public static Function<PortraitPersonProjectDto, PortraitPersonProjectRespVO> PortraitPersonProjectConverter() {
        return portraitPersonProjectDto -> {
            PortraitPersonProjectRespVO portraitPersonProjectRespVO = new PortraitPersonProjectRespVO();
            portraitPersonProjectRespVO.setName(portraitPersonProjectDto.getProjectName());
            portraitPersonProjectRespVO.setValue(portraitPersonProjectDto.getDevEquivalent());
            return portraitPersonProjectRespVO;
        };
    }
}
