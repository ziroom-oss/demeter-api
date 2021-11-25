package com.ziroom.tech.demeterapi.open.portrait.person.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.DateUtil;
import com.ziroom.tech.demeterapi.open.portrait.common.dao.PortraitMapper;
import com.ziroom.tech.demeterapi.open.portrait.person.dao.PortraitPersonGrowingupMapper;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitDevlopReportDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonGrowingupDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonProjectDto;
import com.ziroom.tech.demeterapi.open.portrait.person.entity.PortraitPersonGrowingupEntity;
import com.ziroom.tech.demeterapi.open.portrait.person.param.PortraitPersonReqParam;
import com.ziroom.tech.demeterapi.open.portrait.person.service.PortraitPersonService;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xuzeyu
 */
@Service
public class PortraitPersonServiceImpl implements PortraitPersonService {

    @Autowired
    private PortraitMapper portraitMapper;

    @Autowired
    private PortraitPersonGrowingupMapper portraitPersonGrowingupMapper;


    /**
     * 获取个人成长信息展现
     * 获取当前月个人最新成长指标 注：因此模块信息实时性变动不强, 故成长指标每个月定时同步维护 参见定时任务
     */
    public ModelResult<List<PortraitPersonGrowingupDto>> getUserGrowingupInfo(String uid) {
        //获取当前时间
        String nowDay = DateUtil.getNowDateTimeStrToMonth();
        List<PortraitPersonGrowingupEntity> userGrowingupInfoList = portraitPersonGrowingupMapper.getUserGrowingupInfo(uid, nowDay);
        if(CollectionUtils.isEmpty(userGrowingupInfoList)){
            return ModelResultUtil.error(ResponseEnum.RESPONSE_DATA_EMPTY.getCode(), ResponseEnum.RESPONSE_DATA_EMPTY.getMessage());
        }
        Map<String, List<PortraitPersonGrowingupEntity>> growingupMap = userGrowingupInfoList.stream().collect(Collectors.groupingBy(PortraitPersonGrowingupEntity::getTitle));
        List<PortraitPersonGrowingupDto> portraitPersonGrowingupDtos = Lists.newArrayList();
        growingupMap.forEach((k,v)->{
            PortraitPersonGrowingupDto portraitPersonGrowingupDto = new PortraitPersonGrowingupDto();
            portraitPersonGrowingupDto.setTitle(k);
            portraitPersonGrowingupDto.setType(v.get(0).getType());
            List<PortraitPersonGrowingupDto.Point> pointList = v.stream().map(m -> {
                PortraitPersonGrowingupDto.Point point = new PortraitPersonGrowingupDto.Point();
                point.setCoreName(m.getCoreName());
                point.setCoreData(m.getCoreData());
                return point;
            }).collect(Collectors.toList());
            portraitPersonGrowingupDto.setPoints(pointList);
            portraitPersonGrowingupDtos.add(portraitPersonGrowingupDto);
        });
        return ModelResultUtil.success(portraitPersonGrowingupDtos);
    }

    /**
     * 获取个人开发指标表现
     * @return
     */
    public ModelResult<PortraitDevlopReportDto> getPortraitPersonDevModel(PortraitPersonReqParam personReqParam){
        /**
         * 获取个人开发指标
         * 产出类指标, 效率类指标 发布类指标, 成本类指标 可从 ca_person_devlop_report 快照表获取
         * 质量类指标 如注释覆盖度 测试覆盖度 可从 ca_commit_report 快照表计算获取
         * 开发价值 代码影响力 实时变动性高 提取方式参照readme 因快照表记录的开发价值 代码影响力 是项目维度的 此处获取的是开发者维度, 可根据多个项目的开发价值等指标 按权重分配
         */
        return ModelResultUtil.success(new PortraitDevlopReportDto());
    }

    /**
     * 指标数据团队平均值展现
     */
    public ModelResult<PortraitDevlopReportDto> getTeamDevlopPortraitData(PortraitPersonReqParam personReqParam) {
        /**
         * 获取团队开发指标平均值
         * 产出类指标, 效率类指标 发布类指标, 成本类指标 可从 ca_person_devlop_report 快照表获取
         * 质量类指标 如注释覆盖度 测试覆盖度 可从 ca_commit_report 快照表计算获取
         * 开发价值 代码影响力 实时变动性高 提取方式参照readme
         */
        return null;
    }

    /**
     * 个人项目指标数据展现
     */
    public ModelResult<List<PortraitPersonProjectDto>> getProjectPortraitData(PortraitPersonReqParam personReqParam){
        /**
         * 统计开发者在各个项目的表现
         * 可从ca_commit_report快照表获取
         */
        List<PortraitPersonProjectDto> portraitPersonProjectDtos = Lists.newArrayList();
        return ModelResultUtil.success(portraitPersonProjectDtos);
    }
}
