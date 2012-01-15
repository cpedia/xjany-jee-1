/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2012-1-4 17:23:21                            */
/*==============================================================*/


drop table if exists all_user;

drop table if exists all_user_group;

drop table if exists bbs_bbslink;

drop table if exists bbs_board;

drop table if exists bbs_degree;

drop table if exists bbs_message;

drop table if exists bbs_online;

drop table if exists bbs_reply;

drop table if exists bbs_sub_board;

drop table if exists bbs_topic;

drop table if exists bbs_user_profile;

drop table if exists bbs_user_role;

drop table if exists district;

drop table if exists file;

/*==============================================================*/
/* Table: all_user                                              */
/*==============================================================*/
create table all_user
(
   userId               int not null auto_increment comment '用户ID',
   cms_userId           int,
   bbs_userId           int,
   userName             varchar(30) not null comment '用户名',
   userPsw              varchar(100) not null comment '密 码',
   groupId              int comment '组ID',
   userSex              int default 1 comment '用户性别(0、男 1、女)',
   userEmail            varchar(50) comment '用户E-mail',
   userRegTime          datetime comment '注册时间',
   userRegIp            varchar(30) comment '注册IP',
   userRealName         varchar(30) comment '真实姓名',
   userLoad             int default 0 comment '用户登陆次数',
   userLastTime         datetime comment '最后登陆时间',
   userLastIp           varchar(30) comment '最后登陆Ip',
   userState            int default 1 comment '用户状态 0、离线 1、在线',
   cms_del              int(2) default 0,
   primary key (userId)
);

alter table all_user comment '全局用户公共信息';

/*==============================================================*/
/* Table: bbs_bbslink                                           */
/*==============================================================*/
create table bbs_bbslink
(
   linkId               int not null auto_increment comment '友情论坛ID',
   linkName             varchar(50) not null comment '友情论坛名称',
   linkLogo             varchar(50) not null comment '友情论坛图标',
   linkUrl              varchar(50) not null comment '友情论坛URL地址',
   cms_del              int(2) default 0,
   primary key (linkId)
);

alter table bbs_bbslink comment '友情论坛表(bbslink)';

/*==============================================================*/
/* Table: bbs_board                                             */
/*==============================================================*/
create table bbs_board
(
   boaId                int not null auto_increment comment '父版块ID，主键，自动增长',
   boaName              varchar(30) not null comment '版块名称',
   boaMaster            varchar(20) comment '超级版主',
   cms_del              int(2)  default 0,
   primary key (boaId)
);

alter table bbs_board comment '父版块表';

/*==============================================================*/
/* Table: bbs_degree                                            */
/*==============================================================*/
create table bbs_degree
(
   degreeId             int not null auto_increment comment '等级ID',
   degreeName           varchar(50) comment '等级名',
   cms_del              int(2)  default 0,
   primary key (degreeId)
);

alter table bbs_degree comment '用户等级表(degree)';

/*==============================================================*/
/* Table: bbs_message                                           */
/*==============================================================*/
create table bbs_message
(
   mesId                int not null auto_increment comment '短消息ID',
   mesOwn               varchar(30) not null comment '短消息所属的用户',
   mesTitle             varchar(100) not null comment '短消息标题',
   mesContent           varchar(500) not null comment '短消息内容',
   mesPost              varchar(30) not null comment '短消息发布人',
   mesTime              datetime not null comment '短消息发表的时间',
   mesIsRead            int not null default 1 comment '短消息是否已读 0、否 1、是',
   cms_del              int(2)  default 0,
   primary key (mesId)
);

alter table bbs_message comment '短消息表(message)';

/*==============================================================*/
/* Table: bbs_online                                            */
/*==============================================================*/
create table bbs_online
(
   onId                 int not null auto_increment,
   onName               varchar(30) not null,
   onRole               varchar(50) not null,
   onIp                 varchar(30) not null,
   onBrowser            varchar(30) not null,
   onLocate             varchar(100) not null,
   onTime               datetime not null,
   cms_del              int(2)  default 0,
   primary key (onId)
);

alter table bbs_online comment '当前在线用户记录表(online)';

/*==============================================================*/
/* Table: bbs_reply                                             */
/*==============================================================*/
create table bbs_reply
(
   repId                int not null auto_increment,
   repTopicId           int not null comment '隶属的主题ID',
   repUser              varchar(30) not null,
   repContent           varchar(4000) not null,
   repTime              datetime not null,
   repIp                varchar(30) not null,
   cms_del              int(2)  default 0,
   primary key (repId)
);

alter table bbs_reply comment '帖子回复表(reply)';

/*==============================================================*/
/* Table: bbs_sub_board                                         */
/*==============================================================*/
create table bbs_sub_board
(
   subId                int not null auto_increment comment '子版块ID，主键，自动增长',
   subBoaId             int comment '父版块ID，主键，自动增长',
   subName              varchar(30) not null comment '版块名称',
   subImg               varchar(20) comment '版块图标',
   subInfo              varchar(200) comment '版块介绍',
   subMaster            varchar(20) comment '版主',
   subTopicNum          int comment '版块主题总数',
   subReNum             int comment '版块回复总数',
   subLastUser          varchar(20) comment '最后发表（主题或回复）人',
   subLastTopic         varchar(50) comment '最后访问（发表或回复）主题的名称',
   cms_del              int(2)  default 0,
   primary key (subId)
);

