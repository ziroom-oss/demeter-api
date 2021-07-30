package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class EmployeeTendency {

    private List<String> monthList;

    private List<EmployeeTendencyItem> employeeTendencyItemList;

}
