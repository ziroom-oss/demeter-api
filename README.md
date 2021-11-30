员工成长平台后端项目

主要分为两个模块：技能点学习、画像管理
1. 技能点学习：旨在为员工提供自我成长的学习规划路径 每个岗位都对应一颗技能树 员工通过自我学习技能(待师兄验收通过),项目中应用技能(熟练度达标)可点亮技能树
   技能树可反映一个人技术水平,可以以此衡量一个人的能力
2. 画像管理：分为员工个人画像和团队画像。画像是一个人工作的直观表现,画像中以多个维度指标衡量一个人工作的水平,可以此反应一个人工作的质量。

接入此系统需要完成的事情：
1. 员工画像部分-完成数据指标的采集分析,根据本系统提供的数据模型 采集指标 定时同步数据指标, 此系统仅提供数据展示能力(演示版 有静态数据提供)。
2. 接入自己公司的用户中心, 消息中心, 文件中心(演示版 有本地服务提供, 用户中心参见 EhrServiceClient.class 消息中心参见 ConsoleSendHandler.class 文件中心参见 FileService.class 用户接入需要替换)
3. 配置文件中更改redis缓存地址, 根据需要修改 RedisFacade.class (登录模块需要redis, 演示版有本地缓存提供)
4. 登录拦截器(演示版 由TestLoginInterceptor.class支持, 正式使用时替换为LoginInterceptor.class)

指标数据解读：
开发当量：衡量开发者修改代码的工作量的指标
代码影响力：综合开发当量和函数调用关系,影响力越大代表该提交或者该开发者贡献代码对整个项目代码库的影响越广。  开发者影响的函数数目/总函数数目
注释覆盖度：有注释的函数占项目中总函数个数的比例。  提交有注释函数数目/提交的总函数数目
测试覆盖度计算：被测试函数覆盖的函数占项目中非测试函数总数比例。  被测试覆盖的函数数目/提交的总函数数目
工程质量：包括代码复用度、测试覆盖度、注释覆盖度、代码问题数等。
开发价值：综合了开发当量、开发影响力和工程质量的综合指数。  开发价值 = 代码影响力% + 开发当量% + 代码质量% + 测试覆盖度% + 注释覆盖度%
工作饱和度：开发工时/出勤工时
千行bug率计算：

员工画像
1. 代码提交记录分析报告快照表(ca_commit_report), 开发者每次提交都生成一条分析记录快照
开发价值 代码影响力指标 实时变动性强 为方便后面的统计准确性 1. 可定时刷新分析记录,更新保存的开发价值, 影响函数数目等指标 2. 也可在用到的时候实时从源数据提取计算

2. 开发者工作体现分析报告表(ca_commit_report), 多个维度指标的聚合,统计开发者时间区间的表现,以天为单位生成开发者分析报告

3. 开发者成长画像记录表(demeter_person_growingup), 该表存储开发者的成长表现. 注：同一标题 样式类型 必须相同
