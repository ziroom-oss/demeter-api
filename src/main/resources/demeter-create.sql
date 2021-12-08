
-----------------------------------------------------------用户权限-----------------------------------------------------------------
CREATE TABLE `demeter_auth_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_code` int(11) NOT NULL COMMENT '用户code',
  `role` varchar(20) NOT NULL COMMENT '角色',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='用户权限管理';

-----------------------------------------------------------员工画像-----------------------------------------------------------------
CREATE TABLE `ca_commit_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commit_id` varchar(255) DEFAULT NULL COMMENT 'commit hash',
  `commit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `contributor` varchar(255) DEFAULT NULL COMMENT '作者',
  `contributor_email` varchar(255) DEFAULT NULL COMMENT '作者邮箱',
  `group_code` varchar(255) DEFAULT NULL COMMENT 'gitlab组id',
  `group_name` varchar(255) DEFAULT NULL COMMENT 'gitlab组名',
  `dev_equivalent` bigint(20) DEFAULT NULL COMMENT '开发当量',
  `insertions` bigint(20) DEFAULT NULL COMMENT '增加代码行数',
  `deletions` bigint(20) DEFAULT NULL COMMENT '删除代码行数',
  `project_code` varchar(64) DEFAULT NULL COMMENT '项目code',
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名字',
  `department_code` varchar(255) DEFAULT NULL COMMENT '部门code',
  `department_name` varchar(255) DEFAULT NULL COMMENT '部门名字',
  `devlop_value` varchar(255) DEFAULT NULL COMMENT '开发价值',
  `devlop_value_density` varchar(255) DEFAULT NULL COMMENT '价值密度',
  `skil_points` varchar(4000) DEFAULT NULL COMMENT '技能点',
  `has_doc` int(10) DEFAULT NULL COMMENT '注释函数数目',
  `has_test_coverage` int(10) DEFAULT NULL COMMENT '被测试覆盖函数数目',
  `fun_impact` int(10) DEFAULT NULL COMMENT '提交影响的函数数目',
  `commit_fun_total` int(10) DEFAULT NULL COMMENT '提交函数总数目',
  `fun_total` int(10) DEFAULT NULL COMMENT '函数总数目',
  `skil_points` varchar(4000) DEFAULT NULL COMMENT '技能点',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `moditiy_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `date_index` (`commit_time`),
  KEY `idx_pro_com_time` (`project_code`,`commit_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='提交记录分析报告展现表';

CREATE TABLE `ca_commit_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contributor` varchar(255) DEFAULT NULL COMMENT '作者',
  `contributor_email` varchar(255) DEFAULT NULL COMMENT '作者邮箱',
  `level` varchar(4) DEFAULT NULL COMMENT '职级',
  `dev_equivalent` bigint(20) DEFAULT NULL COMMENT '开发当量',
  `insertions` bigint(20) DEFAULT NULL COMMENT '增加代码行数',
  `deletions` bigint(20) DEFAULT NULL COMMENT '删除代码行数',
  `commit_num` int(10) DEFAULT NULL COMMENT '代码提交次数',
  `project_num` int(10) DEFAULT NULL COMMENT '项目数',
  `demand_num` int(10) DEFAULT NULL COMMENT '处理需求数',
  `bug_num` int(10) DEFAULT NULL COMMENT '处理bug数',
  `publish_num` tinyint(2) DEFAULT NULL COMMENT '发布次数',
  `compile_num` tinyint(2) DEFAULT NULL COMMENT '编译次数',
  `online_num` tinyint(2) DEFAULT NULL COMMENT '上线次数',
  `rollback_num` tinyint(2) DEFAULT NULL COMMENT '回滚次数',
  `restart_num` tinyint(2) DEFAULT NULL COMMENT '重启次数',
  `vacation` varchar(10) DEFAULT NULL COMMENT '休假天数',
  `work_hours` tinyint(2) DEFAULT NULL COMMENT '出勤工时',
  `devlop_hours` tinyint(2) DEFAULT NULL COMMENT '开发工时',
  `work_saturability` varchar(20) DEFAULT NULL COMMENT '工作饱和度',
  `department_code` varchar(255) DEFAULT NULL COMMENT '部门code',
  `department_name` varchar(255) DEFAULT NULL COMMENT '部门名字',
  `center_department_code` varchar(255) DEFAULT NULL COMMENT '中心部门code',
  `center_department_name` varchar(255) DEFAULT NULL COMMENT '中心部门名字',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `moditiy_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `date_index` (`commit_time`),
  KEY `idx_pro_com_time` (`project_code`,`commit_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='开发者分析报告展现表';

CREATE TABLE `demeter_person_growingup` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contributor` varchar(255) DEFAULT NULL COMMENT '作者',
  `contributor_email` varchar(255) DEFAULT NULL COMMENT '作者邮箱',
  `department_code` varchar(255) DEFAULT NULL COMMENT '部门code',
  `department_name` varchar(255) DEFAULT NULL COMMENT '部门名字',
  `type` tinyint(2) DEFAULT NULL COMMENT '样式类型',
  `title` varchar(64) DEFAULT NULL COMMENT '标题',
  `core_name` varchar(100) DEFAULT NULL COMMENT '核心指标名称',
  `core_data` decimal(50,10) DEFAULT NULL COMMENT '核心指标值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `moditiy_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='开发者个人成长画像展现表';
-----------------------------------------------------------技能图谱-----------------------------------------------------------------
create table demeter_assign_task
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_name` varchar(255) NOT NULL DEFAULT '' COMMENT '任务名称',
  `task_start_time` datetime DEFAULT NULL COMMENT '任务开始时间，若任务立即生效，则为当前时间',
  `task_end_time` datetime NOT NULL COMMENT '任务周期',
  `task_reward` int(11) NOT NULL COMMENT '成长值奖励',
  `attachment_url` varchar(255) NOT NULL DEFAULT '' COMMENT '任务附件',
  `attachment_name` varchar(1000) DEFAULT NULL COMMENT '附件名',
  `attachment_uuid` varchar(100) DEFAULT NULL COMMENT '附件uuid',
  `task_status` int(11) NOT NULL DEFAULT '0' COMMENT '任务状态',
  `task_description` varchar(2000) NOT NULL DEFAULT '' COMMENT '任务描述',
  `publisher` varchar(20) NOT NULL DEFAULT '',
  `need_email_remind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否邮件提醒',
  `need_punishment` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否有惩罚',
  `need_acceptance` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否需要验收',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) DEFAULT '' COMMENT '创建人',
  `modify_id` varchar(20) DEFAULT NULL COMMENT '修改人',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT '指派任务';

create table demeter_auth_history
(
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `user_task_id` bigint(10) NOT NULL COMMENT '主键',
  `auth_user` varchar(11) DEFAULT NULL COMMENT '认证人系统号',
  `auth_result` int(11) DEFAULT NULL COMMENT '认证结果',
  `auth_opinion` varchar(500) NOT NULL DEFAULT '' COMMENT '认证评价',
  `create_id` varchar(20) DEFAULT NULL,
  `modify_id` varchar(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='认证历史表';

create table demeter_role
(
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_code` varchar(100) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `demeter_role_role_code_uindex` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table demeter_skill_learn_path
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_user_id` bigint(20) NOT NULL COMMENT '关联detmeter_task_user主键',
  `task_id` bigint(20) NOT NULL COMMENT '关联demeter_skill_task主键',
  `path` varchar(255) NOT NULL COMMENT '学习路径',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) NOT NULL DEFAULT '',
  `modify_id` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='员工技能点学习路径';

create table demeter_skill_task
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_name` varchar(255) NOT NULL DEFAULT '' COMMENT '任务名称',
  `task_status` int(11) NOT NULL DEFAULT '1' COMMENT '任务状态：1启用，2禁用',
  `skill_reward` int(11) NOT NULL DEFAULT '0' COMMENT '技能值奖励',
  `attachment_url` varchar(255) NOT NULL DEFAULT '' COMMENT '附件链接',
  `attachment_uuid` varchar(100) DEFAULT NULL COMMENT '附件uuid',
  `attachment_name` varchar(1000) DEFAULT NULL COMMENT '附件名',
  `publisher` varchar(500) NOT NULL DEFAULT '' COMMENT '技能任务发布者',
  `check_role` varchar(100) NOT NULL COMMENT '技能点认证角色id，用“，”隔开',
  `task_remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `skill_id` int(10) unsigned NOT NULL COMMENT '所属技能id',
  `skill_level` int(11) NOT NULL COMMENT '技能点等级：初 中 高',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) DEFAULT '',
  `modify_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT '技能点';

create table demeter_task_user
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `task_type` int(11) NOT NULL COMMENT '任务类型',
  `receiver_uid` varchar(50) NOT NULL COMMENT '接收者系统号',
  `task_status` int(11) NOT NULL COMMENT '任务状态',
  `reject_reason` varchar(1000) DEFAULT NULL COMMENT '拒绝原因',
  `check_result` int(11) NOT NULL COMMENT '验收结果，-1为需要验收但未验收状态，-2为不需要验收的任务',
  `check_opinion` varchar(2000) DEFAULT '-' COMMENT '验收意见',
  `checkout_time` datetime DEFAULT NULL COMMENT '验收通过时间',
  `task_end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) NOT NULL DEFAULT '',
  `modify_id` varchar(20) NOT NULL DEFAULT '',
  `parent_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`task_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='任务发布和接收记录表';

create table demeter_task_user_extend
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_user_id` bigint(20) NOT NULL COMMENT '关联detmeter_task_user主键',
  `task_id` bigint(20) NOT NULL COMMENT '关联demeter_skill_task主键',
  `manifest_id` bigint(20) NOT NULL COMMENT '关联demeter_user_learn_manifest主键',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) NOT NULL DEFAULT '',
  `modify_id` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='员工任务扩展表(task-user，task，manifest)';

create table demeter_user_learn_manifest
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '学习清单名称',
  `assigner_uid` varchar(20) NOT NULL COMMENT '分配者',
  `learner_uid` varchar(20) NOT NULL COMMENT '学习者',
  `learn_period_start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '学习周期开始时间',
  `learn_period_end` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '学习周期结束时间',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 删除',
  `modify_id` varchar(20) NOT NULL DEFAULT '',
  `create_id` varchar(20) NOT NULL DEFAULT '',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='员工学习清单表';

create table jobs
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL COMMENT '职务编号',
  `name` varchar(20) NOT NULL COMMENT '职务名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '已删除 0 禁用 1 启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='互联网职务清单';

create table role_user
(
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `system_code` varchar(30) NOT NULL,
  `role_id` bigint(10) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='角色人员关系表';


create table skill_map
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '图谱名称',
  `is_enable` tinyint(4) NOT NULL DEFAULT '0' COMMENT '已启用 0 禁用 1 启用',
  `job_id` int(11) NOT NULL COMMENT '职位 id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '已删除 0 禁用 1 启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='技能图谱管理表';

create table skill_map_skill
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `skill_map_id` int(11) NOT NULL COMMENT '技能图谱表主键',
  `skill_task_id` bigint(20) NOT NULL COMMENT '技能点表主键',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '已删除 0 禁用 1 启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `job_level` tinyint(4) NOT NULL DEFAULT '1' COMMENT '职级 1-8',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='技能图谱-技能点关联表';

create table skill_tree
(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '节点名称',
  `parent_id` int(11) NOT NULL COMMENT '父节点 id',
  `sort` tinyint(4) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '已删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='技能树';

create table task_finish_condition
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL COMMENT '关联的任务id',
  `task_finish_content` varchar(255) DEFAULT NULL COMMENT '任务完成条件内容',
  `task_type` int(11) NOT NULL COMMENT '关联的任务类型',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) DEFAULT '',
  `modify_id` varchar(20) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='技能验收标准';

create table task_finish_condition_info
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(20) NOT NULL COMMENT '任务完成条件关联的员工系统号',
  `task_id` bigint(20) NOT NULL COMMENT '任务完成条件关联的任务id',
  `task_type` int(11) NOT NULL COMMENT '关联的任务类型',
  `task_finish_condition_id` bigint(20) NOT NULL COMMENT '任务完成条件id\n',
  `task_condition_status` int(11) NOT NULL COMMENT '任务完成条件的状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) DEFAULT NULL,
  `modify_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='员工技能点完成情况';

create table task_finish_outcome
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_address` varchar(2000) NOT NULL DEFAULT '' COMMENT '学习成果文件下载地址',
  `file_name` varchar(500) NOT NULL DEFAULT '' COMMENT '学习成果文件名',
  `task_id` bigint(20) NOT NULL COMMENT '关联的任务id',
  `task_type` int(11) NOT NULL COMMENT '学习成果关联的任务类型',
  `receiver_uid` varchar(20) NOT NULL COMMENT '学习成果关联的员工号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_id` varchar(20) DEFAULT NULL,
  `modify_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学习成果';

