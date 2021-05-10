package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.AssignDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiverListResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.TaskProgressResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author daijiankun
 */
public interface TaskService {

    /**
     * 创建指派类任务
     * @param assignTaskReq 指派类任务请求体
     * @return Resp
     */
    Resp<Object> createAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 创建技能类任务
     * @param skillTaskReq 技能类任务请求体
     * @return Resp
     */
    Resp<Object> createSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 编辑指派类任务
     * @param assignTaskReq 指派类任务请求体
     * @return Resp
     */
    Resp<Object> updateAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 编辑技能类任务
     * @param skillTaskReq  技能类任务请求体
     * @return Resp
     */
    Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 任务列表-我发布的
     * @param taskListQueryReq 任务列表查询请求体
     * @return Resp<List<ReleaseQueryResp>>
     */
    Resp<List<ReleaseQueryResp>> getReleaseList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务列表-我接收的
     * @param taskListQueryReq 任务列表查询请求体
     * @return Resp
     */
    Resp<Object> getExecuteList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务详情-指派类任务
     * @param id 任务id
     * @return Resp<AssignDetailResp>
     */
    @Deprecated
    Resp<AssignDetailResp> getAssignDetail(Long id);

    /**
     * 任务详情-技能类任务
     * @param id 任务id
     * @return Resp
     */
    @Deprecated
    Resp<Object> getSkillDetail(Long id);

    /**
     * 删除任务
     * @param id 任务id
     * @param taskType 任务类型
     * @return Resp
     */
    Resp<Object> delete(Long id, Integer taskType);

    /**
     * 认领任务
     * @param id 任务id
     * @param type 任务类型
     * @return Resp
     */
    Resp<Object> acceptTask(Long id, Integer type);

    /**
     * 验收/认证 任务
     * @param checkTaskReq 验收任务请求体
     * @return Resp
     */
    Resp<Object> checkTask(CheckTaskReq checkTaskReq);


    /**
     * 提交验收任务
     * @param taskId id
     * @param taskType type
     * @return Resp
     */
    Resp<Object> submitCheckTask(Long taskId, Integer taskType);

    /**
     * 拒绝指派类任务
     * @param rejectTaskReq 指派类任务-拒绝请求体
     * @return Resp
     */
    Resp<Object> rejectTask(RejectTaskReq rejectTaskReq);

    /**
     * 查看拒绝原因
     * @param rejectTaskReasonReq 指派类任务-查看拒绝原因请求体
     * @return Resp
     */
    Resp<Object> getRejectReason(RejectTaskReasonReq rejectTaskReasonReq);


    /**
     * 任务接收人清单
     * @param id 任务id
     * @return Resp
     */
    Resp<List<ReceiverListResp>> getTaskCheckList(Long id, Integer taskType);

    /**
     * 任务查看进度
     * @param id
     * @return
     */
    Resp<TaskProgressResp> getTaskProgress(Long id);

    /**
     * 查看任务详情，所有用户及所有类型任务均可使用
     * @param taskId id
     * @param taskType type
     * @return
     */
    Resp<Object> getTaskDetails(Long taskId, Integer taskType);

    /**
     * 任务条件完成
     * @param conditionInfoId conditionInfo Id
     * @return Resp
     */
    Resp<Object> finishTaskCondition(Long conditionInfoId);

    // TODO: 2021/5/6  test
    /**
     * 上传附件
     * @param file 文件
     * @return Resp
     */
    Resp<Object> uploadAttachment(MultipartFile file, Long taskId, Integer taskType);

    /**
     * 上传学习成果
     * @param multipartFile
     * @return
     */
    Resp<Object> uploadLearningOutcome(MultipartFile multipartFile, Long taskId, Integer taskType);

    // TODO: 2021/5/7 test
    /**
     * 查看文件
     * @param uuidString 文件的uuid
     * @return Resp
     */
    Resp<Object> readFile(String uuidString);


}
