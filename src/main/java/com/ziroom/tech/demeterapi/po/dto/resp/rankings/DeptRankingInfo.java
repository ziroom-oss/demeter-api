package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@Data
public class DeptRankingInfo implements Serializable {

    private Integer order;
    @JsonProperty("departmentName")
    private String deptName;
    @JsonProperty("count")
    private Long value;
    private String deptCode;

}
