package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class AuthHistoryResp {

    private Long id;

    private Long userTaskId;

    private String authUser;

    private String authUserName;

    private Integer authResult;

    private String authResultName;

    private String authOpinion;

    private String createId;

    private String modifyId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private Date modifyTime;
}
