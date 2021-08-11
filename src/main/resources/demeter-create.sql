create table demeter.demeter_assign_task
(
    id                bigint auto_increment comment 'primary key'
        primary key,
    task_name         varchar(255)  default '' not null comment '任务名称',
    task_start_time   datetime                 null comment '任务开始时间，若任务立即生效，则为当前时间',
    task_end_time     datetime                 not null comment '任务周期',
    task_reward       int                      not null comment '成长值奖励',
    attachment_url    varchar(255)  default '' not null comment '任务附件',
    attachment_name   varchar(1000)            null comment '附件名',
    attachment_uuid   varchar(100)             null comment '附件uuid',
    task_status       int           default 0  not null comment '任务状态',
    task_description  varchar(2000) default '' not null comment '任务描述',
    publisher         varchar(20)   default '' not null,
    need_email_remind tinyint       default 0  not null comment '是否邮件提醒',
    need_punishment   tinyint       default 0  not null comment '是否有惩罚',
    need_acceptance   tinyint       default 0  not null comment '是否需要验收',
    create_time       datetime                 not null comment '创建时间',
    update_time       datetime                 not null comment '修改时间',
    create_id         varchar(20)   default '' not null comment '创建人',
    modify_id         varchar(20)              null comment '修改人'
);

create table demeter.demeter_auth_history
(
    id           bigint(10) auto_increment
        primary key,
    user_task_id bigint(10)              not null comment 'demeter_task_user表主键',
    auth_user    varchar(11)             null comment '认证人系统号',
    auth_result  int                     null comment '认证结果',
    auth_opinion varchar(500) default '' not null comment '认证评价',
    create_id    varchar(20)             null,
    modify_id    varchar(20)             null,
    create_time  datetime                null,
    modify_time  datetime                null
)
    comment '认证历史表';

create table demeter.demeter_jira_data_model
(
    id                varchar(100) not null comment '唯一id'
        primary key,
    uid               varchar(100) not null comment '用户邮箱前缀',
    issue_id          varchar(100) null comment 'jira标识',
    department_code   varchar(255) null comment '部门code',
    department_name   varchar(255) null comment '部门名称',
    project_name      varchar(255) null comment '项目名称',
    jira_status       varchar(255) null comment 'jiar状态',
    type              varchar(255) null comment 'jira类型',
    expect_start_time datetime     null comment '预计开始时间',
    expect_end_time   datetime     null comment '预计结束时间',
    actual_start_time datetime     null comment '实际开始时间',
    actual_end_time   datetime     null comment '实际结束时间',
    create_time       datetime     null comment '创建时间',
    update_time       datetime     null comment '更新时间'
);



create table demeter.demeter_role
(
    id          bigint(10) auto_increment
        primary key,
    role_code   varchar(100)                            not null comment '角色编码',
    role_name   varchar(100)                            not null comment '角色名',
    create_time timestamp default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP,
    update_time timestamp default '0000-00-00 00:00:00' not null,
    constraint demeter_role_role_code_uindex
        unique (role_code)
);

create table demeter.demeter_skill_learn_path
(
    id           bigint auto_increment
        primary key,
    task_user_id bigint                 not null comment '关联detmeter_task_user主键',
    task_id      bigint                 not null comment '关联demeter_skill_task主键',
    path         varchar(255)           not null comment '学习路径',
    create_time  datetime               not null,
    modify_time  datetime               not null,
    create_id    varchar(20) default '' not null,
    modify_id    varchar(20) default '' not null
)
    comment '员工技能点学习路径';

create table demeter.demeter_skill_task
(
    id              bigint auto_increment
        primary key,
    task_name       varchar(255)  default '' not null comment '任务名称',
    task_status     int           default 1  not null comment '任务状态：1启用，2禁用',
    skill_reward    int           default 0  not null comment '技能值奖励',
    attachment_url  varchar(255)  default '' not null comment '附件链接',
    attachment_uuid varchar(100)             null comment '附件uuid',
    attachment_name varchar(1000)            null comment '附件名',
    publisher       varchar(500)  default '' not null comment '技能任务发布者',
    check_role      varchar(100)             not null comment '技能点认证角色id，用“，”隔开',
    task_remark     varchar(2000) default '' not null comment '备注',
    skill_id        int unsigned             not null comment '所属技能id',
    skill_level     int                      not null comment '技能点等级：初 中 高',
    create_time     datetime                 not null,
    update_time     datetime                 not null,
    create_id       varchar(20)   default '' not null,
    modify_id       varchar(20)              not null
);

create table demeter.demeter_task_user
(
    id            bigint auto_increment,
    task_id       bigint                    not null comment '任务id',
    task_type     int                       not null comment '任务类型',
    receiver_uid  varchar(50)               not null comment '接收者系统号',
    task_status   int                       not null comment '任务状态',
    reject_reason varchar(1000)             null comment '拒绝原因',
    check_result  int                       not null comment '验收结果，-1为需要验收但未验收状态，-2为不需要验收的任务',
    check_opinion varchar(2000) default '-' null comment '验收意见',
    checkout_time datetime                  null comment '验收通过时间',
    task_end_time datetime                  null comment '任务结束时间',
    create_time   datetime                  not null,
    modify_time   datetime                  not null,
    create_id     varchar(20)   default ''  not null,
    modify_id     varchar(20)   default ''  not null,
    parent_id     int(10)                   null,
    primary key (id, task_id)
)
    comment '任务发布和接收记录表';

