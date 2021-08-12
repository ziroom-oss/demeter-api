package com.ziroom.tech.demeterapi.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemeterTaskUser {

    private Long id;

    private Long taskId;

    private Integer taskType;

    private String receiverUid;

    private Integer taskStatus;

    private String rejectReason;

    private Integer checkResult;

    private String checkOpinion;

    private Date checkoutTime;

    private Date taskEndTime;

    private Integer parentId;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}