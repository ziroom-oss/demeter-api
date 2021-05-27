package com.ziroom.tech.demeterapi.po.dto.req.task;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author daijiankun
 */
@Data
public class UploadOutcomeReq {

    MultipartFile multipartFile;

    Long taskId;

    Integer taskType;
}
