package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EhrDeptResp implements Serializable {

    /**
     * 部门编码
     */
    private String code;

    /**
     * 部门名称
     */
    private String name;


}
