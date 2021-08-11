package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import com.ziroom.tech.demeterapi.po.dto.resp.portrait.PersonalByDay;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class PersonResp {

    /**
     * 个人表现
     */
    private PersonOverview personOverview;

    private List<PersonalByDay> personalByDays;


}
