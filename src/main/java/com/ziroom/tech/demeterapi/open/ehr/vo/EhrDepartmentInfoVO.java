package com.ziroom.tech.demeterapi.open.ehr.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class EhrDepartmentInfoVO {
    /**
     * 部门code
     */
    private String departmentCode;

    /**
     * 部门name
     */
    private String departmentName;

}
