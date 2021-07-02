package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CtoResp {

    /**
     * 部门占比
     */
    List<Struct> deptInfo;

    /**
     * 职务占比
     */
    List<Struct> jobInfo;

    /**
     * 职级占比
     */
    List<Struct> levelInfo;

    /**
     * 入职年限占比
     */
    List<Struct> hireDateInfo;
}
