package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import com.ziroom.tech.demeterapi.po.dto.resp.worktop.PersonalResp;
import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class EngineeringMetricResp {

    private PersonalDevResp personalDevResp;

    private PersonalProjectResp personalProjectResp;

    private PersonalDeployResp personalDeployResp;

    private List<PersonalByProject> personalByProject;

    private List<PersonalByDay> personalByDays;

    private PersonalResp personalResp;
}
