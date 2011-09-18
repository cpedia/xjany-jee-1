-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.50-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema xjany
--

CREATE DATABASE IF NOT EXISTS xjany;
USE xjany;

--
-- Definition of table `jc_acquisition`
--

DROP TABLE IF EXISTS `jc_acquisition`;
CREATE TABLE `jc_acquisition` (
  `acquisition_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `acq_name` varchar(50) NOT NULL COMMENT '采集名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '停止时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '当前状态(0:静止;1:采集;2:暂停)',
  `curr_num` int(11) NOT NULL DEFAULT '0' COMMENT '当前号码',
  `curr_item` int(11) NOT NULL DEFAULT '0' COMMENT '当前条数',
  `total_item` int(11) NOT NULL DEFAULT '0' COMMENT '每页总条数',
  `pause_time` int(11) NOT NULL DEFAULT '0' COMMENT '暂停时间(毫秒)',
  `page_encoding` varchar(20) NOT NULL DEFAULT 'GBK' COMMENT '页面编码',
  `plan_list` longtext COMMENT '采集列表',
  `dynamic_addr` varchar(255) DEFAULT NULL COMMENT '动态地址',
  `dynamic_start` int(11) DEFAULT NULL COMMENT '页码开始',
  `dynamic_end` int(11) DEFAULT NULL COMMENT '页码结束',
  `linkset_start` varchar(255) DEFAULT NULL COMMENT '内容链接区开始',
  `linkset_end` varchar(255) DEFAULT NULL COMMENT '内容链接区结束',
  `link_start` varchar(255) DEFAULT NULL COMMENT '内容链接开始',
  `link_end` varchar(255) DEFAULT NULL COMMENT '内容链接结束',
  `title_start` varchar(255) DEFAULT NULL COMMENT '标题开始',
  `title_end` varchar(255) DEFAULT NULL COMMENT '标题结束',
  `keywords_start` varchar(255) DEFAULT NULL COMMENT '关键字开始',
  `keywords_end` varchar(255) DEFAULT NULL COMMENT '关键字结束',
  `description_start` varchar(255) DEFAULT NULL COMMENT '描述开始',
  `description_end` varchar(255) DEFAULT NULL COMMENT '描述结束',
  `content_start` varchar(255) DEFAULT NULL COMMENT '内容开始',
  `content_end` varchar(255) DEFAULT NULL COMMENT '内容结束',
  `pagination_start` varchar(255) DEFAULT NULL COMMENT '内容分页开始',
  `pagination_end` varchar(255) DEFAULT NULL COMMENT '内容分页结束',
  PRIMARY KEY (`acquisition_id`),
  KEY `fk_jc_acquisition_channel` (`channel_id`),
  KEY `fk_jc_acquisition_contenttype` (`type_id`),
  KEY `fk_jc_acquisition_site` (`site_id`),
  KEY `fk_jc_acquisition_user` (`user_id`),
  CONSTRAINT `fk_jc_acquisition_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_acquisition_contenttype` FOREIGN KEY (`type_id`) REFERENCES `jc_content_type` (`type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_acquisition_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_acquisition_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS采集表';

--
-- Dumping data for table `jc_acquisition`
--

/*!40000 ALTER TABLE `jc_acquisition` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_acquisition` ENABLE KEYS */;


--
-- Definition of table `jc_advertising`
--

DROP TABLE IF EXISTS `jc_advertising`;
CREATE TABLE `jc_advertising` (
  `advertising_id` int(11) NOT NULL AUTO_INCREMENT,
  `adspace_id` int(11) NOT NULL,
  `site_id` int(11) NOT NULL,
  `ad_name` varchar(100) NOT NULL COMMENT '广告名称',
  `category` varchar(50) NOT NULL COMMENT '广告类型',
  `ad_code` longtext COMMENT '广告代码',
  `ad_weight` int(11) NOT NULL DEFAULT '1' COMMENT '广告权重',
  `display_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '展现次数',
  `click_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点击次数',
  `start_time` date DEFAULT NULL COMMENT '开始时间',
  `end_time` date DEFAULT NULL COMMENT '结束时间',
  `is_enabled` char(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`advertising_id`),
  KEY `fk_jc_advertising_site` (`site_id`),
  KEY `fk_jc_space_advertising` (`adspace_id`),
  CONSTRAINT `fk_jc_advertising_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
  CONSTRAINT `fk_jc_space_advertising` FOREIGN KEY (`adspace_id`) REFERENCES `jc_advertising_space` (`adspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='CMS广告表';

--
-- Dumping data for table `jc_advertising`
--

/*!40000 ALTER TABLE `jc_advertising` DISABLE KEYS */;
INSERT INTO `jc_advertising` (`advertising_id`,`adspace_id`,`site_id`,`ad_name`,`category`,`ad_code`,`ad_weight`,`display_count`,`click_count`,`start_time`,`end_time`,`is_enabled`) VALUES 
 (1,1,1,'banner','image',NULL,1,135,0,NULL,NULL,'1'),
 (2,2,1,'通栏广告1','image',NULL,1,55,2,NULL,NULL,'1');
/*!40000 ALTER TABLE `jc_advertising` ENABLE KEYS */;


--
-- Definition of table `jc_advertising_attr`
--

DROP TABLE IF EXISTS `jc_advertising_attr`;
CREATE TABLE `jc_advertising_attr` (
  `advertising_id` int(11) NOT NULL,
  `attr_name` varchar(50) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_params_advertising` (`advertising_id`),
  CONSTRAINT `fk_jc_params_advertising` FOREIGN KEY (`advertising_id`) REFERENCES `jc_advertising` (`advertising_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS广告属性表';

--
-- Dumping data for table `jc_advertising_attr`
--

/*!40000 ALTER TABLE `jc_advertising_attr` DISABLE KEYS */;
INSERT INTO `jc_advertising_attr` (`advertising_id`,`attr_name`,`attr_value`) VALUES 
 (1,'image_title','查看JEECMS官方网站'),
 (1,'image_url','/r/cms/www/red/img/banner.gif'),
 (1,'image_target','_blank'),
 (1,'image_link','http://www.jeecms.com'),
 (1,'image_width','735'),
 (1,'image_height','70'),
 (2,'image_title','JEECMS官方网站'),
 (2,'image_url','/r/cms/www/red/img/banner1.jpg'),
 (2,'image_target','_blank'),
 (2,'image_link','http://www.jeecms.com'),
 (2,'image_width','960'),
 (2,'image_height','60');
/*!40000 ALTER TABLE `jc_advertising_attr` ENABLE KEYS */;


--
-- Definition of table `jc_advertising_space`
--

DROP TABLE IF EXISTS `jc_advertising_space`;
CREATE TABLE `jc_advertising_space` (
  `adspace_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `ad_name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_enabled` char(1) NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`adspace_id`),
  KEY `fk_jc_adspace_site` (`site_id`),
  CONSTRAINT `fk_jc_adspace_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='CMS广告版位表';

--
-- Dumping data for table `jc_advertising_space`
--

/*!40000 ALTER TABLE `jc_advertising_space` DISABLE KEYS */;
INSERT INTO `jc_advertising_space` (`adspace_id`,`site_id`,`ad_name`,`description`,`is_enabled`) VALUES 
 (1,1,'页头banner','全站页头banner','1'),
 (2,1,'通栏广告','页面中间通栏广告','1');
/*!40000 ALTER TABLE `jc_advertising_space` ENABLE KEYS */;


--
-- Definition of table `jc_channel`
--

DROP TABLE IF EXISTS `jc_channel`;
CREATE TABLE `jc_channel` (
  `channel_id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` int(11) NOT NULL COMMENT '模型ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父栏目ID',
  `channel_path` varchar(30) DEFAULT NULL COMMENT '访问路径',
  `lft` int(11) NOT NULL DEFAULT '1' COMMENT '树左边',
  `rgt` int(11) NOT NULL DEFAULT '2' COMMENT '树右边',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `has_content` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有内容',
  `is_display` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  PRIMARY KEY (`channel_id`),
  KEY `fk_jc_channel_model` (`model_id`),
  KEY `fk_jc_channel_parent` (`parent_id`),
  KEY `fk_jc_channel_site` (`site_id`),
  CONSTRAINT `fk_jc_channel_model` FOREIGN KEY (`model_id`) REFERENCES `jc_model` (`model_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_channel_parent` FOREIGN KEY (`parent_id`) REFERENCES `jc_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_channel_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='CMS栏目表';

--
-- Dumping data for table `jc_channel`
--

/*!40000 ALTER TABLE `jc_channel` DISABLE KEYS */;
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES 
 (10,2,1,NULL,'about',1,2,10,0,1),
 (40,2,1,NULL,'contactus',3,4,10,0,1),
 (43,1,1,NULL,'Doctor',5,6,1,1,1),
 (44,4,1,NULL,'Product',7,8,10,1,1);
/*!40000 ALTER TABLE `jc_channel` ENABLE KEYS */;


--
-- Definition of table `jc_channel_attr`
--

DROP TABLE IF EXISTS `jc_channel_attr`;
CREATE TABLE `jc_channel_attr` (
  `channel_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_channel` (`channel_id`),
  CONSTRAINT `fk_jc_attr_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目扩展属性表';

--
-- Dumping data for table `jc_channel_attr`
--

/*!40000 ALTER TABLE `jc_channel_attr` DISABLE KEYS */;
INSERT INTO `jc_channel_attr` (`channel_id`,`attr_name`,`attr_value`) VALUES 
 (10,'ename','about'),
 (40,'ename','contact'),
 (43,'ename',''),
 (44,'ename','');
/*!40000 ALTER TABLE `jc_channel_attr` ENABLE KEYS */;


--
-- Definition of table `jc_channel_ext`
--

DROP TABLE IF EXISTS `jc_channel_ext`;
CREATE TABLE `jc_channel_ext` (
  `channel_id` int(11) NOT NULL,
  `channel_name` varchar(100) NOT NULL COMMENT '名称',
  `final_step` tinyint(4) DEFAULT '2' COMMENT '终审级别',
  `after_check` tinyint(4) DEFAULT NULL COMMENT '审核后(1:不能修改删除;2:修改后退回;3:修改后不变)',
  `is_static_channel` char(1) NOT NULL DEFAULT '0' COMMENT '是否栏目静态化',
  `is_static_content` char(1) NOT NULL DEFAULT '0' COMMENT '是否内容静态化',
  `is_access_by_dir` char(1) NOT NULL DEFAULT '1' COMMENT '是否使用目录访问',
  `is_list_child` char(1) NOT NULL DEFAULT '0' COMMENT '是否使用子栏目列表',
  `page_size` int(11) NOT NULL DEFAULT '20' COMMENT '每页多少条记录',
  `channel_rule` varchar(150) DEFAULT NULL COMMENT '栏目页生成规则',
  `content_rule` varchar(150) DEFAULT NULL COMMENT '内容页生成规则',
  `link` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `tpl_channel` varchar(100) DEFAULT NULL COMMENT '栏目页模板',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '内容页模板',
  `title_img` varchar(100) DEFAULT NULL COMMENT '缩略图',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图',
  `has_title_img` tinyint(1) NOT NULL DEFAULT '0' COMMENT '内容是否有缩略图',
  `has_content_img` tinyint(1) NOT NULL DEFAULT '0' COMMENT '内容是否有内容图',
  `title_img_width` int(11) NOT NULL DEFAULT '139' COMMENT '内容标题图宽度',
  `title_img_height` int(11) NOT NULL DEFAULT '139' COMMENT '内容标题图高度',
  `content_img_width` int(11) NOT NULL DEFAULT '310' COMMENT '内容内容图宽度',
  `content_img_height` int(11) NOT NULL DEFAULT '310' COMMENT '内容内容图高度',
  `comment_control` int(11) NOT NULL DEFAULT '0' COMMENT '评论(0:匿名;1:会员;2:关闭)',
  `allow_updown` tinyint(1) NOT NULL DEFAULT '1' COMMENT '顶踩(true:开放;false:关闭)',
  `is_blank` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否新窗口打开',
  `title` varchar(255) DEFAULT NULL COMMENT 'TITLE',
  `keywords` varchar(255) DEFAULT NULL COMMENT 'KEYWORDS',
  `description` varchar(255) DEFAULT NULL COMMENT 'DESCRIPTION',
  PRIMARY KEY (`channel_id`),
  CONSTRAINT `fk_jc_ext_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目内容表';

--
-- Dumping data for table `jc_channel_ext`
--

/*!40000 ALTER TABLE `jc_channel_ext` DISABLE KEYS */;
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`) VALUES 
 (10,'关于我们',NULL,NULL,'0','0','1','0',20,NULL,NULL,NULL,NULL,NULL,NULL,'http://www.richlife-china.com/my/images/7be21eff-2a1c-4841-a148-1683d8c0f2bf.jpg',0,0,139,139,310,310,0,1,0,'关于我们','关于我们','关于我们'),
 (40,'联系我们',NULL,NULL,'0','0','1','0',20,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,NULL,NULL,NULL),
 (43,'天然医生',NULL,NULL,'0','0','0','0',20,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,'天然医生','天然医生','天然医生'),
 (44,'产品中心',NULL,NULL,'0','0','1','0',20,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,139,139,310,310,0,1,0,'产品中心',NULL,'产品中心');
/*!40000 ALTER TABLE `jc_channel_ext` ENABLE KEYS */;


--
-- Definition of table `jc_channel_txt`
--

DROP TABLE IF EXISTS `jc_channel_txt`;
CREATE TABLE `jc_channel_txt` (
  `channel_id` int(11) NOT NULL,
  `txt` longtext COMMENT '栏目内容',
  `txt1` longtext COMMENT '扩展内容1',
  `txt2` longtext COMMENT '扩展内容2',
  `txt3` longtext COMMENT '扩展内容3',
  PRIMARY KEY (`channel_id`),
  CONSTRAINT `fk_jc_txt_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目文本表';

--
-- Dumping data for table `jc_channel_txt`
--

/*!40000 ALTER TABLE `jc_channel_txt` DISABLE KEYS */;
INSERT INTO `jc_channel_txt` (`channel_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES 
 (10,'<p><font size=\"2\">&nbsp;&nbsp;&nbsp; JEECMS是JavaEE版网站管理系统（Java Enterprise Edition Content Manage System）的简称。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;Java凭借其强大、稳定、安全、高效等多方面的优势，一直是企业级应用的首选。在国外基于JavaEE技术的CMS已经发展的相当成熟，但授权费昂贵，一般需几十万一套；而国内在这方面一直比较薄弱，至今没有一款基于JavaEE技术的开源免费CMS产品。这次我们本着&quot;大气开源，诚信图强&quot;的原则将我们开发的这套JEECMS系统源码完全公布，希望能为国内JavaEE技术的发展尽自己的一份力量。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;JEECMS使用目前java主流技术架构：hibernate3+spring3+freemarker。AJAX使用jquery和json实现。视图层并没有使用传统的 JSP技术，而是使用更为专业、灵活、高效freemarker。 数据库使用MYSQL，并可支持orcale、DB2、SQLServer等主流数据库。应用服务器使用tomcat，并支持其他weblogic、 websphere等应用服务器。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;JEECMS并不是一个只追求技术之先进，而不考虑用户实际使用的象牙塔CMS。系统的设计宗旨就是从用户的需求出发，提供最便利、合理的使用方式，懂html就能建站，从设计上满足搜索引擎优化，最小性能消耗满足小网站要求、可扩展群集满足大网站需要。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;很多人觉得java、jsp难掌握，技术门槛高。jeecms具有强大的模板机制。所有前台页面均由模板生成，通过在线编辑模板轻松调整页面显示。模板内容不涉及任何java和jsp技术，只需掌握html语法和jeecms标签即可完成动态网页制作。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;强大、灵活的标签。提供两种风格的标签，一种风格的标签封装了大量互联网上常见的显示样式，通过调整参数就可实现文章列表、图文混排、图文滚动、跑马灯、焦点图等效果。这种标签的优势在于页面制作简单、效率高，对js、css、html不够精通和希望快速建站的用户非常适用。并且各种效果的内容不使用js生成，对搜索引擎非常友好。另一种风格的标签只负责读取数据，由用户自己控制显示内容和显示方式，想到什么就能做到什么，对于技术能力高和追求个性化的用户，可谓如鱼得水。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;采用完全生成静态页面技术，加快页面访问速度，提升搜索引擎友好性；采用扁平的、可自定义的路径结构。对于有特别需求者，可自定义页面后缀，如.php,.asp,.aspx等。</font></p>\r\n<p><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;站群设计，对于大型的网站，往往需要通过次级域名建立子站群，各个子站后台管理权限可以分离，程序和附件分离，前台用户实现单点登录，大规模网站轻松建设。</font></p>',NULL,NULL,NULL),
 (40,'<p><strong>瑞莱全劲健康食品贸易（上海）有限公司</strong></p>\r\n<p>&nbsp;</p>\r\n<p>上海市浦东新区严桥路326号3楼 邮编：200125</p>\r\n<p>电话：021-51962822</p>\r\n<p>传真：021-51962823</p>\r\n<p><iframe width=\"600\" scrolling=\"no\" height=\"350\" frameborder=\"0\" src=\"http://ditu.google.cn/maps?f=q&amp;source=s_q&amp;hl=zh-CN&amp;geocode=&amp;q=%E4%B8%8A%E6%B5%B7%E5%B8%82%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA%E4%B8%A5%E6%A1%A5%E8%B7%AF326%E5%8F%B73%E6%A5%BC&amp;sll=31.18673,121.528944&amp;sspn=0.010647,0.022638&amp;g=%E4%B8%8A%E6%B5%B7%E5%B8%82%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA%E4%B8%A5%E6%A1%A5%E8%B7%AF326%E5%8F%B73%E6%A5%BC&amp;brcurrent=3,0x35b27a4f25879eb1:0xbd6cd4370b6f9c12,0,0x35ad8c73cd3952c7:0xbb190e9364c4e592%3B5,0,0&amp;ie=UTF8&amp;hq=&amp;hnear=%E4%B8%8A%E6%B5%B7%E5%B8%82%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA%E4%B8%A5%E6%A1%A5%E8%B7%AF326%E5%8F%B7&amp;ll=31.194154,121.531363&amp;spn=0.025697,0.051498&amp;z=14&amp;iwloc=A&amp;output=embed\" marginwidth=\"0\" marginheight=\"0\"></iframe><br />\r\n<small><a style=\"color: #0000ff; text-align: left;\" href=\"http://ditu.google.cn/maps?f=q&amp;source=embed&amp;hl=zh-CN&amp;geocode=&amp;q=%E4%B8%8A%E6%B5%B7%E5%B8%82%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA%E4%B8%A5%E6%A1%A5%E8%B7%AF326%E5%8F%B73%E6%A5%BC&amp;sll=31.18673,121.528944&amp;sspn=0.010647,0.022638&amp;g=%E4%B8%8A%E6%B5%B7%E5%B8%82%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA%E4%B8%A5%E6%A1%A5%E8%B7%AF326%E5%8F%B73%E6%A5%BC&amp;brcurrent=3,0x35b27a4f25879eb1:0xbd6cd4370b6f9c12,0,0x35ad8c73cd3952c7:0xbb190e9364c4e592%3B5,0,0&amp;ie=UTF8&amp;hq=&amp;hnear=%E4%B8%8A%E6%B5%B7%E5%B8%82%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA%E4%B8%A5%E6%A1%A5%E8%B7%AF326%E5%8F%B7&amp;ll=31.194154,121.531363&amp;spn=0.025697,0.051498&amp;z=14&amp;iwloc=A\">查看大图</a></small><br />\r\n<br />\r\n&nbsp;</p>\r\n<p><strong>瑞莱全劲食品贸易（北京）有限公司</strong><br />\r\n北京市朝阳区酒仙桥中路26号新华联丽港B座316  邮编：100016</p>\r\n<p>电话：010-64360459</p>\r\n<p>传真：010-64369269</p>\r\n<p>热线电话：400-600-3469</p>\r\n<p><iframe width=\"600\" scrolling=\"no\" height=\"350\" frameborder=\"0\" src=\"http://ditu.google.cn/maps?f=q&amp;source=s_q&amp;hl=zh-CN&amp;geocode=&amp;q=%E5%8C%97%E4%BA%AC%E5%B8%82%E6%9C%9D%E9%98%B3%E5%8C%BA%E9%85%92%E4%BB%99%E6%A1%A5%E4%B8%AD%E8%B7%AF26%E5%8F%B7%E6%96%B0%E5%8D%8E%E8%81%94%E4%B8%BD%E6%B8%AFB%E5%BA%A7316&amp;sll=31.18672,121.528959&amp;sspn=0.010647,0.022638&amp;brcurrent=3,0x35f1ab7ca300f109:0x54c75ac2e5cb445d,0,0x35f1abee23736947:0xd7bb8b3026d0813a%3B5,0,0&amp;ie=UTF8&amp;hq=%E6%96%B0%E5%8D%8E%E8%81%94%E4%B8%BD%E6%B8%AF&amp;hnear=%E5%8C%97%E4%BA%AC%E5%B8%82%E6%9C%9D%E9%98%B3%E5%8C%BA%E9%85%92%E4%BB%99%E6%A1%A5%E4%B8%AD%E8%B7%AF26%E5%8F%B7%E6%96%B0%E5%8D%8E%E8%81%94%E4%B8%BD%E6%B8%AF&amp;ll=39.982776,116.500826&amp;spn=0.023018,0.051498&amp;z=14&amp;iwloc=A&amp;output=embed\" marginwidth=\"0\" marginheight=\"0\"></iframe><br />\r\n<small><a style=\"color: #0000ff; text-align: left;\" href=\"http://ditu.google.cn/maps?f=q&amp;source=embed&amp;hl=zh-CN&amp;geocode=&amp;q=%E5%8C%97%E4%BA%AC%E5%B8%82%E6%9C%9D%E9%98%B3%E5%8C%BA%E9%85%92%E4%BB%99%E6%A1%A5%E4%B8%AD%E8%B7%AF26%E5%8F%B7%E6%96%B0%E5%8D%8E%E8%81%94%E4%B8%BD%E6%B8%AFB%E5%BA%A7316&amp;sll=31.18672,121.528959&amp;sspn=0.010647,0.022638&amp;brcurrent=3,0x35f1ab7ca300f109:0x54c75ac2e5cb445d,0,0x35f1abee23736947:0xd7bb8b3026d0813a%3B5,0,0&amp;ie=UTF8&amp;hq=%E6%96%B0%E5%8D%8E%E8%81%94%E4%B8%BD%E6%B8%AF&amp;hnear=%E5%8C%97%E4%BA%AC%E5%B8%82%E6%9C%9D%E9%98%B3%E5%8C%BA%E9%85%92%E4%BB%99%E6%A1%A5%E4%B8%AD%E8%B7%AF26%E5%8F%B7%E6%96%B0%E5%8D%8E%E8%81%94%E4%B8%BD%E6%B8%AF&amp;ll=39.982776,116.500826&amp;spn=0.023018,0.051498&amp;z=14&amp;iwloc=A\">查看大图</a></small></p>\r\n<p><br />\r\n&nbsp;</p>\r\n<p>&nbsp;</p>\r\n<p><strong>广州瑞莱全劲健康食品贸易有限公司</strong></p>\r\n<p>广州市天河区林乐路39-49号中旅商务大厦东塔25D 邮编：510610</p>\r\n<p>电话：020-38823249<br />\r\n传真：020-38823249</p>\r\n<p>热线电话：400-882-8875</p>\r\n<p><iframe width=\"600\" scrolling=\"no\" height=\"350\" frameborder=\"0\" src=\"http://ditu.google.cn/maps?f=q&amp;source=s_q&amp;hl=zh-CN&amp;geocode=&amp;q=%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E6%9E%97%E4%B9%90%E8%B7%AF39-49%E5%8F%B7%E4%B8%AD%E6%97%85%E5%95%86%E5%8A%A1%E5%A4%A7%E5%8E%A6%E4%B8%9C%E5%A1%9425D&amp;sll=39.976989,116.492157&amp;sspn=0.036504,0.090551&amp;brcurrent=3,0x3402fe5428850f7b:0xd78d5d77462a0b60,0,0x340301fe46c655a3:0xc549ef142225757a%3B5,0,0&amp;ie=UTF8&amp;hq=%E6%9E%97%E4%B9%90%E8%B7%AF39-49%E5%8F%B7%E4%B8%AD%E6%97%85%E5%95%86%E5%8A%A1%E5%A4%A7%E5%8E%A6%E4%B8%9C%E5%A1%9425D&amp;hnear=%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA&amp;ll=23.151804,113.33333&amp;spn=0.027621,0.051498&amp;z=14&amp;iwloc=A&amp;output=embed\" marginwidth=\"0\" marginheight=\"0\"></iframe><br />\r\n<small><a style=\"color: #0000ff; text-align: left;\" href=\"http://ditu.google.cn/maps?f=q&amp;source=embed&amp;hl=zh-CN&amp;geocode=&amp;q=%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E6%9E%97%E4%B9%90%E8%B7%AF39-49%E5%8F%B7%E4%B8%AD%E6%97%85%E5%95%86%E5%8A%A1%E5%A4%A7%E5%8E%A6%E4%B8%9C%E5%A1%9425D&amp;sll=39.976989,116.492157&amp;sspn=0.036504,0.090551&amp;brcurrent=3,0x3402fe5428850f7b:0xd78d5d77462a0b60,0,0x340301fe46c655a3:0xc549ef142225757a%3B5,0,0&amp;ie=UTF8&amp;hq=%E6%9E%97%E4%B9%90%E8%B7%AF39-49%E5%8F%B7%E4%B8%AD%E6%97%85%E5%95%86%E5%8A%A1%E5%A4%A7%E5%8E%A6%E4%B8%9C%E5%A1%9425D&amp;hnear=%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA&amp;ll=23.151804,113.33333&amp;spn=0.027621,0.051498&amp;z=14&amp;iwloc=A\">查看大图</a></small></p>',NULL,NULL,NULL);
/*!40000 ALTER TABLE `jc_channel_txt` ENABLE KEYS */;


--
-- Definition of table `jc_channel_user`
--

DROP TABLE IF EXISTS `jc_channel_user`;
CREATE TABLE `jc_channel_user` (
  `channel_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`user_id`),
  KEY `fk_jc_channel_user` (`user_id`),
  CONSTRAINT `fk_jc_channel_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
  CONSTRAINT `fk_jc_user_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目用户关联表';

--
-- Dumping data for table `jc_channel_user`
--

/*!40000 ALTER TABLE `jc_channel_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_channel_user` ENABLE KEYS */;


--
-- Definition of table `jc_chnl_group_contri`
--

DROP TABLE IF EXISTS `jc_chnl_group_contri`;
CREATE TABLE `jc_chnl_group_contri` (
  `channel_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`group_id`),
  KEY `fk_jc_channel_group_c` (`group_id`),
  CONSTRAINT `fk_jc_channel_group_c` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`),
  CONSTRAINT `fk_jc_group_channel_c` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目投稿会员组关联表';

--
-- Dumping data for table `jc_chnl_group_contri`
--

/*!40000 ALTER TABLE `jc_chnl_group_contri` DISABLE KEYS */;
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES 
 (43,1),
 (43,2);
/*!40000 ALTER TABLE `jc_chnl_group_contri` ENABLE KEYS */;


--
-- Definition of table `jc_chnl_group_view`
--

DROP TABLE IF EXISTS `jc_chnl_group_view`;
CREATE TABLE `jc_chnl_group_view` (
  `channel_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`group_id`),
  KEY `fk_jc_channel_group_v` (`group_id`),
  CONSTRAINT `fk_jc_channel_group_v` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`),
  CONSTRAINT `fk_jc_group_channel_v` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目浏览会员组关联表';

--
-- Dumping data for table `jc_chnl_group_view`
--

/*!40000 ALTER TABLE `jc_chnl_group_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_chnl_group_view` ENABLE KEYS */;


--
-- Definition of table `jc_comment`
--

DROP TABLE IF EXISTS `jc_comment`;
CREATE TABLE `jc_comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_user_id` int(11) DEFAULT NULL COMMENT '评论用户ID',
  `reply_user_id` int(11) DEFAULT NULL COMMENT '回复用户ID',
  `content_id` int(11) NOT NULL COMMENT '内容ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `ups` smallint(6) NOT NULL DEFAULT '0' COMMENT '支持数',
  `downs` smallint(6) NOT NULL DEFAULT '0' COMMENT '反对数',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐',
  `is_checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否审核',
  PRIMARY KEY (`comment_id`),
  KEY `fk_jc_comment_content` (`content_id`),
  KEY `fk_jc_comment_reply` (`reply_user_id`),
  KEY `fk_jc_comment_site` (`site_id`),
  KEY `fk_jc_comment_user` (`comment_user_id`),
  CONSTRAINT `fk_jc_comment_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_comment_reply` FOREIGN KEY (`reply_user_id`) REFERENCES `jc_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_comment_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_comment_user` FOREIGN KEY (`comment_user_id`) REFERENCES `jc_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='CMS评论表';

--
-- Dumping data for table `jc_comment`
--

/*!40000 ALTER TABLE `jc_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_comment` ENABLE KEYS */;


--
-- Definition of table `jc_comment_ext`
--

DROP TABLE IF EXISTS `jc_comment_ext`;
CREATE TABLE `jc_comment_ext` (
  `comment_id` int(11) NOT NULL,
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `text` longtext COMMENT '评论内容',
  `reply` longtext COMMENT '回复内容',
  KEY `fk_jc_ext_comment` (`comment_id`),
  CONSTRAINT `fk_jc_ext_comment` FOREIGN KEY (`comment_id`) REFERENCES `jc_comment` (`comment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS评论扩展表';

--
-- Dumping data for table `jc_comment_ext`
--

/*!40000 ALTER TABLE `jc_comment_ext` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_comment_ext` ENABLE KEYS */;


--
-- Definition of table `jc_config`
--

DROP TABLE IF EXISTS `jc_config`;
CREATE TABLE `jc_config` (
  `config_id` int(11) NOT NULL,
  `context_path` varchar(20) DEFAULT '/JeeCms' COMMENT '部署路径',
  `servlet_point` varchar(20) DEFAULT NULL COMMENT 'Servlet挂载点',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `db_file_uri` varchar(50) NOT NULL DEFAULT '/dbfile.svl?n=' COMMENT '数据库附件访问地址',
  `is_upload_to_db` tinyint(1) NOT NULL DEFAULT '0' COMMENT '上传附件至数据库',
  `def_img` varchar(255) NOT NULL DEFAULT '/JeeCms/r/cms/www/default/no_picture.gif' COMMENT '图片不存在时默认图片',
  `login_url` varchar(255) NOT NULL DEFAULT '/login.jspx' COMMENT '登录地址',
  `process_url` varchar(255) DEFAULT NULL COMMENT '登录后处理地址',
  `mark_on` tinyint(1) NOT NULL DEFAULT '1' COMMENT '开启图片水印',
  `mark_width` int(11) NOT NULL DEFAULT '120' COMMENT '图片最小宽度',
  `mark_height` int(11) NOT NULL DEFAULT '120' COMMENT '图片最小高度',
  `mark_image` varchar(100) DEFAULT '/r/cms/www/watermark.png' COMMENT '图片水印',
  `mark_content` varchar(100) NOT NULL DEFAULT 'www.jeecms.com' COMMENT '文字水印内容',
  `mark_size` int(11) NOT NULL DEFAULT '20' COMMENT '文字水印大小',
  `mark_color` varchar(10) NOT NULL DEFAULT '#FF0000' COMMENT '文字水印颜色',
  `mark_alpha` int(11) NOT NULL DEFAULT '50' COMMENT '水印透明度（0-100）',
  `mark_position` int(11) NOT NULL DEFAULT '1' COMMENT '水印位置(0-5)',
  `mark_offset_x` int(11) NOT NULL DEFAULT '0' COMMENT 'x坐标偏移量',
  `mark_offset_y` int(11) NOT NULL DEFAULT '0' COMMENT 'y坐标偏移量',
  `count_clear_time` date NOT NULL COMMENT '计数器清除时间',
  `count_copy_time` datetime NOT NULL COMMENT '计数器拷贝时间',
  `download_code` varchar(32) NOT NULL DEFAULT 'jeecms' COMMENT '下载防盗链md5混淆码',
  `download_time` int(11) NOT NULL DEFAULT '12' COMMENT '下载有效时间（小时）',
  `email_host` varchar(50) DEFAULT NULL COMMENT '邮件发送服务器',
  `email_encoding` varchar(20) DEFAULT NULL COMMENT '邮件发送编码',
  `email_username` varchar(100) DEFAULT NULL COMMENT '邮箱用户名',
  `email_password` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `email_personal` varchar(100) DEFAULT NULL COMMENT '邮箱发件人',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS配置表';

--
-- Dumping data for table `jc_config`
--

/*!40000 ALTER TABLE `jc_config` DISABLE KEYS */;
INSERT INTO `jc_config` (`config_id`,`context_path`,`servlet_point`,`port`,`db_file_uri`,`is_upload_to_db`,`def_img`,`login_url`,`process_url`,`mark_on`,`mark_width`,`mark_height`,`mark_image`,`mark_content`,`mark_size`,`mark_color`,`mark_alpha`,`mark_position`,`mark_offset_x`,`mark_offset_y`,`count_clear_time`,`count_copy_time`,`download_code`,`download_time`,`email_host`,`email_encoding`,`email_username`,`email_password`,`email_personal`) VALUES 
 (1,NULL,NULL,80,'/dbfile.svl?n=',0,'/r/cms/www/no_picture.gif','/login.jspx',NULL,1,120,120,'/r/cms/www/watermark.png','www.jeecms.com',20,'#FF0000',50,1,0,0,'2011-09-18','2011-09-18 22:54:10','jeecms',12,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `jc_config` ENABLE KEYS */;


--
-- Definition of table `jc_config_attr`
--

DROP TABLE IF EXISTS `jc_config_attr`;
CREATE TABLE `jc_config_attr` (
  `config_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_config` (`config_id`),
  CONSTRAINT `fk_jc_attr_config` FOREIGN KEY (`config_id`) REFERENCES `jc_config` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS配置属性表';

--
-- Dumping data for table `jc_config_attr`
--

/*!40000 ALTER TABLE `jc_config_attr` DISABLE KEYS */;
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES 
 (1,'password_min_len','3'),
 (1,'username_reserved',''),
 (1,'register_on','true'),
 (1,'member_on','true'),
 (1,'username_min_len','3'),
 (1,'version','jeecms-3.1.1-final');
/*!40000 ALTER TABLE `jc_config_attr` ENABLE KEYS */;


--
-- Definition of table `jc_content`
--

DROP TABLE IF EXISTS `jc_content`;
CREATE TABLE `jc_content` (
  `content_id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) NOT NULL COMMENT '栏目ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `type_id` int(11) NOT NULL COMMENT '属性ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `sort_date` datetime NOT NULL COMMENT '排序日期',
  `top_level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '固顶级别',
  `has_title_img` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有标题图',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '状态(0:草稿;1:审核中;2:审核通过;3:回收站)',
  `views_day` int(11) NOT NULL DEFAULT '0' COMMENT '日访问数',
  `comments_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日评论数',
  `downloads_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日下载数',
  `ups_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日顶数',
  PRIMARY KEY (`content_id`),
  KEY `fk_jc_content_site` (`site_id`),
  KEY `fk_jc_content_type` (`type_id`),
  KEY `fk_jc_content_user` (`user_id`),
  KEY `fk_jc_contentchannel` (`channel_id`),
  CONSTRAINT `fk_jc_contentchannel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_content_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_content_type` FOREIGN KEY (`type_id`) REFERENCES `jc_content_type` (`type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_content_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8 COMMENT='CMS内容表';

--
-- Dumping data for table `jc_content`
--

/*!40000 ALTER TABLE `jc_content` DISABLE KEYS */;
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`) VALUES 
 (190,43,1,1,1,'2011-09-18 18:24:37',0,0,0,2,0,0,0,0),
 (191,44,1,1,1,'2011-09-18 18:31:07',0,1,0,2,2,0,0,0);
/*!40000 ALTER TABLE `jc_content` ENABLE KEYS */;


--
-- Definition of table `jc_content_attachment`
--

DROP TABLE IF EXISTS `jc_content_attachment`;
CREATE TABLE `jc_content_attachment` (
  `content_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL COMMENT '排列顺序',
  `attachment_path` varchar(255) NOT NULL COMMENT '附件路径',
  `attachment_name` varchar(100) NOT NULL COMMENT '附件名称',
  `filename` varchar(100) DEFAULT NULL COMMENT '文件名',
  `download_count` int(11) NOT NULL DEFAULT '0' COMMENT '下载次数',
  KEY `fk_jc_attachment_content` (`content_id`),
  CONSTRAINT `fk_jc_attachment_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容附件表';

--
-- Dumping data for table `jc_content_attachment`
--

/*!40000 ALTER TABLE `jc_content_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_content_attachment` ENABLE KEYS */;


--
-- Definition of table `jc_content_attr`
--

DROP TABLE IF EXISTS `jc_content_attr`;
CREATE TABLE `jc_content_attr` (
  `content_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_content` (`content_id`),
  CONSTRAINT `fk_jc_attr_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容扩展属性表';

--
-- Dumping data for table `jc_content_attr`
--

/*!40000 ALTER TABLE `jc_content_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_content_attr` ENABLE KEYS */;


--
-- Definition of table `jc_content_channel`
--

DROP TABLE IF EXISTS `jc_content_channel`;
CREATE TABLE `jc_content_channel` (
  `channel_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`content_id`),
  KEY `fk_jc_channel_content` (`content_id`),
  CONSTRAINT `fk_jc_channel_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_jc_content_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容栏目关联表';

--
-- Dumping data for table `jc_content_channel`
--

/*!40000 ALTER TABLE `jc_content_channel` DISABLE KEYS */;
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES 
 (43,190),
 (44,191);
/*!40000 ALTER TABLE `jc_content_channel` ENABLE KEYS */;


--
-- Definition of table `jc_content_check`
--

DROP TABLE IF EXISTS `jc_content_check`;
CREATE TABLE `jc_content_check` (
  `content_id` int(11) NOT NULL,
  `check_step` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审核步数',
  `check_opinion` varchar(255) DEFAULT NULL COMMENT '审核意见',
  `is_rejected` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否退回',
  PRIMARY KEY (`content_id`),
  CONSTRAINT `fk_jc_check_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容审核信息表';

--
-- Dumping data for table `jc_content_check`
--

/*!40000 ALTER TABLE `jc_content_check` DISABLE KEYS */;
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`) VALUES 
 (190,2,NULL,0),
 (191,2,NULL,0);
/*!40000 ALTER TABLE `jc_content_check` ENABLE KEYS */;


--
-- Definition of table `jc_content_count`
--

DROP TABLE IF EXISTS `jc_content_count`;
CREATE TABLE `jc_content_count` (
  `content_id` int(11) NOT NULL,
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '总访问数',
  `views_month` int(11) NOT NULL DEFAULT '0' COMMENT '月访问数',
  `views_week` int(11) NOT NULL DEFAULT '0' COMMENT '周访问数',
  `views_day` int(11) NOT NULL DEFAULT '0' COMMENT '日访问数',
  `comments` int(11) NOT NULL DEFAULT '0' COMMENT '总评论数',
  `comments_month` int(11) NOT NULL DEFAULT '0' COMMENT '月评论数',
  `comments_week` smallint(6) NOT NULL DEFAULT '0' COMMENT '周评论数',
  `comments_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日评论数',
  `downloads` int(11) NOT NULL DEFAULT '0' COMMENT '总下载数',
  `downloads_month` int(11) NOT NULL DEFAULT '0' COMMENT '月下载数',
  `downloads_week` smallint(6) NOT NULL DEFAULT '0' COMMENT '周下载数',
  `downloads_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日下载数',
  `ups` int(11) NOT NULL DEFAULT '0' COMMENT '总顶数',
  `ups_month` int(11) NOT NULL DEFAULT '0' COMMENT '月顶数',
  `ups_week` smallint(6) NOT NULL DEFAULT '0' COMMENT '周顶数',
  `ups_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日顶数',
  `downs` int(11) NOT NULL DEFAULT '0' COMMENT '总踩数',
  PRIMARY KEY (`content_id`),
  CONSTRAINT `fk_jc_count_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容计数表';

--
-- Dumping data for table `jc_content_count`
--

/*!40000 ALTER TABLE `jc_content_count` DISABLE KEYS */;
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES 
 (190,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
 (191,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
/*!40000 ALTER TABLE `jc_content_count` ENABLE KEYS */;


--
-- Definition of table `jc_content_ext`
--

DROP TABLE IF EXISTS `jc_content_ext`;
CREATE TABLE `jc_content_ext` (
  `content_id` int(11) NOT NULL,
  `title` varchar(150) NOT NULL COMMENT '标题',
  `short_title` varchar(150) DEFAULT NULL COMMENT '简短标题',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `origin` varchar(100) DEFAULT NULL COMMENT '来源',
  `origin_url` varchar(255) DEFAULT NULL COMMENT '来源链接',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `release_date` datetime NOT NULL COMMENT '发布日期',
  `media_path` varchar(255) DEFAULT NULL COMMENT '媒体路径',
  `media_type` varchar(20) DEFAULT NULL COMMENT '媒体类型',
  `title_color` varchar(10) DEFAULT NULL COMMENT '标题颜色',
  `is_bold` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否加粗',
  `title_img` varchar(100) DEFAULT NULL COMMENT '标题图片',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图片',
  `type_img` varchar(100) DEFAULT NULL COMMENT '类型图片',
  `link` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '指定模板',
  PRIMARY KEY (`content_id`),
  CONSTRAINT `fk_jc_ext_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容扩展表';

--
-- Dumping data for table `jc_content_ext`
--

/*!40000 ALTER TABLE `jc_content_ext` DISABLE KEYS */;
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`) VALUES 
 (190,'中医说：医食同源，天然是人类最好的医生 ',NULL,NULL,NULL,NULL,NULL,'2011-09-18 18:24:37',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL),
 (191,'蛋白质粉伴侣 ',NULL,NULL,NULL,NULL,NULL,'2011-09-18 18:31:07',NULL,NULL,NULL,0,'http://www.hkshky.com/uploadmyfile/20110616115241.jpg',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `jc_content_ext` ENABLE KEYS */;


--
-- Definition of table `jc_content_group_view`
--

DROP TABLE IF EXISTS `jc_content_group_view`;
CREATE TABLE `jc_content_group_view` (
  `content_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`content_id`,`group_id`),
  KEY `fk_jc_content_group_v` (`group_id`),
  CONSTRAINT `fk_jc_content_group_v` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`),
  CONSTRAINT `fk_jc_group_content_v` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容浏览会员组关联表';

--
-- Dumping data for table `jc_content_group_view`
--

/*!40000 ALTER TABLE `jc_content_group_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_content_group_view` ENABLE KEYS */;


--
-- Definition of table `jc_content_picture`
--

DROP TABLE IF EXISTS `jc_content_picture`;
CREATE TABLE `jc_content_picture` (
  `content_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL COMMENT '排列顺序',
  `img_path` varchar(100) NOT NULL COMMENT '图片地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`content_id`,`priority`),
  CONSTRAINT `fk_jc_picture_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容图片表';

--
-- Dumping data for table `jc_content_picture`
--

/*!40000 ALTER TABLE `jc_content_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_content_picture` ENABLE KEYS */;


--
-- Definition of table `jc_content_tag`
--

DROP TABLE IF EXISTS `jc_content_tag`;
CREATE TABLE `jc_content_tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL COMMENT 'tag名称',
  `ref_counter` int(11) NOT NULL DEFAULT '1' COMMENT '被引用的次数',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `ak_tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COMMENT='CMS内容TAG表';

--
-- Dumping data for table `jc_content_tag`
--

/*!40000 ALTER TABLE `jc_content_tag` DISABLE KEYS */;
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES 
 (1,'2011',6),
 (2,'中国',1),
 (3,'货币',1),
 (4,'信贷',1),
 (5,'楼市',2),
 (6,'限购令',1),
 (7,'人民币',1),
 (8,'升值',1),
 (9,'机构',2),
 (10,'CPI',1),
 (11,'回落',1),
 (12,'资金',1),
 (13,'净流出',1),
 (14,'桑德',1),
 (15,'投“靶”',1),
 (16,'基金',4),
 (17,'资管',1),
 (18,'回报',1),
 (19,'A股',1),
 (20,'债基',1),
 (21,'绩效',1),
 (22,'上市',1),
 (23,'年前',1),
 (24,'加仓',1),
 (25,'寻找',1),
 (26,'生财之道',1),
 (27,'理财',1),
 (28,'赚钱',1),
 (29,'个税',1),
 (30,'改革',2),
 (31,'政策',4),
 (32,'2010',1),
 (33,'美元',1),
 (34,'欧元',1),
 (35,'财险',1),
 (36,'保费',1),
 (37,'周鸿祎',1),
 (38,'英雄',1),
 (39,'商人',1),
 (40,'汤玉祥',1),
 (41,'轿车',1),
 (42,'Facebook',1),
 (43,'投资',1),
 (44,'新年',1),
 (45,'李开复',1),
 (46,'李瑜',1),
 (47,'王建硕',1),
 (48,'网站',1),
 (49,'规模化',1),
 (50,'沈阳',1),
 (51,'房价',1),
 (52,'调控',2),
 (53,'房产税',1),
 (54,'分歧',1),
 (55,'评估',1),
 (56,'房产',1),
 (57,'成交',1),
 (58,'房地产',1),
 (59,'策略',1),
 (60,'户型',1),
 (61,'花都雅居乐花园',1),
 (62,'卡布奇诺',1),
 (63,'公寓',1),
 (64,'君华香柏广场',1),
 (65,'巴亚莫',1),
 (66,'别墅',1),
 (67,'低碳',1),
 (68,'收纳箱',1),
 (69,'餐桌',1),
 (70,'地板',1),
 (71,'订单',1),
 (72,'利润',1),
 (73,'衣柜',1),
 (74,'重压力',1);
/*!40000 ALTER TABLE `jc_content_tag` ENABLE KEYS */;


--
-- Definition of table `jc_content_topic`
--

DROP TABLE IF EXISTS `jc_content_topic`;
CREATE TABLE `jc_content_topic` (
  `content_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  PRIMARY KEY (`content_id`,`topic_id`),
  KEY `fk_jc_content_topic` (`topic_id`),
  CONSTRAINT `fk_jc_content_topic` FOREIGN KEY (`topic_id`) REFERENCES `jc_topic` (`topic_id`),
  CONSTRAINT `fk_jc_topic_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS专题内容关联表';

--
-- Dumping data for table `jc_content_topic`
--

/*!40000 ALTER TABLE `jc_content_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_content_topic` ENABLE KEYS */;


--
-- Definition of table `jc_content_txt`
--

DROP TABLE IF EXISTS `jc_content_txt`;
CREATE TABLE `jc_content_txt` (
  `content_id` int(11) NOT NULL,
  `txt` longtext COMMENT '文章内容',
  `txt1` longtext COMMENT '扩展内容1',
  `txt2` longtext COMMENT '扩展内容2',
  `txt3` longtext COMMENT '扩展内容3',
  PRIMARY KEY (`content_id`),
  CONSTRAINT `fk_jc_txt_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容文本表';

--
-- Dumping data for table `jc_content_txt`
--

/*!40000 ALTER TABLE `jc_content_txt` DISABLE KEYS */;
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES 
 (190,'<p><span>&ldquo;医食同源&rdquo;是中医的一种习惯说法。意思是说，医药的知识和饮食的知识，是来自同一个起源的。我国古代有句俗话，叫做&ldquo;民以食为本&rdquo;。原始人曾经历过渔猎时期、农耕时期。当时人们还不会培植各种有用的植物，也不会驯养各种有用的动物，所以只能吃自然界现成的食物。<br />\r\n&nbsp;&nbsp;&nbsp;&nbsp;那 时候的原始人，疾病是不少的，尤其是由于吃了不清洁、难以消化的食物而引起的疾病，如肠胃病等时有发生。然而，也就是在这个过程中，人们也发现了一些能治 疗人体不适的食物。比如说，野葱、姜、蒜等，都是具有一定医疗作用的食物。葱白吃了会发汗，姜吃了能去除受寒所引起的疾病，像关节疼痛、胃痛、感冒等等。<br />\r\n&nbsp;&nbsp;&nbsp;&nbsp;中 医最常用的剂型是汤药，就是把几味中药放在水中熬，然后服汤。晋朝皇甫谧的《甲乙经》就说过：&ldquo;伊尹&rdquo;采用神农本草以为汤液&rdquo;。伊尹是商朝汤王的臣子，专 管厨房的烹调，他有一手好的烹调手艺。伊尹做的菜汤，既可做菜肴，也可做治病的汤药。也就是说，伊尹既是厨师，也是医师，集二职于一身，从他身上充分体现 出&ldquo;医食同源&rdquo;的传说是可靠的。<br />\r\n&nbsp;&nbsp;&nbsp;&nbsp;再比如，张仲景的《伤寒杂病论》中的药方，像桂枝汤中共五味药，即桂枝、白芍、生姜、大枣、甘草，其中后 三味都是厨房常用的调味品和食物。其它如川椒、茴香、酒等等都是既可以调味，又可以入药的东西。有些既可在药店买到，又可以在食品店买到，如胡桃、桂圆、 大枣、桂皮&hellip;&hellip;你很难严格把它们区分开，究竟是药物还是食物。这也在一定程度上说明了&ldquo;医食同源&rdquo;的道理。   </span></p>',NULL,NULL,NULL),
 (191,'<p><font size=\"1\">&nbsp; </font><font size=\"2\">本品针对单独补充蛋白质粉时，蛋白质不为人体充分吸收而特别研制的独特配方，富含植物膳食纤维以延长蛋白质在人体的吸收过程，高赖氨酸、钙、VE以帮助蛋白质的利用、转化和各营养素的合成。</font></p>\r\n<div><font size=\"2\" color=\"#ff0033\">不添加人造香精、色素或防腐剂，安全自然健康</font></div>\r\n<div><font size=\"2\">A：膳食纤维可帮助蛋白质在人体内充分的吸收</font></div>\r\n<div><font size=\"2\">大量研究表明：人体在空腹补充蛋白质粉时，蛋白质很大部分并没有被人体吸收利用，而是在体内转化成有害的氨气，人体在补充蛋白质粉的同时，必须要补充膳食纤维来配合蛋白质在人体内的吸收，才利于蛋白质很好的被人体充分吸收利用</font></div>\r\n<div><font size=\"2\">B：赖氨酸协助蛋白质的合成利用及吸收。</font></div>\r\n<div><font size=\"2\">赖氨酸是帮助其它营养物质被人体充分吸收和利用的关键物质,人体只有补充了足够的L-赖氨酸才能提高食物蛋白质的吸收和利用,达到均衡营养,促进生长发育.其作用有: <br />\r\n1 ,提高智力,促进生长,增强体质.&nbsp;</font></div>\r\n<div><font size=\"2\">2 ,增进食欲,改善营养不良状况. <br />\r\n3 ,改善失眠,提高记忆力.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></div>\r\n<div><font size=\"2\">4 ,帮助产生抗体,激素和酶,提高免疫力,增加血色素. <br />\r\n5 ,帮助钙的吸收,治疗防止骨质疏松症&nbsp;</font></div>\r\n<div><font size=\"2\">6 ,降低血中甘油三酯的水平,预防心脑血管疾病的产生.</font></div>\r\n<div><font size=\"2\">C：钙与蛋白质是非常亲近的朋友，</font></div>\r\n<div><font size=\"2\">D：天然的小麦胚芽维生素E对生物膜及蛋白质的合成起到重要的作用</font></div>\r\n<div><font size=\"2\" color=\"#ff0033\">食用方法：蛋白质粉、蛋白质粉伴侣各一勺，加适量温开水冲开搅拌均匀即可饮用</font></div>',NULL,NULL,NULL);
/*!40000 ALTER TABLE `jc_content_txt` ENABLE KEYS */;


--
-- Definition of table `jc_content_type`
--

DROP TABLE IF EXISTS `jc_content_type`;
CREATE TABLE `jc_content_type` (
  `type_id` int(11) NOT NULL,
  `type_name` varchar(20) NOT NULL COMMENT '名称',
  `img_width` int(11) DEFAULT NULL COMMENT '图片宽',
  `img_height` int(11) DEFAULT NULL COMMENT '图片高',
  `has_image` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有图片',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容类型表';

--
-- Dumping data for table `jc_content_type`
--

/*!40000 ALTER TABLE `jc_content_type` DISABLE KEYS */;
INSERT INTO `jc_content_type` (`type_id`,`type_name`,`img_width`,`img_height`,`has_image`,`is_disabled`) VALUES 
 (1,'普通',100,100,0,0),
 (2,'图文',143,98,1,0),
 (3,'焦点',280,200,1,0),
 (4,'头条',0,0,0,0);
/*!40000 ALTER TABLE `jc_content_type` ENABLE KEYS */;


--
-- Definition of table `jc_contenttag`
--

DROP TABLE IF EXISTS `jc_contenttag`;
CREATE TABLE `jc_contenttag` (
  `content_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  KEY `fk_jc_content_tag` (`tag_id`),
  KEY `fk_jc_tag_content` (`content_id`),
  CONSTRAINT `fk_jc_content_tag` FOREIGN KEY (`tag_id`) REFERENCES `jc_content_tag` (`tag_id`),
  CONSTRAINT `fk_jc_tag_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容标签关联表';

--
-- Dumping data for table `jc_contenttag`
--

/*!40000 ALTER TABLE `jc_contenttag` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_contenttag` ENABLE KEYS */;


--
-- Definition of table `jc_friendlink`
--

DROP TABLE IF EXISTS `jc_friendlink`;
CREATE TABLE `jc_friendlink` (
  `friendlink_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `friendlinkctg_id` int(11) NOT NULL,
  `site_name` varchar(150) NOT NULL COMMENT '网站名称',
  `domain` varchar(255) NOT NULL COMMENT '网站地址',
  `logo` varchar(150) DEFAULT NULL COMMENT '图标',
  `email` varchar(100) DEFAULT NULL COMMENT '站长邮箱',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '点击次数',
  `is_enabled` char(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`friendlink_id`),
  KEY `fk_jc_ctg_friendlink` (`friendlinkctg_id`),
  KEY `fk_jc_friendlink_site` (`site_id`),
  CONSTRAINT `fk_jc_ctg_friendlink` FOREIGN KEY (`friendlinkctg_id`) REFERENCES `jc_friendlink_ctg` (`friendlinkctg_id`),
  CONSTRAINT `fk_jc_friendlink_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='CMS友情链接';

--
-- Dumping data for table `jc_friendlink`
--

/*!40000 ALTER TABLE `jc_friendlink` DISABLE KEYS */;
INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES 
 (1,1,1,'JEECMS官网','http://www.jeecms.com',NULL,'jeecms@163.com','JEECMS是JavaEE版网站管理系统（Java Enterprise Edition Content Manage System）的简称。Java凭借其强大、稳定、安全、高效等多方面的优势，一直是企业级应用的首选。',3,'1',1),
 (2,1,1,'JEEBBS论坛','http://bbs.jeecms.com',NULL,'jeecms@163.com','JEEBBS论坛',1,'1',10);
/*!40000 ALTER TABLE `jc_friendlink` ENABLE KEYS */;


--
-- Definition of table `jc_friendlink_ctg`
--

DROP TABLE IF EXISTS `jc_friendlink_ctg`;
CREATE TABLE `jc_friendlink_ctg` (
  `friendlinkctg_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `friendlinkctg_name` varchar(50) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`friendlinkctg_id`),
  KEY `fk_jc_friendlinkctg_site` (`site_id`),
  CONSTRAINT `fk_jc_friendlinkctg_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS友情链接类别';

--
-- Dumping data for table `jc_friendlink_ctg`
--

/*!40000 ALTER TABLE `jc_friendlink_ctg` DISABLE KEYS */;
INSERT INTO `jc_friendlink_ctg` (`friendlinkctg_id`,`site_id`,`friendlinkctg_name`,`priority`) VALUES 
 (1,1,'文字链接',1);
/*!40000 ALTER TABLE `jc_friendlink_ctg` ENABLE KEYS */;


--
-- Definition of table `jc_group`
--

DROP TABLE IF EXISTS `jc_group`;
CREATE TABLE `jc_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `need_captcha` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否需要验证码',
  `need_check` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否需要审核',
  `allow_per_day` int(11) NOT NULL DEFAULT '4096' COMMENT '每日允许上传KB',
  `allow_max_file` int(11) NOT NULL DEFAULT '1024' COMMENT '每个文件最大KB',
  `allow_suffix` varchar(255) DEFAULT 'jpg,jpeg,gif,png,bmp' COMMENT '允许上传的后缀',
  `is_reg_def` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认会员组',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='CMS会员组表';

--
-- Dumping data for table `jc_group`
--

/*!40000 ALTER TABLE `jc_group` DISABLE KEYS */;
INSERT INTO `jc_group` (`group_id`,`group_name`,`priority`,`need_captcha`,`need_check`,`allow_per_day`,`allow_max_file`,`allow_suffix`,`is_reg_def`) VALUES 
 (1,'普通会员',10,1,1,4096,1024,'jpg,jpeg,gif,png,bmp',1),
 (2,'高级组',10,1,1,0,0,'',0);
/*!40000 ALTER TABLE `jc_group` ENABLE KEYS */;


--
-- Definition of table `jc_guestbook`
--

DROP TABLE IF EXISTS `jc_guestbook`;
CREATE TABLE `jc_guestbook` (
  `guestbook_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `guestbookctg_id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL COMMENT '留言会员',
  `admin_id` int(11) DEFAULT NULL COMMENT '回复管理员',
  `ip` varchar(50) NOT NULL COMMENT '留言IP',
  `create_time` datetime NOT NULL COMMENT '留言时间',
  `replay_time` datetime DEFAULT NULL COMMENT '回复时间',
  `is_checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否审核',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐',
  PRIMARY KEY (`guestbook_id`),
  KEY `fk_jc_ctg_guestbook` (`guestbookctg_id`),
  KEY `fk_jc_guestbook_admin` (`admin_id`),
  KEY `fk_jc_guestbook_member` (`member_id`),
  KEY `fk_jc_guestbook_site` (`site_id`),
  CONSTRAINT `fk_jc_ctg_guestbook` FOREIGN KEY (`guestbookctg_id`) REFERENCES `jc_guestbook_ctg` (`guestbookctg_id`),
  CONSTRAINT `fk_jc_guestbook_admin` FOREIGN KEY (`admin_id`) REFERENCES `jc_user` (`user_id`),
  CONSTRAINT `fk_jc_guestbook_member` FOREIGN KEY (`member_id`) REFERENCES `jc_user` (`user_id`),
  CONSTRAINT `fk_jc_guestbook_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='CMS留言';

--
-- Dumping data for table `jc_guestbook`
--

/*!40000 ALTER TABLE `jc_guestbook` DISABLE KEYS */;
INSERT INTO `jc_guestbook` (`guestbook_id`,`site_id`,`guestbookctg_id`,`member_id`,`admin_id`,`ip`,`create_time`,`replay_time`,`is_checked`,`is_recommend`) VALUES 
 (1,1,1,1,NULL,'127.0.0.1','2011-01-04 10:08:18',NULL,1,1),
 (2,1,1,1,NULL,'192.168.0.1','2011-01-04 14:45:45',NULL,1,1),
 (3,1,1,1,NULL,'192.168.0.1','2011-01-04 14:46:24',NULL,1,1),
 (4,1,1,1,NULL,'192.168.0.1','2011-01-04 14:52:41',NULL,1,1);
/*!40000 ALTER TABLE `jc_guestbook` ENABLE KEYS */;


--
-- Definition of table `jc_guestbook_ctg`
--

DROP TABLE IF EXISTS `jc_guestbook_ctg`;
CREATE TABLE `jc_guestbook_ctg` (
  `guestbookctg_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `ctg_name` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`guestbookctg_id`),
  KEY `fk_jc_guestbookctg_site` (`site_id`),
  CONSTRAINT `fk_jc_guestbookctg_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS留言类别';

--
-- Dumping data for table `jc_guestbook_ctg`
--

/*!40000 ALTER TABLE `jc_guestbook_ctg` DISABLE KEYS */;
INSERT INTO `jc_guestbook_ctg` (`guestbookctg_id`,`site_id`,`ctg_name`,`priority`,`description`) VALUES 
 (1,1,'普通',1,'普通留言');
/*!40000 ALTER TABLE `jc_guestbook_ctg` ENABLE KEYS */;


--
-- Definition of table `jc_guestbook_ext`
--

DROP TABLE IF EXISTS `jc_guestbook_ext`;
CREATE TABLE `jc_guestbook_ext` (
  `guestbook_id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '留言标题',
  `content` longtext COMMENT '留言内容',
  `reply` longtext COMMENT '回复内容',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(100) DEFAULT NULL COMMENT '电话',
  `qq` varchar(50) DEFAULT NULL COMMENT 'QQ',
  KEY `fk_jc_ext_guestbook` (`guestbook_id`),
  CONSTRAINT `fk_jc_ext_guestbook` FOREIGN KEY (`guestbook_id`) REFERENCES `jc_guestbook` (`guestbook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS留言内容';

--
-- Dumping data for table `jc_guestbook_ext`
--

/*!40000 ALTER TABLE `jc_guestbook_ext` DISABLE KEYS */;
INSERT INTO `jc_guestbook_ext` (`guestbook_id`,`title`,`content`,`reply`,`email`,`phone`,`qq`) VALUES 
 (1,'湖南未来一周仍维持低温雨雪天气','湖南未来一周仍维持低温雨雪天气',NULL,NULL,NULL,NULL),
 (2,'范冰冰退出娱乐圈','范冰冰退出娱乐圈',NULL,NULL,NULL,NULL),
 (3,'JEECMS v3.0.2正式版发布','终于发布了',NULL,NULL,NULL,NULL),
 (4,'多重压力将影响整体衣柜发展','据悉，2010年下半年以来，衣柜企业整体销售形势不容乐观。不少企业下滑幅度高达60%，在一些主流卖场里，即使排名前十名的企业也未必能盈利。在这种情况下，企业纷纷打出了涨价牌，实属无奈之举。',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `jc_guestbook_ext` ENABLE KEYS */;


--
-- Definition of table `jc_keyword`
--

DROP TABLE IF EXISTS `jc_keyword`;
CREATE TABLE `jc_keyword` (
  `keyword_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL COMMENT '站点ID',
  `keyword_name` varchar(100) NOT NULL COMMENT '名称',
  `url` varchar(255) NOT NULL COMMENT '链接',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`keyword_id`),
  KEY `fk_jc_keyword_site` (`site_id`),
  CONSTRAINT `fk_jc_keyword_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS内容关键词表';

--
-- Dumping data for table `jc_keyword`
--

/*!40000 ALTER TABLE `jc_keyword` DISABLE KEYS */;
INSERT INTO `jc_keyword` (`keyword_id`,`site_id`,`keyword_name`,`url`,`is_disabled`) VALUES 
 (1,NULL,'内容管理系统','<a href=\"http://www.jeecms.com/\" target=\"_blank\">内容管理系统</a>',0);
/*!40000 ALTER TABLE `jc_keyword` ENABLE KEYS */;


--
-- Definition of table `jc_log`
--

DROP TABLE IF EXISTS `jc_log`;
CREATE TABLE `jc_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `site_id` int(11) DEFAULT NULL,
  `category` int(11) NOT NULL COMMENT '日志类型',
  `log_time` datetime NOT NULL COMMENT '日志时间',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `url` varchar(255) DEFAULT NULL COMMENT 'URL地址',
  `title` varchar(255) DEFAULT NULL COMMENT '日志标题',
  `content` varchar(255) DEFAULT NULL COMMENT '日志内容',
  PRIMARY KEY (`log_id`),
  KEY `fk_jc_log_site` (`site_id`),
  KEY `fk_jc_log_user` (`user_id`),
  CONSTRAINT `fk_jc_log_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
  CONSTRAINT `fk_jc_log_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8 COMMENT='CMS日志表';

--
-- Dumping data for table `jc_log`
--

/*!40000 ALTER TABLE `jc_log` DISABLE KEYS */;
INSERT INTO `jc_log` (`log_id`,`user_id`,`site_id`,`category`,`log_time`,`ip`,`url`,`title`,`content`) VALUES 
 (1,1,NULL,1,'2011-06-04 14:45:30','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (2,1,1,3,'2011-06-04 14:47:34','127.0.0.1','/jeeadmin/jeecms/content/o_update.do','修改文章','id=189;title=大连遭遇强降雪和寒潮天气'),
 (3,1,1,3,'2011-06-04 14:48:44','127.0.0.1','/jeeadmin/jeecms/content/o_update.do','修改文章','id=189;title=大连遭遇强降雪和寒潮天气'),
 (4,NULL,NULL,2,'2011-06-04 14:49:09','127.0.0.1','/jeeadmin/jeecms/login.do','登录失败','username=admin;password=password'),
 (5,1,NULL,1,'2011-06-04 14:49:12','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (6,1,1,3,'2011-06-04 14:49:50','127.0.0.1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=10;name=关于我们'),
 (7,1,1,3,'2011-06-04 14:49:55','127.0.0.1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=10;name=关于我们'),
 (8,1,1,3,'2011-06-04 14:50:36','127.0.0.1','/jeeadmin/jeecms/member/o_save.do','增加会员','id=2;username=korven'),
 (9,1,1,3,'2011-06-04 14:50:44','127.0.0.1','/jeeadmin/jeecms/member/o_delete.do','删除会员','id=2;username=korven'),
 (10,1,1,3,'2011-06-04 14:51:00','127.0.0.1','/jeeadmin/jeecms/admin_local/o_save.do','增加用户','id=3;username=korven'),
 (11,1,1,3,'2011-06-04 14:51:10','127.0.0.1','/jeeadmin/jeecms/admin_local/o_update.do','修改用户','id=3;username=korven'),
 (12,1,1,3,'2011-06-04 14:51:15','127.0.0.1','/jeeadmin/jeecms/admin_local/o_update.do','修改用户','id=3;username=korven'),
 (13,1,1,3,'2011-06-04 14:51:18','127.0.0.1','/jeeadmin/jeecms/admin_local/o_delete.do','删除用户','id=3;username=korven'),
 (14,1,1,3,'2011-06-04 14:52:08','127.0.0.1','/jeeadmin/jeecms/channel/o_save.do','增加栏目','id=40;title=null'),
 (15,1,1,3,'2011-06-04 14:52:13','127.0.0.1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=40;name=11'),
 (16,1,1,3,'2011-06-04 14:52:15','127.0.0.1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=40;title=22'),
 (17,1,NULL,1,'2011-06-04 15:22:42','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (18,1,NULL,1,'2011-06-04 15:22:55','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (19,1,1,3,'2011-06-04 15:23:35','127.0.0.1','/jeeadmin/jeecms/advertising_space/o_save.do','增加广告版位','id=1;name=页头banner'),
 (20,1,1,3,'2011-06-04 15:24:53','127.0.0.1','/jeeadmin/jeecms/advertising/o_save.do','增加广告','id=1;name=banner'),
 (21,1,1,3,'2011-06-04 15:25:28','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (22,1,1,3,'2011-06-04 15:26:04','127.0.0.1','/jeeadmin/jeecms/friendlink_ctg/o_save.do','增加友情链接类别','id=1;name=文字链接'),
 (23,1,1,3,'2011-06-04 15:26:08','127.0.0.1','/jeeadmin/jeecms/friendlink_ctg/o_update.do','修改友情链接类别',NULL),
 (24,1,1,3,'2011-06-04 15:26:56','127.0.0.1','/jeeadmin/jeecms/friendlink/o_save.do','增加友情链接','id=1;name=JEECMS官网'),
 (25,1,1,3,'2011-06-04 15:28:24','127.0.0.1','/jeeadmin/jeecms/friendlink/o_update.do','修改友情链接','id=1;name=JEECMS官网'),
 (26,1,1,3,'2011-06-04 15:29:21','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/index/首页.html'),
 (27,1,1,3,'2011-06-04 15:29:52','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/index/首页.html'),
 (28,1,1,3,'2011-06-04 15:31:05','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/index/首页.html'),
 (29,1,1,3,'2011-06-04 15:32:05','127.0.0.1','/jeeadmin/jeecms/content/o_update.do','修改文章','id=186;title=谎言：日本科研捕鲸'),
 (30,1,1,3,'2011-06-04 15:32:37','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/index/首页.html'),
 (31,1,1,3,'2011-06-04 15:40:57','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/index/首页.html'),
 (32,1,1,3,'2011-06-04 15:41:16','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/index/首页.html'),
 (33,1,NULL,1,'2011-06-04 15:43:05','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (34,1,1,3,'2011-06-04 15:59:43','127.0.0.1','/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (35,1,1,3,'2011-06-04 16:05:19','127.0.0.1','/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (36,1,1,3,'2011-06-04 16:10:15','127.0.0.1','/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (37,1,1,3,'2011-06-04 16:13:31','127.0.0.1','/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (38,1,1,3,'2011-06-04 16:16:55','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/default/member/会员登录页.html'),
 (39,1,1,3,'2011-06-04 16:29:11','127.0.0.1','/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (40,1,NULL,1,'2011-06-06 15:22:59','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (41,1,1,3,'2011-06-06 15:30:02','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/include/页头导航栏.html'),
 (42,1,1,3,'2011-06-06 15:34:31','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/index/首页.html'),
 (43,1,1,3,'2011-06-06 15:34:39','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/index/首页.html'),
 (44,1,1,3,'2011-06-06 15:34:52','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/index/首页.html'),
 (45,1,1,3,'2011-06-06 15:39:48','127.0.0.1','/jeeadmin/jeecms/template/o_save.do','增加模板','filename=广告'),
 (46,1,1,3,'2011-06-06 15:40:07','127.0.0.1','/jeeadmin/jeecms/template/o_save.do','增加模板','filename=广告版位'),
 (47,1,1,3,'2011-06-06 15:40:26','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/index/首页.html'),
 (48,1,NULL,1,'2011-06-06 16:51:33','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (49,1,1,3,'2011-06-06 16:51:49','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/include/页头导航栏.html'),
 (50,1,1,3,'2011-06-06 16:52:17','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (51,1,1,3,'2011-06-06 16:52:25','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (52,1,1,3,'2011-06-06 16:53:00','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/include/页头导航栏.html'),
 (53,1,1,3,'2011-06-06 16:53:10','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/include/页头导航栏.html'),
 (54,1,1,3,'2011-06-06 16:53:23','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (55,1,1,3,'2011-06-06 16:53:33','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (56,1,1,3,'2011-06-06 16:53:45','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (57,1,1,3,'2011-06-06 16:53:55','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (58,1,1,3,'2011-06-06 16:54:06','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (59,1,1,3,'2011-06-06 16:54:20','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (60,1,1,3,'2011-06-06 16:54:29','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (61,1,1,3,'2011-06-06 16:56:14','127.0.0.1','/jeeadmin/jeecms/resource/o_update.do','修改资源','filename=/r/cms/www/red/css/layout.css'),
 (62,1,1,3,'2011-06-06 16:56:42','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (63,1,1,3,'2011-06-06 16:57:39','127.0.0.1','/jeeadmin/jeecms/resource/o_update.do','修改资源','filename=/r/cms/www/red/css/layout.css'),
 (64,1,1,3,'2011-06-06 16:58:01','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (65,1,1,3,'2011-06-06 17:01:42','127.0.0.1','/jeeadmin/jeecms/advertising_space/o_save.do','增加广告版位','id=2;name=横条广告'),
 (66,1,1,3,'2011-06-06 17:02:53','127.0.0.1','/jeeadmin/jeecms/advertising/o_save.do','增加广告','id=2;name=通栏广告1'),
 (67,1,1,3,'2011-06-06 17:03:12','127.0.0.1','/jeeadmin/jeecms/advertising_space/o_update.do','修改广告版位','id=2;name=通栏广告'),
 (68,1,1,3,'2011-06-06 17:13:02','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/index/首页.html'),
 (69,1,1,3,'2011-06-06 17:13:51','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=2;name=通栏广告1'),
 (70,1,1,3,'2011-06-06 17:15:25','127.0.0.1','/jeeadmin/jeecms/advertising/o_update.do','修改广告','id=1;name=banner'),
 (71,1,1,3,'2011-06-06 17:18:34','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/channel/新闻栏目_父级.html'),
 (72,1,1,3,'2011-06-06 17:18:59','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/channel/新闻栏目.html'),
 (73,1,1,3,'2011-06-06 17:22:14','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/content/新闻内容.html'),
 (74,1,1,3,'2011-06-06 17:27:45','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/include/页脚友情链接栏.html'),
 (75,1,1,3,'2011-06-06 17:28:59','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/include/页脚友情链接栏.html'),
 (76,1,1,3,'2011-06-06 17:30:37','127.0.0.1','/jeeadmin/jeecms/friendlink/o_save.do','增加友情链接','id=2;name=JEEBBS论坛'),
 (77,1,NULL,1,'2011-06-06 21:43:07','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (78,1,1,3,'2011-06-06 21:44:10','127.0.0.1','/jeeadmin/jeecms/template/o_save.do','增加模板','filename=找回密码结果页'),
 (79,1,1,3,'2011-06-06 21:44:29','127.0.0.1','/jeeadmin/jeecms/template/o_save.do','增加模板','filename=找回密码输入页'),
 (80,1,1,3,'2011-06-06 21:46:38','127.0.0.1','/jeeadmin/jeecms/template/o_save.do','增加模板','filename=密码重置页'),
 (81,1,NULL,1,'2011-06-06 21:47:27','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (82,1,1,3,'2011-06-06 21:49:09','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/csi/会员登录.html'),
 (83,1,1,3,'2011-06-06 21:49:50','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/csi/会员登录.html'),
 (84,1,1,3,'2011-06-06 22:03:36','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/找回密码输入页.html'),
 (85,1,1,3,'2011-06-06 22:11:48','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/找回密码输入页.html'),
 (86,1,1,3,'2011-06-06 22:20:52','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/找回密码结果页.html'),
 (87,1,NULL,1,'2011-06-06 23:01:32','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (88,1,1,3,'2011-06-06 23:01:51','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/找回密码结果页.html'),
 (89,1,1,3,'2011-06-06 23:03:10','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/找回密码结果页.html'),
 (90,1,1,3,'2011-06-06 23:08:17','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/密码重置页.html'),
 (91,1,1,3,'2011-06-06 23:09:01','127.0.0.1','/jeeadmin/jeecms/template/o_update.do','修改模板','filename=/WEB-INF/t/cms/www/red/member/密码重置页.html'),
 (92,1,NULL,1,'2011-06-07 00:00:55','127.0.0.1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (93,1,1,3,'2011-06-07 00:01:49','127.0.0.1','/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (94,1,NULL,1,'2011-06-07 08:22:33','127.0.0.1','/jeecms310/jeeadmin/jeecms/login.do','登录成功',NULL),
 (95,1,1,3,'2011-06-07 08:22:44','127.0.0.1','/jeecms310/jeeadmin/jeecms/config/o_system_update.do','修改系统设置',NULL),
 (96,1,1,3,'2011-06-07 08:23:48','127.0.0.1','/jeecms310/jeeadmin/jeecms/config/o_system_update.do','修改系统设置',NULL),
 (97,1,1,3,'2011-06-07 08:23:58','127.0.0.1','/jeecms310/jeeadmin/jeecms/config/o_login_update.do','修改登录设置',NULL),
 (98,1,NULL,1,'2011-09-15 22:21:16','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (99,1,NULL,1,'2011-09-16 20:19:06','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (100,1,NULL,1,'2011-09-16 20:30:39','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (101,1,NULL,1,'2011-09-16 20:40:20','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (102,1,NULL,1,'2011-09-16 20:41:59','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (103,1,NULL,1,'2011-09-16 20:50:52','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (104,1,1,3,'2011-09-16 21:24:53','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=5;title=体育世界'),
 (105,1,1,3,'2011-09-16 21:25:24','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=7;title=时尚汽车'),
 (106,1,1,3,'2011-09-16 21:25:29','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=6;title=科技创新'),
 (107,1,1,3,'2011-09-16 21:29:47','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=2;title=影视娱乐'),
 (108,1,1,3,'2011-09-16 21:29:47','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=3;title=财经报道'),
 (109,1,1,3,'2011-09-16 21:29:47','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=4;title=房产资讯'),
 (110,1,1,3,'2011-09-16 21:47:03','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=1;name=最新动态'),
 (111,1,1,3,'2011-09-16 21:47:28','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=9;name=产品中心'),
 (112,1,NULL,1,'2011-09-16 22:34:34','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (113,1,NULL,1,'2011-09-17 00:27:32','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (114,1,NULL,1,'2011-09-17 02:36:20','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (115,1,1,3,'2011-09-17 02:49:26','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=1;name=最新动态'),
 (116,1,NULL,1,'2011-09-17 08:27:44','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (117,1,NULL,1,'2011-09-17 09:42:27','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (118,1,NULL,1,'2011-09-17 11:01:26','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (119,1,NULL,1,'2011-09-17 13:13:30','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (120,1,NULL,1,'2011-09-17 13:24:39','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (121,1,NULL,1,'2011-09-17 13:49:20','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (122,1,NULL,1,'2011-09-17 13:56:37','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (123,1,NULL,1,'2011-09-17 14:46:49','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (124,1,NULL,1,'2011-09-17 14:53:39','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (125,1,NULL,1,'2011-09-17 15:11:30','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (126,1,1,3,'2011-09-17 15:11:45','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_save.do','增加友情链接','id=4;name=dd'),
 (127,1,1,3,'2011-09-17 15:12:42','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_delete.do','删除友情链接','id=4;name=dd'),
 (128,1,NULL,1,'2011-09-17 15:18:37','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (129,1,NULL,1,'2011-09-17 15:23:07','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (130,1,1,3,'2011-09-17 15:33:37','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=1;name=xjany'),
 (131,1,1,3,'2011-09-17 15:33:45','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=1;name=xjany'),
 (132,1,1,3,'2011-09-17 15:35:10','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_save.do','增加友情链接','id=5;name=a'),
 (133,1,NULL,1,'2011-09-17 16:07:41','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (134,1,NULL,1,'2011-09-17 16:58:19','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (135,1,1,3,'2011-09-17 18:27:24','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=10;name=关于我们'),
 (136,1,NULL,1,'2011-09-17 20:33:50','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (137,1,1,3,'2011-09-17 20:35:27','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_delete.do','删除友情链接','id=5;name=a'),
 (138,1,1,3,'2011-09-17 20:35:31','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_delete.do','删除友情链接','id=3;name=test2'),
 (139,1,1,3,'2011-09-17 20:35:34','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_delete.do','删除友情链接','id=1;name=xjany'),
 (140,1,1,3,'2011-09-17 20:35:39','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_save.do','增加友情链接','id=6;name=tes'),
 (141,1,1,3,'2011-09-17 20:38:28','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (142,1,1,3,'2011-09-17 20:38:38','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (143,1,NULL,1,'2011-09-17 20:59:16','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (144,1,1,3,'2011-09-17 21:00:13','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (145,1,1,3,'2011-09-17 21:00:19','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (146,1,1,3,'2011-09-17 21:00:28','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (147,1,1,3,'2011-09-17 21:00:38','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (148,1,1,3,'2011-09-17 21:00:56','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (149,1,1,3,'2011-09-17 21:02:00','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (150,1,1,3,'2011-09-17 21:03:54','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_save.do','增加栏目','id=40;title=null'),
 (151,1,1,3,'2011-09-17 21:06:06','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=40;name=联系我们'),
 (152,1,1,3,'2011-09-17 21:14:10','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=1;name=天然医生'),
 (153,1,NULL,1,'2011-09-18 01:06:03','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (154,1,1,3,'2011-09-18 01:06:37','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_save.do','增加友情链接','id=7;name=123'),
 (155,1,1,3,'2011-09-18 01:10:00','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=7;name=123'),
 (156,1,1,3,'2011-09-18 01:12:32','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=1;name=新闻时事'),
 (157,1,1,3,'2011-09-18 01:20:10','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_save.do','增加栏目','id=41;title=天然医生'),
 (158,1,NULL,1,'2011-09-18 09:38:50','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (159,1,1,3,'2011-09-18 09:39:37','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=6;name=tes'),
 (160,1,1,3,'2011-09-18 09:40:10','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_update.do','修改友情链接','id=7;name=123'),
 (161,1,1,3,'2011-09-18 09:54:05','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=1;name=新闻时事'),
 (162,1,1,3,'2011-09-18 09:54:51','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=9;name=产品中心'),
 (163,1,1,3,'2011-09-18 09:55:05','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=10;name=关于我们'),
 (164,1,1,3,'2011-09-18 09:56:52','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=40;name=联系我们'),
 (165,1,1,3,'2011-09-18 09:57:08','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=41;name=天然医生'),
 (166,1,NULL,1,'2011-09-18 11:45:17','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (167,1,1,3,'2011-09-18 11:47:55','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_save.do','增加友情链接','id=8;name=aaa'),
 (168,1,NULL,1,'2011-09-18 13:07:12','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (169,1,1,3,'2011-09-18 13:08:51','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_save.do','增加栏目','id=42;title=产品1'),
 (170,1,1,3,'2011-09-18 13:09:07','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=9;name=产品中心'),
 (171,1,1,3,'2011-09-18 13:30:31','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/model/o_update.do','修改模型','id=4;name=产品'),
 (172,1,1,3,'2011-09-18 13:41:47','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=37;name=产品类型1'),
 (173,1,NULL,1,'2011-09-18 16:38:45','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (174,1,1,3,'2011-09-18 16:39:20','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/xjanypic/o_save.do','增加友情链接','id=9;name=4444'),
 (175,1,NULL,1,'2011-09-18 17:48:22','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (176,1,1,3,'2011-09-18 17:49:06','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/site_config/o_base_update.do','站点基本设置',NULL),
 (177,1,1,3,'2011-09-18 17:51:03','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=41;title=天然医生'),
 (178,1,1,3,'2011-09-18 17:51:25','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=1;name=天然医生'),
 (179,1,NULL,1,'2011-09-18 18:06:59','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (180,1,1,3,'2011-09-18 18:08:28','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=1;title=天然医生'),
 (181,1,1,3,'2011-09-18 18:08:28','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_delete.do','删除栏目','id=9;title=产品中心'),
 (182,1,1,3,'2011-09-18 18:09:50','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_save.do','增加栏目','id=43;title=天然医生'),
 (183,1,1,3,'2011-09-18 18:16:12','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_save.do','增加栏目','id=44;title=产品中心'),
 (184,1,1,3,'2011-09-18 18:16:34','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=44;name=产品中心'),
 (185,1,1,3,'2011-09-18 18:24:37','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/content/o_save.do','增加文章','id=190;title=中医说：医食同源，天然是人类最好的医生 '),
 (186,1,1,3,'2011-09-18 18:31:07','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/content/o_save.do','增加文章','id=191;title=蛋白质粉伴侣 '),
 (187,1,NULL,1,'2011-09-18 19:16:38','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (188,1,NULL,1,'2011-09-18 20:45:40','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (189,1,1,3,'2011-09-18 21:33:32','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/channel/o_update.do','修改栏目','id=44;name=产品中心'),
 (190,1,NULL,1,'2011-09-18 23:00:57','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/login.do','登录成功',NULL),
 (191,1,1,3,'2011-09-18 23:01:08','0:0:0:0:0:0:0:1','/jeeadmin/jeecms/content/o_update.do','修改文章','id=191;title=蛋白质粉伴侣 ');
/*!40000 ALTER TABLE `jc_log` ENABLE KEYS */;


--
-- Definition of table `jc_model`
--

DROP TABLE IF EXISTS `jc_model`;
CREATE TABLE `jc_model` (
  `model_id` int(11) NOT NULL,
  `model_name` varchar(100) NOT NULL COMMENT '名称',
  `model_path` varchar(100) NOT NULL COMMENT '路径',
  `tpl_channel_prefix` varchar(20) DEFAULT NULL COMMENT '栏目模板前缀',
  `tpl_content_prefix` varchar(20) DEFAULT NULL COMMENT '内容模板前缀',
  `title_img_width` int(11) NOT NULL DEFAULT '139' COMMENT '栏目标题图宽度',
  `title_img_height` int(11) NOT NULL DEFAULT '139' COMMENT '栏目标题图高度',
  `content_img_width` int(11) NOT NULL DEFAULT '310' COMMENT '栏目内容图宽度',
  `content_img_height` int(11) NOT NULL DEFAULT '310' COMMENT '栏目内容图高度',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `has_content` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有内容',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_def` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认模型',
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS模型表';

--
-- Dumping data for table `jc_model`
--

/*!40000 ALTER TABLE `jc_model` DISABLE KEYS */;
INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`) VALUES 
 (1,'文章','1','文章栏目','文章内容',139,139,310,310,1,1,0,1),
 (2,'单页','2','单页','',139,139,310,310,2,0,0,0),
 (4,'产品','4','产品栏目','产品内容',139,139,310,310,4,1,0,0);
/*!40000 ALTER TABLE `jc_model` ENABLE KEYS */;


--
-- Definition of table `jc_model_item`
--

DROP TABLE IF EXISTS `jc_model_item`;
CREATE TABLE `jc_model_item` (
  `modelitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` int(11) NOT NULL,
  `field` varchar(50) NOT NULL COMMENT '字段',
  `item_label` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '70' COMMENT '排列顺序',
  `def_value` varchar(255) DEFAULT NULL COMMENT '默认值',
  `opt_value` varchar(255) DEFAULT NULL COMMENT '可选项',
  `text_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `area_rows` varchar(3) DEFAULT NULL COMMENT '文本行数',
  `area_cols` varchar(3) DEFAULT NULL COMMENT '文本列数',
  `help` varchar(255) DEFAULT NULL COMMENT '帮助信息',
  `help_position` varchar(1) DEFAULT NULL COMMENT '帮助位置',
  `data_type` int(11) NOT NULL DEFAULT '1' COMMENT '数据类型',
  `is_single` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否独占一行',
  `is_channel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否栏目模型项',
  `is_custom` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否自定义',
  `is_display` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  PRIMARY KEY (`modelitem_id`),
  KEY `fk_jc_item_model` (`model_id`),
  CONSTRAINT `fk_jc_item_model` FOREIGN KEY (`model_id`) REFERENCES `jc_model` (`model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8 COMMENT='CMS模型项表';

--
-- Dumping data for table `jc_model_item`
--

/*!40000 ALTER TABLE `jc_model_item` DISABLE KEYS */;
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`) VALUES 
 (1,1,'name','栏目名称',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (2,1,'path','访问路径',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (3,1,'title','meta标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (4,1,'keywords','meta关键字',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (5,1,'description','meta描述',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1),
 (6,1,'tplChannel','栏目模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1),
 (7,1,'tplContent','内容模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1),
 (8,1,'channelStatic','栏目静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1),
 (9,1,'contentStatic','内容静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1),
 (10,1,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (11,1,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (12,1,'docImg','文档图片',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1),
 (13,1,'finalStep','终审级别',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (14,1,'afterCheck','审核后',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1),
 (15,1,'commentControl','评论',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (16,1,'allowUpdown','顶踩',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (17,1,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1),
 (18,1,'contriGroupIds','投稿权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1),
 (19,1,'userIds','管理权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,1,0,1),
 (20,1,'link','外部链接',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1),
 (21,1,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1),
 (22,1,'channelId','栏目',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1),
 (23,1,'title','标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (24,1,'shortTitle','简短标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1),
 (25,1,'titleColor','标题颜色',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1),
 (26,1,'tagStr','Tag标签',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (27,1,'description','摘要',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1),
 (28,1,'author','作者',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1),
 (29,1,'origin','来源',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1),
 (30,1,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1),
 (31,1,'topLevel','固顶级别',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1),
 (32,1,'releaseDate','发布时间',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,0,0,1),
 (33,1,'typeId','内容类型',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1),
 (34,1,'tplContent','指定模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1),
 (35,1,'typeImg','类型图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (36,1,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (37,1,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (38,1,'attachments','附件',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (39,1,'media','多媒体',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (40,1,'txt','内容',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1),
 (41,1,'pictures','图片集',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1),
 (42,2,'name','栏目名称',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (43,2,'path','访问路径',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (44,2,'title','meta标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (45,2,'keywords','meta关键字',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (46,2,'description','meta描述',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1),
 (47,2,'tplChannel','栏目模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1),
 (48,2,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (49,2,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (50,2,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1),
 (51,2,'link','外部链接',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1),
 (52,2,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1),
 (53,2,'txt','内容',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1),
 (93,4,'name','栏目名称',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (94,4,'path','访问路径',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (95,4,'title','meta标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (96,4,'keywords','meta关键字',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1),
 (97,4,'description','meta描述',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1),
 (98,4,'tplChannel','栏目模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1),
 (99,4,'tplContent','内容模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1),
 (100,4,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1),
 (101,4,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (102,4,'docImg','文档图片',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1),
 (103,4,'commentControl','评论',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (104,4,'allowUpdown','顶踩',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1),
 (105,4,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,1,0,1),
 (106,4,'userIds','管理权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,1,0,1),
 (107,4,'channelId','栏目',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,0),
 (108,4,'title','产品名称',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (109,4,'shortTitle','产品简称',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,0),
 (110,4,'titleColor','标题颜色',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1),
 (111,4,'description','摘要',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1),
 (112,4,'author','发布人',6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (113,4,'viewGroupIds','浏览权限',7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,0,0,0),
 (114,4,'topLevel','固顶级别',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1),
 (115,4,'releaseDate','发布时间',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,1,0,0,1),
 (116,4,'typeId','内容类型',21,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1),
 (117,4,'tplContent','指定模板',22,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1),
 (118,4,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (119,4,'attachments','产品上传',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,0),
 (120,4,'txt','产品介绍',20,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1),
 (121,4,'softType','产品类型',12,'国产软件','国产软件,国外软件','100','3','30',NULL,NULL,6,0,0,1,0),
 (122,4,'warrant','软件授权',13,'免费版','免费版,共享版','','3','30','','',6,0,0,1,0),
 (123,4,'relatedLink','相关链接',14,'http://','','50','3','30','','',1,0,0,1,0),
 (124,4,'demoUrl','演示地址',15,'http://',NULL,'60','3','30',NULL,NULL,1,0,0,1,0),
 (126,1,'ename','英文名称',70,'','','','3','30','','',1,1,1,1,1),
 (127,2,'ename','英文名称',70,'','','','3','30','','',1,1,1,1,1),
 (129,4,'ename','英文名称',70,'','','','3','30','','',1,1,1,1,1),
 (130,4,'tagStr','Tag标签',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,0),
 (131,4,'origin','来源',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,0),
 (132,4,'typeImg','类型图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,0),
 (133,4,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1),
 (134,4,'media','多媒体',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,0),
 (135,4,'txt1','内容1',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,0),
 (136,4,'txt2','内容2',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,0),
 (137,4,'txt3','内容3',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,0),
 (138,4,'pictures','图片集',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,0);
/*!40000 ALTER TABLE `jc_model_item` ENABLE KEYS */;


--
-- Definition of table `jc_piclist_xjany`
--

DROP TABLE IF EXISTS `jc_piclist_xjany`;
CREATE TABLE `jc_piclist_xjany` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` longtext,
  `name` longtext,
  `pic` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jc_piclist_xjany`
--

/*!40000 ALTER TABLE `jc_piclist_xjany` DISABLE KEYS */;
INSERT INTO `jc_piclist_xjany` (`id`,`url`,`name`,`pic`) VALUES 
 (6,'http://','tes','/u/cms/www/201109/18093935711j.jpg'),
 (7,'http://www.baidu.com','123','/u/cms/www/201109/18094002eqlq.jpg'),
 (8,'http://','aaa','/u/cms/www/201109/181147525m8c.jpg'),
 (9,'http://www.google.com','4444','/u/cms/www/201109/18163918o1jw.jpg');
/*!40000 ALTER TABLE `jc_piclist_xjany` ENABLE KEYS */;


--
-- Definition of table `jc_role`
--

DROP TABLE IF EXISTS `jc_role`;
CREATE TABLE `jc_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL,
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `is_super` char(1) NOT NULL DEFAULT '0' COMMENT '拥有所有权限',
  PRIMARY KEY (`role_id`),
  KEY `fk_jc_role_site` (`site_id`),
  CONSTRAINT `fk_jc_role_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS角色表';

--
-- Dumping data for table `jc_role`
--

/*!40000 ALTER TABLE `jc_role` DISABLE KEYS */;
INSERT INTO `jc_role` (`role_id`,`site_id`,`role_name`,`priority`,`is_super`) VALUES 
 (1,NULL,'管理员',10,'1');
/*!40000 ALTER TABLE `jc_role` ENABLE KEYS */;


--
-- Definition of table `jc_role_permission`
--

DROP TABLE IF EXISTS `jc_role_permission`;
CREATE TABLE `jc_role_permission` (
  `role_id` int(11) NOT NULL,
  `uri` varchar(100) NOT NULL,
  KEY `fk_jc_permission_role` (`role_id`),
  CONSTRAINT `fk_jc_permission_role` FOREIGN KEY (`role_id`) REFERENCES `jc_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS角色授权表';

--
-- Dumping data for table `jc_role_permission`
--

/*!40000 ALTER TABLE `jc_role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_role_permission` ENABLE KEYS */;


--
-- Definition of table `jc_sensitivity`
--

DROP TABLE IF EXISTS `jc_sensitivity`;
CREATE TABLE `jc_sensitivity` (
  `sensitivity_id` int(11) NOT NULL AUTO_INCREMENT,
  `search` varchar(255) NOT NULL COMMENT '敏感词',
  `replacement` varchar(255) NOT NULL COMMENT '替换词',
  PRIMARY KEY (`sensitivity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS敏感词表';

--
-- Dumping data for table `jc_sensitivity`
--

/*!40000 ALTER TABLE `jc_sensitivity` DISABLE KEYS */;
INSERT INTO `jc_sensitivity` (`sensitivity_id`,`search`,`replacement`) VALUES 
 (1,'法论功','***');
/*!40000 ALTER TABLE `jc_sensitivity` ENABLE KEYS */;


--
-- Definition of table `jc_site`
--

DROP TABLE IF EXISTS `jc_site`;
CREATE TABLE `jc_site` (
  `site_id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` int(11) NOT NULL COMMENT '配置ID',
  `ftp_upload_id` int(11) DEFAULT NULL COMMENT '上传ftp',
  `domain` varchar(50) NOT NULL COMMENT '域名',
  `site_path` varchar(20) NOT NULL COMMENT '路径',
  `site_name` varchar(100) NOT NULL COMMENT '网站名称',
  `short_name` varchar(100) NOT NULL COMMENT '简短名称',
  `protocol` varchar(20) NOT NULL DEFAULT 'http://' COMMENT '协议',
  `dynamic_suffix` varchar(10) NOT NULL DEFAULT '.jhtml' COMMENT '动态页后缀',
  `static_suffix` varchar(10) NOT NULL DEFAULT '.html' COMMENT '静态页后缀',
  `static_dir` varchar(50) DEFAULT NULL COMMENT '静态页存放目录',
  `is_index_to_root` char(1) NOT NULL DEFAULT '0' COMMENT '是否使用将首页放在根目录下',
  `is_static_index` char(1) NOT NULL DEFAULT '0' COMMENT '是否静态化首页',
  `locale_admin` varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '后台本地化',
  `locale_front` varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '前台本地化',
  `tpl_solution` varchar(50) NOT NULL DEFAULT 'default' COMMENT '模板方案',
  `final_step` tinyint(4) NOT NULL DEFAULT '2' COMMENT '终审级别',
  `after_check` tinyint(4) NOT NULL DEFAULT '2' COMMENT '审核后(1:不能修改删除;2:修改后退回;3:修改后不变)',
  `is_relative_path` char(1) NOT NULL DEFAULT '1' COMMENT '是否使用相对路径',
  `is_recycle_on` char(1) NOT NULL DEFAULT '1' COMMENT '是否开启回收站',
  `domain_alias` varchar(255) DEFAULT NULL COMMENT '域名别名',
  `domain_redirect` varchar(255) DEFAULT NULL COMMENT '域名重定向',
  `description_xjany` longtext,
  `leftdiv_xjany` longtext,
  PRIMARY KEY (`site_id`),
  UNIQUE KEY `ak_domain` (`domain`),
  KEY `fk_jc_site_config` (`config_id`),
  KEY `fk_jc_site_upload_ftp` (`ftp_upload_id`),
  CONSTRAINT `fk_jc_site_config` FOREIGN KEY (`config_id`) REFERENCES `jc_config` (`config_id`),
  CONSTRAINT `fk_jc_site_upload_ftp` FOREIGN KEY (`ftp_upload_id`) REFERENCES `jo_ftp` (`ftp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS站点表';

--
-- Dumping data for table `jc_site`
--

/*!40000 ALTER TABLE `jc_site` DISABLE KEYS */;
INSERT INTO `jc_site` (`site_id`,`config_id`,`ftp_upload_id`,`domain`,`site_path`,`site_name`,`short_name`,`protocol`,`dynamic_suffix`,`static_suffix`,`static_dir`,`is_index_to_root`,`is_static_index`,`locale_admin`,`locale_front`,`tpl_solution`,`final_step`,`after_check`,`is_relative_path`,`is_recycle_on`,`domain_alias`,`domain_redirect`,`description_xjany`,`leftdiv_xjany`) VALUES 
 (1,1,NULL,'localhost','www','JEECMS开发站','JEECMS开发站','http://','.jhtml','.html','','0','0','zh_CN','zh_CN','red',2,2,'1','1','','','<p>&nbsp;</p>\r\n<p><span style=\"font-size: larger;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 香港盛昊康源有限公司是由百年老店盛昊堂和澳康源健康食品共同發起 組建的一家國際性公司！公司總部設立在金融、商業中心的香港九龍旺角道33號凱途發展大  廈，並在廣州設有國內總辦事處和工廠，公司立足香港，擁有世界先進的管理體系，嚴謹的産品質量控制體系！以&ldquo;創建國際化營養食品企業&rdquo;爲目標！以生産和科  研相結合的發展方針！經過不斷努力，我們和國內外多家科研中心、學術機構、健康養生中心建立起長期的合作機制！爲公司長遠發展奠定了堅實的基礎！<br />\r\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                         公司目前擁有員工300多人，是一支高效、團結的營銷團隊、研發團隊及有著優秀管理經驗的團隊。本著&ldquo;誠信、務實、創新、高效&rdquo;的企業文化，能更有效的爲 消費者提供更多優質産品及完善服務！</strong></span></p>','<div xmlns=\"\" class=\"columnSpace\" id=\"elem-FrontSpecifies_show01-1283067684075\" name=\"说明页\">\r\n<div id=\"FrontSpecifies_show01-1283067684075\" class=\"FrontSpecifies_show01-d1_c1\">\r\n<p>&nbsp;</p>\r\n<table width=\"184\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n    <tbody>\r\n        <tr>\r\n            <td><img width=\"182\" height=\"153\" alt=\"\" src=\"/r/cms/www/red/img/a_9.gif\" /></td>\r\n        </tr>\r\n        <tr>\r\n            <td align=\"left\" style=\"padding-top: 10px;\"><span style=\"color: #008000;\">\r\n            <p><strong>咨询订购热线<br />\r\n            上海:400-820-4620<br />\r\n            北京:400-600-3469<br />\r\n            广州:400-882-8875<br />\r\n            <br />\r\n            </strong></p>\r\n            <p><strong>团购电话<br />\r\n            上海:021-51362800<br />\r\n            北京:010-64319816<br />\r\n            广州:020-38910750<br />\r\n            <br />\r\n            </strong></p>\r\n            <p><strong>VIP客服邮箱<br />\r\n            上海:<a href=\"mailto:vip@richlife-china.com\"><span style=\"color: #008000;\">vip@richlife-china.com</span></a><br />\r\n            北京:<a href=\"mailto:vip-bj@richlife-china.com\"><span style=\"color: #008000;\">vip-bj@richlife-china.com</span></a><br />\r\n            广州:<a href=\"mailto:vip-gz@richlife-china.com\"><span style=\"color: #008000;\">vip-gz@richlife-china.com</span></a><br />\r\n            <br />\r\n            </strong></p>\r\n            </span><strong><span style=\"color: #008000;\">微博:</span><br />\r\n            <a href=\"http://weibo.com/richlifechina\" target=\"_blank\"><span style=\"color:\r\n            #008000;\">http://weibo.com/richlifechina</span></a><br />\r\n            <a href=\"http://weibo.com/richlifechina\" target=\"_blank\"><img alt=\"\" src=\"/r/cms/www/red/img/weibologo-v1.jpg\" /></a><br />\r\n            </strong></td>\r\n        </tr>\r\n    </tbody>\r\n</table>\r\n</div>\r\n</div>');
/*!40000 ALTER TABLE `jc_site` ENABLE KEYS */;


--
-- Definition of table `jc_site_attr`
--

DROP TABLE IF EXISTS `jc_site_attr`;
CREATE TABLE `jc_site_attr` (
  `site_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_site` (`site_id`),
  CONSTRAINT `fk_jc_attr_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点属性表';

--
-- Dumping data for table `jc_site_attr`
--

/*!40000 ALTER TABLE `jc_site_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_site_attr` ENABLE KEYS */;


--
-- Definition of table `jc_site_cfg`
--

DROP TABLE IF EXISTS `jc_site_cfg`;
CREATE TABLE `jc_site_cfg` (
  `site_id` int(11) NOT NULL,
  `cfg_name` varchar(30) NOT NULL COMMENT '名称',
  `cfg_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_cfg_site` (`site_id`),
  CONSTRAINT `fk_jc_cfg_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点配置表';

--
-- Dumping data for table `jc_site_cfg`
--

/*!40000 ALTER TABLE `jc_site_cfg` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_site_cfg` ENABLE KEYS */;


--
-- Definition of table `jc_site_model`
--

DROP TABLE IF EXISTS `jc_site_model`;
CREATE TABLE `jc_site_model` (
  `model_id` int(11) NOT NULL AUTO_INCREMENT,
  `field` varchar(50) NOT NULL COMMENT '字段',
  `model_label` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `upload_path` varchar(100) DEFAULT NULL COMMENT '上传路径',
  `text_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `area_rows` varchar(3) DEFAULT NULL COMMENT '文本行数',
  `area_cols` varchar(3) DEFAULT NULL COMMENT '文本列数',
  `help` varchar(255) DEFAULT NULL COMMENT '帮助信息',
  `help_position` varchar(1) DEFAULT NULL COMMENT '帮助位置',
  `data_type` int(11) DEFAULT '1' COMMENT '0:编辑器;1:文本框;2:文本区;3:图片;4:附件',
  `is_single` tinyint(1) DEFAULT '1' COMMENT '是否独占一行',
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `ak_field` (`field`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点信息模型表';

--
-- Dumping data for table `jc_site_model`
--

/*!40000 ALTER TABLE `jc_site_model` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_site_model` ENABLE KEYS */;


--
-- Definition of table `jc_site_txt`
--

DROP TABLE IF EXISTS `jc_site_txt`;
CREATE TABLE `jc_site_txt` (
  `site_id` int(11) NOT NULL,
  `txt_name` varchar(30) NOT NULL COMMENT '名称',
  `txt_value` longtext COMMENT '值',
  KEY `fk_jc_txt_site` (`site_id`),
  CONSTRAINT `fk_jc_txt_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点文本表';

--
-- Dumping data for table `jc_site_txt`
--

/*!40000 ALTER TABLE `jc_site_txt` DISABLE KEYS */;
/*!40000 ALTER TABLE `jc_site_txt` ENABLE KEYS */;


--
-- Definition of table `jc_topic`
--

DROP TABLE IF EXISTS `jc_topic`;
CREATE TABLE `jc_topic` (
  `topic_id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) DEFAULT NULL,
  `topic_name` varchar(150) NOT NULL COMMENT '名称',
  `short_name` varchar(150) DEFAULT NULL COMMENT '简称',
  `keywords` varchar(255) DEFAULT NULL COMMENT '关键字',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `title_img` varchar(100) DEFAULT NULL COMMENT '标题图',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '专题模板',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推??',
  PRIMARY KEY (`topic_id`),
  KEY `fk_jc_topic_channel` (`channel_id`),
  CONSTRAINT `fk_jc_topic_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='CMS专题表';

--
-- Dumping data for table `jc_topic`
--

/*!40000 ALTER TABLE `jc_topic` DISABLE KEYS */;
INSERT INTO `jc_topic` (`topic_id`,`channel_id`,`topic_name`,`short_name`,`keywords`,`description`,`title_img`,`content_img`,`tpl_content`,`priority`,`is_recommend`) VALUES 
 (1,NULL,'2010年南非世界杯','世界杯','世界杯','2010年世界杯将在南非约翰内斯堡拉开帷幕，32路豪强将在一个月的时间里，为大力神杯展开争夺。','http://a2.att.hudong.com/08/61/01300000406647124377613651616.jpg','http://i0.sinaimg.cn/ty/news/2010/0611/sjbsc.jpg','',10,1),
 (2,NULL,'上海世博会专题','世博','世博','人类文明的盛会，我们大家的世博，精彩开篇，“满月”有余。随着上海世博会的有序前行，人们从中收获的感悟也由表及里。','http://xwcb.eastday.com/c/20061116/images/00033531.jpg','http://news.china.com/zh_cn/focus/expo2010/images/top_pic.jpg','',10,1),
 (3,NULL,'低碳经济','低碳','低碳','所谓低碳经济，是指在可持续发展理念指导下，通过技术创新、制度创新、产业转型、新能源开发等多种手段，尽可能地减少煤炭石油等高碳能源消耗，减少温室气体排放，达到经济社会发展与生态环境保护双赢的一种经济发展形态。','http://www.6788.cn/bscyw/upfiles/0125/1f0aaff5/fery/w1tg.jpg',NULL,'',10,0);
/*!40000 ALTER TABLE `jc_topic` ENABLE KEYS */;


--
-- Definition of table `jc_user`
--

DROP TABLE IF EXISTS `jc_user`;
CREATE TABLE `jc_user` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '管理员级别',
  `upload_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '上传总大小',
  `upload_size` int(11) NOT NULL DEFAULT '0' COMMENT '上传大小',
  `upload_date` date DEFAULT NULL COMMENT '上传日期',
  `is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否管理员',
  `is_viewonly_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否只读管理员',
  `is_self_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否只管理自己的数据',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`),
  KEY `fk_jc_user_group` (`group_id`),
  CONSTRAINT `fk_jc_user_group` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS用户表';

--
-- Dumping data for table `jc_user`
--

/*!40000 ALTER TABLE `jc_user` DISABLE KEYS */;
INSERT INTO `jc_user` (`user_id`,`group_id`,`username`,`email`,`register_time`,`register_ip`,`last_login_time`,`last_login_ip`,`login_count`,`rank`,`upload_total`,`upload_size`,`upload_date`,`is_admin`,`is_viewonly_admin`,`is_self_admin`,`is_disabled`) VALUES 
 (1,1,'admin','admin@yahoo.com','2011-01-03 00:00:00','127.0.0.1','2011-09-18 23:00:57','0:0:0:0:0:0:0:1',61,9,0,0,NULL,1,0,0,0),
 (4,1,'korven','811459917@qq.com','2011-06-04 15:42:10','127.0.0.1','2011-06-04 16:33:02','127.0.0.1',3,0,0,0,'2011-06-04',0,0,0,0);
/*!40000 ALTER TABLE `jc_user` ENABLE KEYS */;


--
-- Definition of table `jc_user_ext`
--

DROP TABLE IF EXISTS `jc_user_ext`;
CREATE TABLE `jc_user_ext` (
  `user_id` int(11) NOT NULL,
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `intro` varchar(255) DEFAULT NULL COMMENT '个人介绍',
  `comefrom` varchar(150) DEFAULT NULL COMMENT '来自',
  `qq` varchar(100) DEFAULT NULL COMMENT 'QQ',
  `msn` varchar(100) DEFAULT NULL COMMENT 'MSN',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_jc_ext_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS用户扩展信息表';

--
-- Dumping data for table `jc_user_ext`
--

/*!40000 ALTER TABLE `jc_user_ext` DISABLE KEYS */;
INSERT INTO `jc_user_ext` (`user_id`,`realname`,`gender`,`birthday`,`intro`,`comefrom`,`qq`,`msn`,`phone`,`mobile`) VALUES 
 (1,'JEECMS',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
 (4,'korven',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `jc_user_ext` ENABLE KEYS */;


--
-- Definition of table `jc_user_role`
--

DROP TABLE IF EXISTS `jc_user_role`;
CREATE TABLE `jc_user_role` (
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `fk_jc_role_user` (`user_id`),
  CONSTRAINT `fk_jc_role_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
  CONSTRAINT `fk_jc_user_role` FOREIGN KEY (`role_id`) REFERENCES `jc_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS用户角色关联表';

--
-- Dumping data for table `jc_user_role`
--

/*!40000 ALTER TABLE `jc_user_role` DISABLE KEYS */;
INSERT INTO `jc_user_role` (`role_id`,`user_id`) VALUES 
 (1,1);
/*!40000 ALTER TABLE `jc_user_role` ENABLE KEYS */;


--
-- Definition of table `jc_user_site`
--

DROP TABLE IF EXISTS `jc_user_site`;
CREATE TABLE `jc_user_site` (
  `usersite_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `site_id` int(11) NOT NULL,
  `check_step` tinyint(4) NOT NULL DEFAULT '1' COMMENT '审核级别',
  `is_all_channel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否拥有所有栏目的权限',
  PRIMARY KEY (`usersite_id`),
  KEY `fk_jc_site_user` (`user_id`),
  KEY `fk_jc_user_site` (`site_id`),
  CONSTRAINT `fk_jc_site_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
  CONSTRAINT `fk_jc_user_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS管理员站点表';

--
-- Dumping data for table `jc_user_site`
--

/*!40000 ALTER TABLE `jc_user_site` DISABLE KEYS */;
INSERT INTO `jc_user_site` (`usersite_id`,`user_id`,`site_id`,`check_step`,`is_all_channel`) VALUES 
 (1,1,1,2,1);
/*!40000 ALTER TABLE `jc_user_site` ENABLE KEYS */;


--
-- Definition of table `jc_vote_item`
--

DROP TABLE IF EXISTS `jc_vote_item`;
CREATE TABLE `jc_vote_item` (
  `voteitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `votetopic_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `vote_count` int(11) NOT NULL DEFAULT '0' COMMENT '投票数量',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`voteitem_id`),
  KEY `fk_jc_vote_item_topic` (`votetopic_id`),
  CONSTRAINT `fk_jc_vote_item_topic` FOREIGN KEY (`votetopic_id`) REFERENCES `jc_vote_topic` (`votetopic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='CMS投票项';

--
-- Dumping data for table `jc_vote_item`
--

/*!40000 ALTER TABLE `jc_vote_item` DISABLE KEYS */;
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`) VALUES 
 (1,1,'基于java技术，安全稳定，易扩展',21,1),
 (4,1,'jsp是未来发展的趋势',23,4),
 (5,1,'java执行速度快，性能优良',5,5),
 (6,1,'跨平台，支持windows、linux、unix',6,6),
 (7,1,'学习研究',11,7),
 (8,1,'其它',10,8);
/*!40000 ALTER TABLE `jc_vote_item` ENABLE KEYS */;


--
-- Definition of table `jc_vote_record`
--

DROP TABLE IF EXISTS `jc_vote_record`;
CREATE TABLE `jc_vote_record` (
  `voterecored_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `votetopic_id` int(11) NOT NULL,
  `vote_time` datetime NOT NULL COMMENT '投票时间',
  `vote_ip` varchar(50) NOT NULL COMMENT '投票IP',
  `vote_cookie` varchar(32) NOT NULL COMMENT '投票COOKIE',
  PRIMARY KEY (`voterecored_id`),
  KEY `fk_jc_vote_record_topic` (`votetopic_id`),
  KEY `fk_jc_voterecord_user` (`user_id`),
  CONSTRAINT `fk_jc_voterecord_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
  CONSTRAINT `fk_jc_vote_record_topic` FOREIGN KEY (`votetopic_id`) REFERENCES `jc_vote_topic` (`votetopic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS投票记录';

--
-- Dumping data for table `jc_vote_record`
--

/*!40000 ALTER TABLE `jc_vote_record` DISABLE KEYS */;
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`) VALUES 
 (1,NULL,1,'2011-06-04 15:41:31','127.0.0.1','2600e4a345ba4fc289088d7abe59321c');
/*!40000 ALTER TABLE `jc_vote_record` ENABLE KEYS */;


--
-- Definition of table `jc_vote_topic`
--

DROP TABLE IF EXISTS `jc_vote_topic`;
CREATE TABLE `jc_vote_topic` (
  `votetopic_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `repeate_hour` int(11) DEFAULT NULL COMMENT '重复投票限制时间，单位小时，为空不允许重复投票',
  `total_count` int(11) NOT NULL DEFAULT '0' COMMENT '总投票数',
  `multi_select` int(11) NOT NULL DEFAULT '1' COMMENT '最多可以选择几项',
  `is_restrict_member` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制会员',
  `is_restrict_ip` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制IP',
  `is_restrict_cookie` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制COOKIE',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_def` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认主题',
  PRIMARY KEY (`votetopic_id`),
  KEY `fk_jc_votetopic_site` (`site_id`),
  CONSTRAINT `fk_jc_votetopic_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS投票主题';

--
-- Dumping data for table `jc_vote_topic`
--

/*!40000 ALTER TABLE `jc_vote_topic` DISABLE KEYS */;
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`) VALUES 
 (1,1,'您为什么选择jsp cms,java cms? ','在php cms为建站主流CMS的年代，您为什么选择jsp cms,java cms？请给出您的意见吧！',NULL,NULL,NULL,76,3,0,0,1,0,1);
/*!40000 ALTER TABLE `jc_vote_topic` ENABLE KEYS */;


--
-- Definition of table `jo_authentication`
--

DROP TABLE IF EXISTS `jo_authentication`;
CREATE TABLE `jo_authentication` (
  `authentication_id` char(32) NOT NULL COMMENT '认证ID',
  `userid` int(11) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) NOT NULL COMMENT '登录ip',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证信息表';

--
-- Dumping data for table `jo_authentication`
--

/*!40000 ALTER TABLE `jo_authentication` DISABLE KEYS */;
INSERT INTO `jo_authentication` (`authentication_id`,`userid`,`username`,`email`,`login_time`,`login_ip`,`update_time`) VALUES 
 ('37aba1df70fd4925bc494f805a2a35c2',1,'admin','admin@yahoo.com','2011-09-18 23:00:57','0:0:0:0:0:0:0:1','2011-09-18 23:07:39');
/*!40000 ALTER TABLE `jo_authentication` ENABLE KEYS */;


--
-- Definition of table `jo_config`
--

DROP TABLE IF EXISTS `jo_config`;
CREATE TABLE `jo_config` (
  `cfg_key` varchar(50) NOT NULL COMMENT '配置KEY',
  `cfg_value` varchar(255) DEFAULT NULL COMMENT '配置VALUE',
  PRIMARY KEY (`cfg_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';

--
-- Dumping data for table `jo_config`
--

/*!40000 ALTER TABLE `jo_config` DISABLE KEYS */;
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES 
 ('email_encoding',''),
 ('email_host','smtp.163.com'),
 ('email_password','1'),
 ('email_personal',''),
 ('email_port',NULL),
 ('email_username','jeecms@163.com'),
 ('login_error_interval','30'),
 ('login_error_times','3'),
 ('message_subject','JEECMS会员密码找回信息'),
 ('message_text','感谢您使用JEECMS系统会员密码找回功能，请记住以下找回信息：\r\n用户ID：${uid}\r\n用户名：${username}\r\n您的新密码为：${resetPwd}\r\n请访问如下链接新密码才能生效：\r\nhttp://localhost/member/password_reset.jspx?uid=${uid}&key=${resetKey}\r\n');
/*!40000 ALTER TABLE `jo_config` ENABLE KEYS */;


--
-- Definition of table `jo_ftp`
--

DROP TABLE IF EXISTS `jo_ftp`;
CREATE TABLE `jo_ftp` (
  `ftp_id` int(11) NOT NULL AUTO_INCREMENT,
  `ftp_name` varchar(100) NOT NULL COMMENT '名称',
  `ip` varchar(50) NOT NULL COMMENT 'IP',
  `port` int(11) NOT NULL DEFAULT '21' COMMENT '端口号',
  `username` varchar(100) DEFAULT NULL COMMENT '登录名',
  `password` varchar(100) DEFAULT NULL COMMENT '登陆密码',
  `encoding` varchar(20) NOT NULL DEFAULT 'UTF-8' COMMENT '编码',
  `timeout` int(11) DEFAULT NULL COMMENT '超时时间',
  `ftp_path` varchar(255) DEFAULT NULL COMMENT '路径',
  `url` varchar(255) NOT NULL COMMENT '访问URL',
  PRIMARY KEY (`ftp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='FTP表';

--
-- Dumping data for table `jo_ftp`
--

/*!40000 ALTER TABLE `jo_ftp` DISABLE KEYS */;
/*!40000 ALTER TABLE `jo_ftp` ENABLE KEYS */;


--
-- Definition of table `jo_template`
--

DROP TABLE IF EXISTS `jo_template`;
CREATE TABLE `jo_template` (
  `tpl_name` varchar(150) NOT NULL COMMENT '模板名称',
  `tpl_source` longtext COMMENT '模板内容',
  `last_modified` bigint(20) NOT NULL COMMENT '最后修改时间',
  `is_directory` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否目录',
  PRIMARY KEY (`tpl_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表';

--
-- Dumping data for table `jo_template`
--

/*!40000 ALTER TABLE `jo_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `jo_template` ENABLE KEYS */;


--
-- Definition of table `jo_upload`
--

DROP TABLE IF EXISTS `jo_upload`;
CREATE TABLE `jo_upload` (
  `filename` varchar(150) NOT NULL COMMENT '文件名',
  `length` int(11) NOT NULL COMMENT '文件大小(字节)',
  `last_modified` bigint(20) NOT NULL COMMENT '最后修改时间',
  `content` longblob NOT NULL COMMENT '内容',
  PRIMARY KEY (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传附件表';

--
-- Dumping data for table `jo_upload`
--

/*!40000 ALTER TABLE `jo_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `jo_upload` ENABLE KEYS */;


--
-- Definition of table `jo_user`
--

DROP TABLE IF EXISTS `jo_user`;
CREATE TABLE `jo_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `password` char(32) NOT NULL COMMENT '密码',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `reset_key` char(32) DEFAULT NULL COMMENT '重置密码KEY',
  `reset_pwd` varchar(10) DEFAULT NULL COMMENT '重置密码VALUE',
  `error_time` datetime DEFAULT NULL COMMENT '登录错误时间',
  `error_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录错误次数',
  `error_ip` varchar(50) DEFAULT NULL COMMENT '登录错误IP',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户表';

--
-- Dumping data for table `jo_user`
--

/*!40000 ALTER TABLE `jo_user` DISABLE KEYS */;
INSERT INTO `jo_user` (`user_id`,`username`,`email`,`password`,`register_time`,`register_ip`,`last_login_time`,`last_login_ip`,`login_count`,`reset_key`,`reset_pwd`,`error_time`,`error_count`,`error_ip`) VALUES 
 (1,'admin','admin@yahoo.com','5f4dcc3b5aa765d61d8327deb882cf99','2011-01-03 00:00:00','127.0.0.1','2011-09-18 23:00:57','0:0:0:0:0:0:0:1',61,NULL,NULL,NULL,0,NULL),
 (4,'korven','811459917@qq.com','dda1cb7e1d1363b0239cb9f6c799bf59','2011-06-04 15:42:10','127.0.0.1','2011-06-04 16:33:02','127.0.0.1',3,NULL,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `jo_user` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
