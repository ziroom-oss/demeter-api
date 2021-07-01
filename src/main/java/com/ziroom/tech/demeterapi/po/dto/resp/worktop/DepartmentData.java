package com.ziroom.tech.demeterapi.po.dto.resp.worktop;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class DepartmentData {

    private List<KVResp> problems;

    private List<KVResp> workTime;
}
