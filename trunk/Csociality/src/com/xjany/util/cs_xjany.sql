/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2012-1-4 17:23:21                            */
/*==============================================================*/


drop table if exists all_user;

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
   userId               int not null auto_increment comment '�û�ID',
   cms_userId           int,
   bbs_userId           int,
   userName             varchar(30) not null comment '�û���',
   userPsw              varchar(30) not null comment '�� ��',
   userSex              int not null default 1 comment '�û��Ա�(0���� 1��Ů)',
   userEmail            varchar(50) not null comment '�û�E-mail',
   userRegTime          datetime not null comment 'ע��ʱ��',
   userIp               varchar(30) not null comment 'ע��IP',
   userLoad             int not null default 0 comment '�û���½����',
   userLastTime         datetime not null comment '����½ʱ��',
   userState            int not null default 1 comment '�û�״̬ 0������ 1������',
   cms_del              int default 0,
   primary key (userId)
);

alter table all_user comment 'ȫ���û�������Ϣ';

/*==============================================================*/
/* Table: bbs_bbslink                                           */
/*==============================================================*/
create table bbs_bbslink
(
   linkId               int not null comment '������̳ID',
   linkName             varchar(50) not null comment '������̳����',
   linkLogo             varchar(50) not null comment '������̳ͼ��',
   linkUrl              varchar(50) not null comment '������̳URL��ַ',
   cms_del              int,
   primary key (linkId)
);

alter table bbs_bbslink comment '������̳��(bbslink)';

/*==============================================================*/
/* Table: bbs_board                                             */
/*==============================================================*/
create table bbs_board
(
   boaId                int not null auto_increment comment '�����ID���������Զ�����',
   boaName              varchar(30) not null comment '�������',
   boaMaster            varchar(20) comment '��������',
   cms_del              int,
   primary key (boaId)
);

alter table bbs_board comment '������';

/*==============================================================*/
/* Table: bbs_degree                                            */
/*==============================================================*/
create table bbs_degree
(
   degreeId             int not null auto_increment comment '�ȼ�ID',
   degreeName           varchar(50) comment '�ȼ���',
   cms_del              int,
   primary key (degreeId)
);

alter table bbs_degree comment '�û��ȼ���(degree)';

/*==============================================================*/
/* Table: bbs_message                                           */
/*==============================================================*/
create table bbs_message
(
   mesId                int not null auto_increment comment '����ϢID',
   mesOwn               varchar(30) not null comment '����Ϣ�������û�',
   mesTitle             varchar(100) not null comment '����Ϣ����',
   mesContent           varchar(500) not null comment '����Ϣ����',
   mesPost              varchar(30) not null comment '����Ϣ������',
   mesTime              datetime not null comment '����Ϣ�����ʱ��',
   mesIsRead            int not null default 1 comment '����Ϣ�Ƿ��Ѷ� 0���� 1����',
   cms_del              int,
   primary key (mesId)
);

alter table bbs_message comment '����Ϣ��(message)';

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
   cms_del              int,
   primary key (onId)
);

alter table bbs_online comment '��ǰ�����û���¼��(online)';

/*==============================================================*/
/* Table: bbs_reply                                             */
/*==============================================================*/
create table bbs_reply
(
   repId                int not null auto_increment,
   repTopicId           int not null comment '����������ID',
   repUser              varchar(30) not null,
   repContent           varchar(4000) not null,
   repTime              datetime not null,
   repIp                varchar(30) not null,
   cms_del              int,
   primary key (repId)
);

alter table bbs_reply comment '���ӻظ���(reply)';

/*==============================================================*/
/* Table: bbs_sub_board                                         */
/*==============================================================*/
create table bbs_sub_board
(
   subId                int not null auto_increment comment '�Ӱ��ID���������Զ�����',
   subBoaId             int comment '�����ID���������Զ�����',
   subName              varchar(30) not null comment '�������',
   subImg               varchar(20) comment '���ͼ��',
   subInfo              varchar(200) comment '������',
   subMaster            varchar(20) comment '����',
   subTopicNum          int comment '�����������',
   subReNum             int comment '���ظ�����',
   subLastUser          varchar(20) comment '��󷢱������ظ�����',
   subLastTopic         varchar(50) comment '�����ʣ������ظ������������',
   cms_del              int,
   primary key (subId)
);

alter table bbs_sub_board comment '�Ӱ���';

/*==============================================================*/
/* Table: bbs_topic                                             */
/*==============================================================*/
create table bbs_topic
(
   topicId              int not null auto_increment comment '����ID���������Զ�����',
   topicTitle           varchar(50) not null comment '������',
   topicContent         varchar(4000) not null comment '��������',
   topicUser            varchar(30) comment '���ⷢ����(��̳���ӷ�����)',
   topicIp              varchar(30) comment '�����˵�IP',
   topicTime            datetime not null comment '���ⱻ����ʱ��',
   topicSubId           int not null comment '����������̳�ĸ��Ӱ��',
   topicHits            int,
   topicReply           int default 0 comment '����ظ���',
   isNews               int default 1 comment '�Ƿ�Ϊ���� 0���� 1����',
   topicElite           int default 1 comment '�Ƿ�Ϊ�������� 0���� 1����',
   topicTop             int default 1 comment '�Ƿ��ö����� 0���� 1����',
   topicLastUser        varchar(30) comment '�������ظ���',
   topicLastTime        datetime not null comment '�������ظ�ʱ��',
   cms_del              int,
   primary key (topicId)
);

alter table bbs_topic comment '���ӱ�(topic)';

/*==============================================================*/
/* Table: bbs_user_profile                                      */
/*==============================================================*/
create table bbs_user_profile
(
   bbs_userId           int not null,
   userId               int,
   roleId               int comment '��ɫID',
   userRoleId           int comment '�û������ڵĽ�ɫID',
   userBirthDay         varchar(10) comment '�û�����',
   userFace             varchar(30) comment '�û�ͷ��',
   userQicq             varchar(10) comment 'Qicq',
   userIntro            varchar(500) comment '�û�ǩ����',
   userAddr             varchar(30) comment '�û���ַ',
   userCareer           varchar(30) comment 'ְҵ����',
   userWealth           int default 0 comment '�û�����,ÿ��1����1��(�����ظ�)���Ӿ���5��',
   userDegree           int default 0 comment '�û��ȼ�',
   userTopic            int default 0 comment '�û�������������ܺ�',
   userReply            int default 0 comment '�û��Ļظ����ܺ�',
   userDelTopic         int default 0 comment '�û���ɾ���������ܺ�',
   userEliteTopic       int default 0 comment '�û����Ƽ��ľ������ܺ�',
   primary key (bbs_userId)
);

alter table bbs_user_profile comment ' �û���(user)';

/*==============================================================*/
/* Table: bbs_user_role                                         */
/*==============================================================*/
create table bbs_user_role
(
   roleId               int not null auto_increment comment '��ɫID',
   roleName             varchar(50) not null comment '��ɫ��',
   cms_del              int,
   primary key (roleId)
);

alter table bbs_user_role comment '�û���ɫ��(role)';

/*==============================================================*/
/* Table: district                                              */
/*==============================================================*/
create table district
(
   id                   int(11) not null auto_increment,
   name                 national varchar(10),
   province             national varchar(10),
   cms_del              int,
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
   cms_del              int,
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

