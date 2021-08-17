package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@Data
@ApiModel("排行榜数据")
public class DeptRankingResp implements Serializable {

    @ApiModelProperty("部门名称")
    @JsonProperty("departmentName")
    private String deptName;

    @ApiModelProperty("数量")
    @JsonProperty("count")
    private Long value;
//
//    @ApiModelProperty("部门编码")
//    private String deptCode;


    public DeptRankingResp(String deptName, Long value) {
        this.deptName = deptName;
        this.value = value;
    }

    public DeptRankingResp() {
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptRankingResp that = (DeptRankingResp) o;
        return Objects.equals(deptName, that.deptName) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptName, value);
    }
}