create table demeter.demeter_task_user_extend
(
    id           bigint auto_increment
        primary key,
    task_user_id bigint                 not null comment '关联detmeter_task_user主键',
    task_id      bigint                 not null comment '关联demeter_skill_task主键',
    manifest_id  bigint                 not null comment '关联demeter_user_learn_manifest主键',
    create_time  datetime               not null,
    modify_time  datetime               not null,
    create_id    varchar(20) default '' not null,
    modify_id    varchar(20) default '' not null
)
    comment '员工任务扩展表';

create table demeter.demeter_user_email
(
    id          bigint(10) auto_increment comment '唯一id'
        primary key,
    email       varchar(255) null comment '员工邮箱',
    sub_email   varchar(255) null comment '员工子邮箱',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间',
    constraint sub_email
        unique (sub_email)
);

create table demeter.demeter_user_info
(
    id          varchar(50)  not null comment '唯一id'
        primary key,
    name        varchar(255) null comment '员工姓名
',
    job_code    varchar(255) null comment '职业号码',
    job_name    varchar(255) null comment '职业名称',
    user_code   varchar(255) null comment '员工自如系统号',
    dept_code   varchar(255) null comment '部门code',
    create_time date         null comment '创建日期',
    update_time date         null comment '更新时间',
    phone       varchar(255) null comment '手机号',
    center_code varchar(255) null
);

create table demeter.demeter_user_learn_manifest
(
    id           bigint auto_increment
        primary key,
    name         varchar(20)            not null comment '学习清单名称',
    assigner_uid varchar(20)            not null comment '分配者',
    learner_uid  varchar(20)            not null comment '学习者',
    learn_period varchar(20)            not null comment '学习周期',
    create_time  datetime               not null,
    modify_time  datetime               not null,
    create_id    varchar(20) default '' not null,
    modify_id    varchar(20) default '' not null
)
    comment '员工学习清单表';

create table demeter.jobs
(
    id          bigint auto_increment
        primary key,
    code        int                                 not null comment '职务编号',
    name        varchar(20)                         not null comment '职务名称',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    modify_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_del      tinyint   default 0                 not null comment '已删除 0 禁用 1 启用',
    constraint unique_code
        unique (code)
)
    comment '互联网职务清单';

create table demeter.role_user
(
    id          bigint(10) auto_increment
        primary key,
    system_code varchar(30)                             not null,
    role_id     bigint(10)                              not null,
    create_time timestamp default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP,
    update_time timestamp default '0000-00-00 00:00:00' not null
)
    comment '角色人员关系表';

create table demeter.skill_map
(
    id          bigint auto_increment
        primary key,
    name        varchar(32)                         not null comment '图谱名称',
    is_enable   tinyint   default 0                 not null comment '已启用 0 禁用 1 启用',
    job_id      int                                 not null comment '职位 id',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    modify_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_del      tinyint   default 0                 not null comment '已删除 0 禁用 1 启用'
)
    comment '技能图谱管理表';

create table demeter.skill_map_skill
(
    id            bigint auto_increment
        primary key,
    skill_map_id  int                                 not null comment '技能图谱表主键',
    skill_task_id bigint                              not null comment '技能点表主键',
    is_del        tinyint   default 0                 not null comment '已删除 0 禁用 1 启用',
    create_time   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    modify_time   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    job_level     tinyint   default 1                 not null comment '职级 1-8'
)
    comment '技能图谱-技能点关联表';

create table demeter.skill_tree
(
    id          int auto_increment
        primary key,
    name        varchar(32)                         not null comment '节点名称',
    parent_id   int                                 not null comment '父节点 id',
    sort        tinyint   default 0                 not null comment '排序',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    modify_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_del      tinyint   default 0                 not null comment '已删除 0 否 1 是'
)
    comment '技能树';

create table demeter.task_finish_condition
(
    id                  bigint auto_increment
        primary key,
    task_id             bigint                 not null comment '关联的任务id',
    task_finish_content varchar(255)           null comment '任务完成条件内容',
    task_type           int                    not null comment '关联的任务类型',
    create_time         datetime               not null,
    modify_time         datetime               not null,
    create_id           varchar(20) default '' not null,
    modify_id           varchar(20) default '' not null
);

create table demeter.task_finish_condition_info
(
    id                       bigint auto_increment
        primary key,
    uid                      varchar(20) not null comment '任务完成条件关联的员工系统号',
    task_id                  bigint      not null comment '任务完成条件关联的任务id',
    task_type                int         not null comment '关联的任务类型',
    task_finish_condition_id bigint      not null comment '任务完成条件id
',
    task_condition_status    int         not null comment '任务完成条件的状态',
    create_time              datetime    not null,
    modify_time              datetime    not null,
    create_id                varchar(20) not null,
    modify_id                varchar(20) not null
);

create table demeter.task_finish_outcome
(
    id           bigint auto_increment
        primary key,
    file_address varchar(2000) default '' not null comment '学习成果文件下载地址',
    file_name    varchar(500)  default '' not null comment '学习成果文件名',
    task_id      bigint                   not null comment '关联的任务id',
    task_type    int                      not null comment '学习成果关联的任务类型',
    receiver_uid varchar(20)              not null comment '学习成果关联的员工号',
    create_time  datetime                 not null,
    modify_time  datetime                 not null,
    create_id    varchar(20)              not null,
    modify_id    varchar(20)              not null
)
    comment '学习成果';

