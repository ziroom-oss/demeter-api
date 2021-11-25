package com.ziroom.tech.demeterapi.open.ehr.service;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrDepartmentInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import java.util.List;

/**
 * @author xuzeyu
 */
public interface OpenEhrService {

    ModelResult<EhrUserInfoDto> getUserInfo(String uid);

    ModelResult<List<EhrDepartmentInfoDto>> getDepartmentList();

    ModelResult<List<UserResp>> getUserDetail(List<String> userCodes);

}
