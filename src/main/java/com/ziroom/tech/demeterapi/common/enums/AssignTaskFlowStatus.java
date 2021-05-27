package com.ziroom.tech.demeterapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 指派类任务流状态
 * @author daijiankun
 */

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AssignTaskFlowStatus {

    /**
     * 全部
     */
    ALL(0, "全部"),

    /**
     * 待认领，指创建任务后接收者认领前的状态。
     */
    UNCLAIMED(1, "待认领"),

    /**
     * 已拒绝，指接收者拒绝任务的状态。
     */
    REJECTED(2, "已拒绝"),

    /**
     * 进行中，指接收任务后，处于任务周期内的状态。
     */
    ONGOING(3, "进行中"),

    /**
     * 已完成，指在任务到期前，所有任务完成条件的状态均被手动更新为已完成。指针对不需要验收的指派类任务，才有该状态。
     */
    FINISHED(4, "已完成"),

    /**
     * 指接收者发起验收后，任务发布者即将验收的状态。
     */
    WAIT_ACCEPTANCE(5, "待验收"),

    /**
     * 已延期，指在任务到期时，至少有一个任务完成条件的状态未被手动更新为已完成，若创建任务时勾选了需要验收，即使所有任务完成条件的状态都更新为已完成，但未上传学习成果，则任务也是未完成。
     */
    UNFINISHED(6, "已延期"),

    /**
     * 验收通过，指由任务发布者验收通过后的状态。
     */
    ACCEPTANCE(7, "验收通过"),

    /**
     * 验收未通过，指由任务发布者验收不通过后的状态。
     */
    FAILED(8, "验收未通过");

    /**
     * 类型码
     */
    private Integer code;
    /**
     * 类型描述
     */
    private String desc;

    private static Map<Integer, AssignTaskFlowStatus> map = Arrays.stream(AssignTaskFlowStatus.values())
            .collect(Collectors.toMap(AssignTaskFlowStatus::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static AssignTaskFlowStatus getByCode(Integer code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<AssignTaskFlowStatus> getAllTaskType() {
        return Arrays.stream(AssignTaskFlowStatus.values()).collect(Collectors.toList());
    }

    /**
     * 判断传入的状态码是不是合法的，当没有找到对应的code或者传入的code为null时，返回false
     * @param code 状态码
     * @return 是否合法 合法:true 不合法:false
     */
    public static boolean isValid(Integer code){
        if(Objects.isNull(code)) {
            return false;
        }
        for(AssignTaskFlowStatus taskType : AssignTaskFlowStatus.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
