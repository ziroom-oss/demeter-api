package com.ziroom.tech.demeterapi.open.ehr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EhrDepartmentInfoDto {
    /**
     * 部门code
     */
    private String departmentCode;

    /**
     * 部门name
     */
    private String departmentName;

}