alter table bbs_sub_board comment '子版块表';

/*==============================================================*/
/* Table: bbs_topic                                             */
/*==============================================================*/
create table bbs_topic
(
   topicId              int not null auto_increment comment '主题ID，主键，自动增长',
   topicTitle           varchar(50) not null comment '主题名',
   topicContent         varchar(4000) not null comment '主题内容',
   topicUser            varchar(30) comment '主题发表者(论坛帖子发表人)',
   topicIp              varchar(30) comment '发表人的IP',
   topicTime            datetime not null comment '主题被创建时间',
   topicSubId           int not null comment '主题属于论坛哪个子版块',
   topicHits            int,
   topicReply           int default 0 comment '主题回复数',
   isNews               int default 1 comment '是否为公告 0、否 1、是',
   topicElite           int default 1 comment '是否为精华帖子 0、否 1、是',
   topicTop             int default 1 comment '是否置顶主题 0、否 1、是',
   topicLastUser        varchar(30) comment '主题最后回复人',
   topicLastTime        datetime not null comment '主题最后回复时间',
   cms_del              int(2)  default 0,
   primary key (topicId)
);

alter table bbs_topic comment '帖子表(topic)';

/*==============================================================*/
/* Table: bbs_user_profile                                      */
/*==============================================================*/
create table bbs_user_profile
(
   bbs_userId           int not null auto_increment,
   userId               int,
   roleId               int comment '角色ID',
   userRoleId           int comment '用户从属于的角色ID',
   userBirthDay         varchar(10) comment '用户生日',
   userFace             varchar(30) comment '用户头像',
   userQicq             varchar(10) comment 'Qicq',
   userIntro            varchar(500) comment '用户签名档',
   userAddr             varchar(30) comment '用户地址',
   userCareer           varchar(30) comment '职业介绍',
   userPosition         varchar(30) comment '职位',
   userCompany          varchar(30) comment '企业',
   userWealth           int default 0 comment '用户积分,每发1帖积1分(包括回复)，加精积5分',
   userDegree           int default 0 comment '用户等级',
   userTopic            int default 0 comment '用户发表的主题数总和',
   userReply            int default 0 comment '用户的回复数总和',
   userDelTopic         int default 0 comment '用户被删除的帖子总和',
   userEliteTopic       int default 0 comment '用户被推荐的精华帖总和',
   userDisable          int default 0 comment '用户锁定 0未锁,1锁定',
   primary key (bbs_userId)
);

alter table bbs_user_profile comment ' 用户表(user)';

/*==============================================================*/
/* Table: bbs_user_role                                         */
/*==============================================================*/
create table bbs_user_role
(
   roleId               int not null auto_increment comment '角色ID',
   roleName             varchar(50) not null comment '角色名',
   cms_del              int(2)  default 0,
   primary key (roleId)
);

alter table bbs_user_role comment '用户角色表(role)';

/*==============================================================*/
/* Table: all_user_group                                         */
/*==============================================================*/
create table all_user_group
(
   groupId               int not null auto_increment comment '组ID',
   groupName             varchar(50) not null comment '组名',
   cms_del              int(2)  default 0,
   primary key (groupId)
);

alter table all_user_group comment '用户组表(group)';

/*==============================================================*/
/* Table: district                                              */
/*==============================================================*/
create table district
(
   id                   int(11) not null auto_increment,
   name                 national varchar(10),
   province             national varchar(10),
   cms_del              int(2)  default 0,
   primary key (id)
);

/*==============================================================*/
/* Table: file                                                  */
/*==============================================================*/
create table file
(
   id                   int(11) not null auto_increment,
   downId               int(11),
   name                 national varchar(50),
   path                 national varchar(10),
   upId                 int(11),
   cms_del              int(2)  default 0,
   primary key (id)
);

alter table all_user add constraint FK_Reference_5 foreign key (bbs_userId)
      references bbs_user_profile (bbs_userId) on delete restrict on update restrict;

alter table bbs_reply add constraint FK_Reference_3 foreign key (repTopicId)
      references bbs_topic (topicId) on delete restrict on update restrict;

alter table bbs_sub_board add constraint FK_Reference_1 foreign key (subBoaId)
      references bbs_board (boaId) on delete restrict on update restrict;

alter table bbs_topic add constraint FK_Reference_2 foreign key (topicSubId)
      references bbs_sub_board (subId) on delete restrict on update restrict;

alter table bbs_user_profile add constraint FK_Reference_4 foreign key (userId)
      references all_user (userId) on delete restrict on update restrict;

alter table bbs_user_profile add constraint FK_Reference_6 foreign key (roleId)
      references bbs_user_role (roleId) on delete restrict on update restrict;
      
alter table all_user add constraint FK_Reference_7 foreign key (groupId)
      references all_user_group (groupId) on delete restrict on update restrict;

