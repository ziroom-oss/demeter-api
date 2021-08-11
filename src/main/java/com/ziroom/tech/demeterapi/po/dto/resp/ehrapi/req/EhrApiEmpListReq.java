package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req;

import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * ehr-api接口
 *
 * @author huangqiaowei
 * @date 2020-08-24 09:41
 **/
@Data
public class EhrApiEmpListReq {

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 统一职务编码
     */
    private String contrastJobCode;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 职务编码
     */
    private String jobCode;

    /**
     * 职务名称
     */
    private String jobName;

    /**
     * 页码
     */
    private Integer pageIndex = NumberUtils.INTEGER_ONE;

    /**
     * 每页条数
     */
    private Integer pageSize = 100;

    /**
     * 职务序列
     */
    private String seriesCode;
}
