package com.ziroom.tech.demeterapi.open.portrait.person.service;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitDevlopReportDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonGrowingupDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonProjectDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonSkillDto;
import com.ziroom.tech.demeterapi.open.portrait.person.param.PortraitPersonReqParam;
import java.util.List;

/**
 * 个人画像
 * @author xuzeyu
 */
public interface PortraitPersonService {

    ModelResult<List<PortraitPersonGrowingupDto>> getUserGrowingupInfo(String uid);

    ModelResult<PortraitDevlopReportDto> getPortraitPersonDevModel(PortraitPersonReqParam personReqParam);

    ModelResult<PortraitDevlopReportDto> getTeamDevlopPortraitData(PortraitPersonReqParam personReqParam);

    ModelResult<List<PortraitPersonProjectDto>> getProjectPortraitData(PortraitPersonReqParam personReqParam);

    ModelResult<PortraitPersonSkillDto> getPortraitPersonSkillInfo(String uid);
}
