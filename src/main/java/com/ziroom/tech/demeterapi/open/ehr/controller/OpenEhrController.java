package com.ziroom.tech.demeterapi.open.ehr.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.ehr.converter.EhrConverter;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrDepartmentInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.service.OpenEhrService;
import com.ziroom.tech.demeterapi.open.ehr.vo.EhrDepartmentInfoVO;
import com.ziroom.tech.demeterapi.open.ehr.vo.EhrUserInfoVO;
import com.ziroom.tech.demeterapi.open.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.model.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.model.ModelResult;
import com.ziroom.tech.demeterapi.open.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.po.dto.resp.enums.TechRanksEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping("open/api/ehr")
public class OpenEhrController {

    @Resource(name = "testOpenEhrService")
    private OpenEhrService openEhrService;

    /**
     * 个人基本信息展现
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ModelResponse<EhrUserInfoVO> getUserInfo(@RequestParam String uid) {
        //获取用户信息
        ModelResult<EhrUserInfoDto> userInfoModelResult = openEhrService.getUserInfo(uid);
        if(!ModelResultUtil.isSuccess(userInfoModelResult)){
            log.warn("[PortraitPersonController] userService.getUserInfo result is {}", JSON.toJSONString(userInfoModelResult));
            return ModelResponseUtil.error(userInfoModelResult.getResultCode(), userInfoModelResult.getResultMessage());
        }
        EhrUserInfoDto userInfo = userInfoModelResult.getResult();
        EhrUserInfoVO personUserInfoVO = EhrConverter.EhrUserInfoConverter().apply(userInfo);
        return ModelResponseUtil.ok(personUserInfoVO);
    }

    /**
     * 获取部门列表
     */
    @RequestMapping(value = "/departmentList", method = RequestMethod.GET)
    public ModelResponse<List<EhrDepartmentInfoVO>> getDepartmentList() {
        //获取部门列表
        ModelResult<List<EhrDepartmentInfoDto>> modelResult = openEhrService.getDepartmentList();
        if(!ModelResultUtil.isSuccess(modelResult)){
            log.warn("[EhrController] openEhrService.getDepartmentList result is {}", JSON.toJSONString(modelResult));
            return ModelResponseUtil.error(modelResult.getResultCode(), modelResult.getResultMessage());
        }
        List<EhrDepartmentInfoDto> departmentInfoDtoList = modelResult.getResult();
        List<EhrDepartmentInfoVO> departmentInfoVOS = departmentInfoDtoList.stream().map(EhrConverter.EhrDepartmentInfoConverter()).collect(Collectors.toList());
        return ModelResponseUtil.ok(departmentInfoVOS);
    }

}
