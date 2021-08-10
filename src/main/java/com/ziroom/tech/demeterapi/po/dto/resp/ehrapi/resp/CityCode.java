package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import com.ziroom.tech.demeterapi.common.enums.CityCodeEnum;
import lombok.Data;

/**
 * 城市编码
 *
 * @author huangqiaowei
 * @date 2020-09-14 15:24
 **/
@Data
public class CityCode {

    private String code;

    private String name;

    public static CityCode copyCityCodeEnum(CityCodeEnum cityCodeEnum) {
        CityCode cityCodeResp = new CityCode();
        cityCodeResp.setCode(cityCodeEnum.getCode());
        cityCodeResp.setName(cityCodeEnum.getName());
        return cityCodeResp;
    }
}
