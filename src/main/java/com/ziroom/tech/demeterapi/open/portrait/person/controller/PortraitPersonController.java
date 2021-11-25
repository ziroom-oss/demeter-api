package com.ziroom.tech.demeterapi.open.portrait.person.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.common.controller.BaseController;
import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.portrait.person.converter.PortraitPersonConverter;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitDevlopReportDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonGrowingupDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonProjectDto;
import com.ziroom.tech.demeterapi.open.portrait.person.param.PortraitPersonReqParam;
import com.ziroom.tech.demeterapi.open.portrait.person.service.PortraitPersonService;
import com.ziroom.tech.demeterapi.open.portrait.person.vo.PortraitDevlopRespVO;
import com.ziroom.tech.demeterapi.open.portrait.person.vo.PortraitPersonGrowingupRespVO;
import com.ziroom.tech.demeterapi.open.portrait.person.vo.PortraitPersonProjectRespVO;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工画像
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping("api/portrait/person")
public class PortraitPersonController extends BaseController {

    @Resource(name = "test")
    private PortraitPersonService portraitPersonService;

    /**
     * 个人成长指标信息展现(默认获取当前月最新指标展示,可自行修改)
     */
    @RequestMapping(value = "/growingup", method = RequestMethod.GET)
    public ModelResponse<List<PortraitPersonGrowingupRespVO>> getUserGrowingupInfo(@RequestParam String uid) {
        ModelResult<List<PortraitPersonGrowingupDto>> userGrowingupInfoModelResult = portraitPersonService.getUserGrowingupInfo(uid);
        if(!ModelResultUtil.isSuccess(userGrowingupInfoModelResult)){
            log.warn("[PortraitPersonController] portraitPersonService.getUserGrowingupInfo result is {}", JSON.toJSONString(userGrowingupInfoModelResult));
            return ModelResponseUtil.error(userGrowingupInfoModelResult.getResultCode(), userGrowingupInfoModelResult.getResultMessage());
        }
        List<PortraitPersonGrowingupDto> userGrowingupInfoList = userGrowingupInfoModelResult.getResult();
        List<PortraitPersonGrowingupRespVO> personGrowingupRespVOS = userGrowingupInfoList.stream().map(PortraitPersonConverter.PortraitPersonGrowingupConverter()).collect(Collectors.toList());
        return ModelResponseUtil.ok(personGrowingupRespVOS);
    }

    /**
     * 个人指标数据展现
     */
    @RequestMapping(value = "/devlop", method = RequestMethod.POST)
    public ModelResponse<PortraitDevlopRespVO> getDevlopPortraitData(@RequestBody PortraitPersonReqParam personReqParam) {
        ModelResult<PortraitDevlopReportDto> portraitPersonDevModelModelResult = portraitPersonService.getPortraitPersonDevModel(personReqParam);
        if(!ModelResultUtil.isSuccess(portraitPersonDevModelModelResult)){
            log.warn("[PortraitPersonController] portraitPersonService.getPortraitPersonDevModel result is {}", JSON.toJSONString(portraitPersonDevModelModelResult));
            return ModelResponseUtil.error(portraitPersonDevModelModelResult.getResultCode(), portraitPersonDevModelModelResult.getResultMessage());
        }
        PortraitDevlopReportDto portraitPersonDevModel = portraitPersonDevModelModelResult.getResult();
        PortraitDevlopRespVO portraitDevlopRespVO = PortraitPersonConverter.PortraitPersonDevlopConverter().apply(portraitPersonDevModel);
        return ModelResponseUtil.ok(portraitDevlopRespVO);
    }

    /**
     * 个人项目指标数据展现
     */
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public ModelResponse<List<PortraitPersonProjectRespVO>> getProjectPortraitData(@RequestBody PortraitPersonReqParam personReqParam) {
        ModelResult<List<PortraitPersonProjectDto>> projectPortraitListModelResult = portraitPersonService.getProjectPortraitData(personReqParam);
        if(!ModelResultUtil.isSuccess(projectPortraitListModelResult)){
            log.warn("[PortraitPersonController] portraitPersonService.getProjectPortraitData result is {}", JSON.toJSONString(projectPortraitListModelResult));
            return ModelResponseUtil.error(projectPortraitListModelResult.getResultCode(), projectPortraitListModelResult.getResultMessage());
        }
        List<PortraitPersonProjectDto> projectPortraitList = projectPortraitListModelResult.getResult();
        List<PortraitPersonProjectRespVO> personProjectRespVOS = projectPortraitList.stream().map(PortraitPersonConverter.PortraitPersonProjectConverter()).collect(Collectors.toList());
        return ModelResponseUtil.ok(personProjectRespVOS);
    }

    /**
     * 指标数据团队平均值展现
     */
    @RequestMapping(value = "/devlopPortraitByTeam", method = RequestMethod.POST)
    public ModelResponse<PortraitDevlopRespVO> getTeamDevlopPortraitData(@RequestBody PortraitPersonReqParam personReqParam) {
        ModelResult<PortraitDevlopReportDto> teamDevlopPortraitModelResult = portraitPersonService.getTeamDevlopPortraitData(personReqParam);
        if(!ModelResultUtil.isSuccess(teamDevlopPortraitModelResult)){
            log.warn("[PortraitPersonController] portraitPersonService.getTeamDevlopPortraitData result is {}", JSON.toJSONString(teamDevlopPortraitModelResult));
            return ModelResponseUtil.error(teamDevlopPortraitModelResult.getResultCode(), teamDevlopPortraitModelResult.getResultMessage());
        }
        PortraitDevlopReportDto teamDevlopPortrait = teamDevlopPortraitModelResult.getResult();
        PortraitDevlopRespVO portraitDevlopRespVO = PortraitPersonConverter.PortraitPersonDevlopConverter().apply(teamDevlopPortrait);
        return ModelResponseUtil.ok(portraitDevlopRespVO);
    }

}
