package com.ziroom.tech.demeterapi.open.ehr.service;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;

/**
 * @author xuzeyu
 */
public interface OpenEhrService {

    ModelResult<EhrUserInfoDto> getUserInfo(String uid);

}
