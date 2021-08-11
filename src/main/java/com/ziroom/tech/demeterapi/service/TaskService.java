package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.*;
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

    Resp<AssignDetailResp> getAssignTask(Long id);

    /**
     * 编辑指派类任务
     * @param assignTaskReq 指派类任务请求体
     * @return Resp
     */
    Resp<Object> updateAssignTask(AssignTaskReq assignTaskReq);

    /**
     * @param taskId id
     * @param taskType type
     * @return
     */
    Resp<Object> updateAssignTaskStatus(Long taskId, Integer taskType, Integer taskStatus);

    /**
     * 发布任务列表
     * @param taskListQueryReq 任务列表查询请求体
     * @return Resp<List<ReleaseQueryResp>>
     */
    PageListResp<ReleaseQueryResp> getReleaseList(TaskListQueryReq taskListQueryReq);

    /**
     * 接收任务列表
     * @param taskListQueryReq 任务列表查询请求体
     * @return Resp
     */
    PageListResp<ReceiveQueryResp> getExecuteList(TaskListQueryReq taskListQueryReq);

    /**
     *
     * @param req
     * @return {@link Resp}
     * @throws
     *
     * @author lipp3
     * @date 2021/6/30 11:06
     *
     * @Description  分配技能学习任务
     */
    Resp createSkillLearnManifest(CreateSkillLearnManifestReq req);

    /**
     * 修改学习清单
     * @param req
     * @return
     */
    Integer modifySkillLearnManifest(ModifySkillLearnManifestReq req);

    /**
     * 为学习清单中的单条技能点添加单条学习路径
     */
    Integer createLearnPathIntoSkill(Long taskUserId, Long taskId, String path);

    /**
     * 为学习清单添加单条技能点
     */
    Integer createSkillTaskIntoManifest(Long taskId, Long manifestId, Long taskUserId);

    /**
     * 为学习清单添加单条技能点前先添加到 task_user
     */
    DemeterTaskUser createTaskUser(Long taskId, String learnerUid);

    /**
     * 移除学习清单中的技能任务
     */
    Integer deleteSkillLearnManifestSkill(Long manifestId, Long taskId);

    /**
     * 获取学习清单详情
     * @param req
     * @return Resp
     */
    PageListResp<SkillLearnManifestResp> getSkillLearnManifest(GetSkillLearnManifestReq req);

    /**
     *
     * @param manifestId 清单id
     * @return {@link Resp}
     * @throws
     *
     * @author
     * @date 2021/7/1 9:24
     *
     * @Description  
     */
    SkillLearnManifestDetailResp getSkillLearnManifestDetail(Long manifestId);


    /**
     *
     * @param manifestId
     * @return {@link Resp<ManifestSkillLearnGrapeResp>}
     * @throws
     *
     * @author
     * @date 2021/7/1 11:10
     *
     * @Description  根据清单id获取技能学习图谱
     */
    ManifestSkillLearnGrapeResp getManifestSkillGrape(Long manifestId);


    /**
     *
     * @param skillId
     * @return {@link SkillHierarchyResp}
     * @throws
     *
     * @author
     * @date 2021/7/1 10:49
     *
     * @Description  获取技能点的层级结构
     */
    SkillHierarchyResp getSkillHierarchy(Long skillId);

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
     * 指派类任务直接完成
     * @param taskId id
     * @return Resp
     */
    Resp<Object> submitComplete(Long taskId);

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
    DemeterTaskUser getRejectReason(RejectTaskReasonReq rejectTaskReasonReq);

    /**
     * 任务查看进度
     * @param id
     * @return
     */
    Resp<TaskProgressResp> getTaskProgress(Long id);

    /**
     * 查看任务详情
     * @param taskId id
     * @param taskType type
     * @return
     */
    @Deprecated
    Resp<Object> getTaskDetails(Long taskId, Integer taskType);


    /**
     * 查看任务详情，所有用户及所有类型任务均可使用
     * @param taskId id
     * @param taskType type
     * @return TaskDetailsResp
     */
    Resp<TaskDetailResp> getAllDetails(Long taskId, Integer taskType);

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
     * @param uploadOutcomeReq 请求体
     * @return Resp<Object>
     */
    Resp<Object> uploadLearningOutcome(UploadOutcomeReq uploadOutcomeReq);

    /**
     * 检查指派任务是否延期
     * @return 是 否
     */
    Boolean checkTaskDelay();

    /**
     * 技能任务搜索
     * @param condition
     * @return
     */
    List<DemeterSkillTask> searchSkillForGraph(String condition);

    /**
     * 移动技能点
     * @param id 技能点id
     * @param skillTreeId 技能树id
     * @return
     */
    boolean submitSkillMove(Long id, Integer skillTreeId);

    /**
     * 重新指派任务
     * @param reassignTaskReq 请求体
     * @return boolean
     */
    boolean reassignTask(ReassignTaskReq reassignTaskReq);
}
