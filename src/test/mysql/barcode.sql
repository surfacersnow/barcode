-- MySQL dump 10.13  Distrib 5.6.33, for Linux (x86_64)
--
-- Host: localhost    Database: barcode
-- ------------------------------------------------------
-- Server version	5.6.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cm_dict`
--

DROP TABLE IF EXISTS `cm_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_dict` (
  `id` varchar(36) NOT NULL,
  `code` varchar(30) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` varchar(2) NOT NULL,
  `status` varchar(2) NOT NULL,
  `note` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `AK_cm_dict_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_dict`
--

LOCK TABLES `cm_dict` WRITE;
/*!40000 ALTER TABLE `cm_dict` DISABLE KEYS */;
/*!40000 ALTER TABLE `cm_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_dict_line`
--

DROP TABLE IF EXISTS `cm_dict_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_dict_line` (
  `id` varchar(36) NOT NULL,
  `code` varchar(30) DEFAULT NULL,
  `dict_id` varchar(36) DEFAULT NULL,
  `dict_line_id` varchar(36) NOT NULL,
  `name` varchar(40) NOT NULL,
  `value` varchar(40) NOT NULL,
  `seq` int(11) DEFAULT NULL,
  `path` varchar(36) DEFAULT NULL,
  `layer` int(11) DEFAULT NULL,
  `detail` char(1) DEFAULT '1',
  `status` varchar(2) DEFAULT NULL,
  `note` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_dict_line`
--

LOCK TABLES `cm_dict_line` WRITE;
/*!40000 ALTER TABLE `cm_dict_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `cm_dict_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_menu`
--

DROP TABLE IF EXISTS `cm_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_menu` (
  `ID` varchar(36) NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `HIGHER_ID` varchar(36) NOT NULL,
  `SEQ` int(11) DEFAULT NULL,
  `RESOURCE_ID` varchar(36) DEFAULT NULL,
  `ICON_URI` varchar(200) DEFAULT NULL,
  `HINT` varchar(50) DEFAULT NULL,
  `Path` varchar(36) DEFAULT NULL,
  `Layer` int(11) DEFAULT NULL,
  `Detail` char(1) DEFAULT '1',
  `STATUS` varchar(2) NOT NULL,
  `DESCRIPTIOIN` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_menu`
--

LOCK TABLES `cm_menu` WRITE;
/*!40000 ALTER TABLE `cm_menu` DISABLE KEYS */;
INSERT INTO `cm_menu` VALUES ('100101','100101','个人信息维护','0000',1,'100101','','0','00',0,'1','1',''),('100102','100102','用户管理','0000',2,'100102','','0','00',0,'1','1',''),('1002','1002','系统管理','0000',0,'1002','','0','00',0,'0','1',''),('100201','100201','机构管理','1002',1,'100201','','0','00',1,'1','1',''),('100202','100202','菜单管理','1002',2,'100202','','0','00',1,'1','1',''),('100203','100203','资源管理','1002',3,'100203','','0','00',1,'1','1',''),('100301','100301','条码维护','1003',1,'100301','','0','00',0,'1','1',''),('100302','100302','生产条码打印','0000',2,'100302','','1','00',0,'1','1',''),('100303','100303','生产条码批量打印','0000',3,'100303','','1','00',0,'1','1',''),('100304','100304','发货条码打印','0000',4,'100304','','1','00',0,'1','1',''),('100305','100305','发货条码批量打印','0000',5,'100305','','1','00',0,'1','1',''),('100306','100306','发货单管理','0000',6,'100306','','0','00',0,'1','1',''),('100307','100307','扫码发货','0000',7,'100307','','1','00',0,'1','1','');
/*!40000 ALTER TABLE `cm_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_org`
--

DROP TABLE IF EXISTS `cm_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_org` (
  `ID` varchar(36) NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `NAME` varchar(60) NOT NULL,
  `SHORTFOR` varchar(20) DEFAULT NULL,
  `HIGHER_ID` varchar(36) DEFAULT NULL,
  `ORG_TYPE` varchar(2) DEFAULT NULL,
  `DIRECTOR` varchar(20) DEFAULT NULL,
  `ADDRESS` varchar(200) DEFAULT NULL,
  `TEL` varchar(20) DEFAULT NULL,
  `FAX` varchar(20) DEFAULT NULL,
  `ZIPCODE` varchar(10) DEFAULT NULL,
  `Path` varchar(36) DEFAULT NULL,
  `Layer` int(11) DEFAULT NULL,
  `Detail` char(1) DEFAULT '1',
  `STATUS` varchar(2) NOT NULL,
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_org`
--

LOCK TABLES `cm_org` WRITE;
/*!40000 ALTER TABLE `cm_org` DISABLE KEYS */;
INSERT INTO `cm_org` VALUES ('0001','01','总公司','总公司',NULL,'0',NULL,'地址测试','124434','354343','343434','01',0,'0','1','erer');
/*!40000 ALTER TABLE `cm_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_org_resource_rel`
--

DROP TABLE IF EXISTS `cm_org_resource_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_org_resource_rel` (
  `ORG_ID` int(11) NOT NULL,
  `RESOURCE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ORG_ID`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_org_resource_rel`
--

LOCK TABLES `cm_org_resource_rel` WRITE;
/*!40000 ALTER TABLE `cm_org_resource_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `cm_org_resource_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_resource`
--

DROP TABLE IF EXISTS `cm_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_resource` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `URI` varchar(200) NOT NULL,
  `TYPE` varchar(2) NOT NULL,
  `STATUS` varchar(2) NOT NULL,
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_resource`
--

LOCK TABLES `cm_resource` WRITE;
/*!40000 ALTER TABLE `cm_resource` DISABLE KEYS */;
INSERT INTO `cm_resource` VALUES ('100101','个人信息维护','./html/system/user_person_manager.html','1','1',''),('100102','用户管理','./html/system/user_manager.html','1','1',''),('1002','系统管理','','1','1',''),('100201','机构管理','./html/sysmgt/org.html','1','1',''),('100202','菜单管理','./html/sysmgt/menu.html','1','1',''),('100203','资源管理','./html/sysmgt/resource.html','1','1',''),('100301','条码维护','./html/barcode/tmwh.html','1','1',''),('100302','生产条码打印','./html/barcode/dsctmdy.html','1','1',''),('100303','发货条码批量打印','./html/barcode/duosctmdy.html','1','1',''),('100304','发货条码打印','./html/barcode/dfhtmdy.html','1','1',''),('100305','发货条码批量打印','./html/barcode/duofhtmdy.html','1','1',''),('100306','发货单管理','./html/barcode/fhdgl.html','1','1',''),('100307','扫码发货','./html/barcode/smfh.html','1','1','');
/*!40000 ALTER TABLE `cm_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_role`
--

DROP TABLE IF EXISTS `cm_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_role` (
  `ID` varchar(36) NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `STATUS` varchar(2) NOT NULL,
  `IS_EXT` varchar(2) NOT NULL,
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_role`
--

LOCK TABLES `cm_role` WRITE;
/*!40000 ALTER TABLE `cm_role` DISABLE KEYS */;
INSERT INTO `cm_role` VALUES ('0001','sysadmin','用户管理','0','1','null'),('0002','zhijy','质检员','1','1','null'),('0003','weihy','条码维护员','1','1','null'),('0004','zhuangxy','装箱员','1','1','null'),('0005','fahy','发货员','1','1','null'),('0006','geren','个人信息维护','1','1','null');
/*!40000 ALTER TABLE `cm_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_role_resource_rel`
--

DROP TABLE IF EXISTS `cm_role_resource_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_role_resource_rel` (
  `ROLE_ID` varchar(36) NOT NULL,
  `RESOURCE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_role_resource_rel`
--

LOCK TABLES `cm_role_resource_rel` WRITE;
/*!40000 ALTER TABLE `cm_role_resource_rel` DISABLE KEYS */;
INSERT INTO `cm_role_resource_rel` VALUES ('0001','100102'),('0002','100302'),('0002','100303'),('0003','100301'),('0004','100304'),('0004','100305'),('0005','100306'),('0005','100307'),('0006','100101');
/*!40000 ALTER TABLE `cm_role_resource_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_user`
--

DROP TABLE IF EXISTS `cm_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_user` (
  `ID` varchar(36) NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `ACCOUNT` varchar(20) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `ORGID` varchar(36) NOT NULL,
  `PASSWD` varchar(100) NOT NULL,
  `STATUS` varchar(2) NOT NULL,
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  `deptName` varchar(256) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_user`
--

LOCK TABLES `cm_user` WRITE;
/*!40000 ALTER TABLE `cm_user` DISABLE KEYS */;
INSERT INTO `cm_user` VALUES ('000001','admin','admin','系统管理员','0001','21232F297A57A5A743894A0E4A801FC3','1','系统自动添加的管理员','轮毂厂','sss1@126.com'),('20170105142244-6b5968fdbc034e1c','cyetong','cyetong','陈业同','','C4CA4238A0B923820DCC509A6F75849B','1','质检员负责涂装下线时通过“生产条码打印”功能，录入轮毂上的产品码（钢印编码），打印生产条码，贴在轮毂的钢印下面','泰安和新精工科技有限公司',''),('20170105142345-94f2b3706e73454a','bhbin','bhbin','鲍怀彬','','C4CA4238A0B923820DCC509A6F75849B','1','负责维护条码相关的集成信息','泰安和新精工科技有限公司','');
/*!40000 ALTER TABLE `cm_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_user_org_rel`
--

DROP TABLE IF EXISTS `cm_user_org_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_user_org_rel` (
  `USER_ID` varchar(36) NOT NULL,
  `ORG_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`USER_ID`,`ORG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_user_org_rel`
--

LOCK TABLES `cm_user_org_rel` WRITE;
/*!40000 ALTER TABLE `cm_user_org_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `cm_user_org_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cm_user_role_rel`
--

DROP TABLE IF EXISTS `cm_user_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_user_role_rel` (
  `USER_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`USER_ID`,`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_user_role_rel`
--

LOCK TABLES `cm_user_role_rel` WRITE;
/*!40000 ALTER TABLE `cm_user_role_rel` DISABLE KEYS */;
INSERT INTO `cm_user_role_rel` VALUES ('000001','0001'),('000001','0002'),('000001','0003'),('000001','0004'),('000001','0005'),('000001','0006'),('20170105142244-6b5968fdbc034e1c','0002'),('20170105142244-6b5968fdbc034e1c','0004'),('20170105142244-6b5968fdbc034e1c','0005'),('20170105142244-6b5968fdbc034e1c','0006'),('20170105142345-94f2b3706e73454a','0003'),('20170105142345-94f2b3706e73454a','0006');
/*!40000 ALTER TABLE `cm_user_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_barcode`
--

DROP TABLE IF EXISTS `crm_barcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_barcode` (
  `BARCODE_KHMC` varchar(256) DEFAULT NULL,
  `BARCODE_SC` varchar(20) NOT NULL,
  `BARCODE_KHBH` varchar(20) NOT NULL,
  `BARCODE_UPC` varchar(20) NOT NULL,
  `BARCODE_KS` varchar(32) NOT NULL,
  `BARCODE_GG` varchar(32) NOT NULL,
  `BARCODE_PCD` varchar(32) NOT NULL,
  `BARCODE_ZXK` varchar(32) NOT NULL,
  `BARCODE_PJ` varchar(32) NOT NULL,
  `BARCODE_DJZ` decimal(20,6) DEFAULT '0.000000',
  `BARCODE_MJZ` decimal(20,6) DEFAULT '0.000000',
  `BARCODE_DJT` decimal(20,6) DEFAULT '0.000000',
  `BARCODE_BMZT` varchar(60) DEFAULT NULL,
  `BARCODE_BMZTYW` varchar(256) DEFAULT NULL,
  `BARCODE_BZFS` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`BARCODE_KHBH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_barcode`
--

LOCK TABLES `crm_barcode` WRITE;
/*!40000 ALTER TABLE `crm_barcode` DISABLE KEYS */;
INSERT INTO `crm_barcode` VALUES ('WPR','172157002P','AR1725761','761138486752','172','15X7','5X120.65','83.06','-6',7.340000,8.920000,0.051000,'抛光','POLISHED','纸箱散装'),('WPR','172151001S','AR1727973B','885463136035','172','17X9','5X127','83.06','-12',11.660000,13.740000,0.075000,'沙丁黑全涂','SATIN BLACK','纸箱散装'),('WPR','229179001S','XD22979050738N','885463164861','229','17X9','5X127','78.3','-38',16.900000,22.600000,0.086000,'沙丁黑配开槽安装环精车','SATIN BLACK WITH SLOTTED RING WITH NO FINISH','纸箱散装'),('WPR','825201006X','XD82521035324N','885463137476','825','20X10','5X139.7','78.3','-24',17.500000,20.240000,0.111000,'亮黑铣窗口','GLOSS BLACK MILLED','纸箱散装');
/*!40000 ALTER TABLE `crm_barcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_order`
--

DROP TABLE IF EXISTS `crm_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_order` (
  `order_ckdh` varchar(20) NOT NULL,
  `order_khmc` varchar(60) NOT NULL,
  `order_mdh` varchar(60) NOT NULL,
  `order_gh` varchar(60) NOT NULL,
  `order_fh` varchar(60) NOT NULL,
  `order_fhrq` varchar(20) NOT NULL,
  `order_fhzt` varchar(20) NOT NULL,
  `order_fhdy` int(11) DEFAULT '0',
  `order_zdsj` varchar(20) DEFAULT NULL,
  `order_zdr` varchar(60) DEFAULT NULL,
  `order_qrsj` varchar(20) DEFAULT NULL,
  `order_qrr` varchar(60) DEFAULT NULL,
  `order_tdh` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`order_ckdh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_order`
--

LOCK TABLES `crm_order` WRITE;
/*!40000 ALTER TABLE `crm_order` DISABLE KEYS */;
INSERT INTO `crm_order` VALUES ('20160101','WPR','目的港','柜号','封号','2017-01-05','1',0,'2017-01-03 20:59:29','222',NULL,NULL),('20160102','WPR','RRE','ERE','RTR','2017-01-05','2',0,'2017-01-01 20:55:13','ERE',NULL,NULL),('22','44','33','55','66','1899-12-06','2',0,'2017-01-03 21:13:26','65',NULL,NULL);
/*!40000 ALTER TABLE `crm_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_order_detail`
--

DROP TABLE IF EXISTS `crm_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_order_detail` (
  `orderDetail_id` varchar(32) NOT NULL,
  `orderDetail_ckdh` varchar(20) NOT NULL,
  `orderDetail_khbh` varchar(20) NOT NULL,
  `orderDetail_mtgs` int(11) DEFAULT NULL,
  `orderDetail_sl` int(11) DEFAULT NULL,
  `orderDetail_bzsj` varchar(20) DEFAULT NULL,
  `orderDetail_bzr` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`orderDetail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_order_detail`
--

LOCK TABLES `crm_order_detail` WRITE;
/*!40000 ALTER TABLE `crm_order_detail` DISABLE KEYS */;
INSERT INTO `crm_order_detail` VALUES ('006629bf326745e8865c0dc649322908','20160101','AR1725761',1,1,'2017-01-03 21:20:57','admin'),('06ee873826614a0ebc92a43fa06a5366','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('081655c249bf4dd2b6fd0b77412ed060','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('09f9c82df13042c983d367dc681d5e12','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('0d9b10fae9c64a3fb781abaa867b0d38','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('17f4694094a5405a8948f77b7adef102','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('1ad45962b03c4e9891b77fa562d7de77','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('1fff1be46e01495a9c3cf52092e8224c','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('27b16793484f4797afb6b63cb8575acc','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('2b53c88329924f338c9e085b5f179650','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('30a3b0414b434de58c575f1d961d4d3c','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('3110895e225e41498bf23e83146154a1','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('315ed48510794583a44746e4a21a8839','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('320cd2d788104e25b88ef571a531cfaf','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('33d58a603cdf40f8a65134066dda600c','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('343eb3a174984151a86b9423b858f397','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('390e34b98d4d4dc688454c9c3565538a','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('3c6415c0b0a24ae38e10d1b3dee27f05','22','AR1725761',2,1,'2017-01-03 21:17:11','admin'),('3cf5814e422842628993d9127b1fd005','20160101','AR1725761',3,1,'2017-01-04 10:32:17','admin'),('3ed5aae3c3e2489d95acf152e675844c','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('40fdbc2e52cb4207a4225ffdd98a9406','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('4154694ff1d1421c84ddf9cfdc4e1ffe','20160101','AR1725761',2,1,'2017-01-03 22:47:33','admin'),('41a8f7a99af24a1d9744fb36348d1e33','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('45320b4382444946926c7fa9ba8252c6','20160101','AR1725761',2,1,'2017-01-03 22:47:33','admin'),('4880a6f37adc473284423fd2ad92e670','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('48ea6cfed912448aa85fdddcd3823168','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('5b9ccbcfa89a4515bb4826549216799b','20160101','AR1725761',2,1,'2017-01-03 22:47:33','admin'),('5e8208091d7549dcbef41dec5320177f','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('5ef83489ca234fc1af0e8f3c193d42a3','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('5fbc94f7b3a7477f8b43718e2977c5df','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('62657234e54842aba2329476dc4a6530','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('636f3557de7f4a0da911ae78459589f7','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('64f9ca165a6a4121b23df14938ae68ae','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('6da041dd10844b9cb7750d9c4f3eacba','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('70f09056069f436bb4ca6689c5a8ad46','20160101','AR1725761',2,1,'2017-01-03 22:47:33','admin'),('73df5d1a844848d0a41c706a637ce689','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('740b115caac5479d95775829f1ebd217','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('74414cc5fe88484f96ab67d7d3632b11','20160101','AR1725761',3,1,'2017-01-04 10:32:17','admin'),('76d671295c6a4b0ca511503b962e8e59','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('7a1f07d0627f4399933a02d21aa81795','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('893004aadba54b4794e605c96dc7348d','20160101','AR1725761',6,1,'2017-01-05 07:58:36','001'),('8ad9a7901e1c4642bc1fa2e0c50f29e4','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('8c91a7d9663d4c2d8e61a01000236ad0','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('90e44e08449f431daec9285eb8a5bd93','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('945cb13c72794f2f839f0aefbeb33156','20160101','AR1725761',4,1,'2017-01-04 10:32:29','admin'),('94e438257bfe4c76b69e307202b1d00b','20160101','AR1725761',5,1,'2017-01-04 20:42:35','admin'),('9954944ab1c14f6d9a0230a9f45c186a','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('9bcf08d77e654feb86fa30bf4b710bd5','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('9bdd133d7d0d41728cc0f68a71dfc3b3','20160101','AR1725761',5,1,'2017-01-04 20:42:35','admin'),('9c73224e4e044727b758daefd46e5f66','20160101','AR1725761',4,1,'2017-01-04 10:32:29','admin'),('a2f3abceed054a67b579c39156ab07e5','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('a57c6bfd3cb746e69536f3a7c8b42ae3','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('a5fdf71062c34281a0b59fd6a2fe2f15','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('ab561e264f1145c29ce5f2e6cb53258d','20160101','AR1725761',3,1,'2017-01-04 10:32:17','admin'),('b3adbd7ae0ce47e688340d8ffee300e1','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('b971d108dd404adb825d6b641d015462','20160102','AR1725761',2,1,'2017-01-01 21:11:03','admin'),('b97a3d79887540f5846a80ae7c46e34d','20160101','AR1725761',5,1,'2017-01-04 20:42:35','admin'),('bdb41cfde29842238385c774dfca1ed7','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('bfae7fc47a8947efabae53cfcb1e271b','22','AR1725761',2,1,'2017-01-03 21:17:11','admin'),('bfd5f21901f844e2af1cdb4381c8b592','20160101','AR1725761',2,1,'2017-01-03 22:47:33','admin'),('c64a930f101341c1a66d31401c322d35','20160101','AR1725761',3,1,'2017-01-04 10:32:17','admin'),('d4fae34ee974430da390c5971a4c732c','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('d604d3d385034a6c9b049b6e54a23cc1','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('dcb8d0c4a92641d890b3670527389eb7','20160101','AR1725761',2,1,'2017-01-03 22:47:33','admin'),('e12f44be81a34deaa98713721a664021','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('e3a7c48cd0e745b7b4c96c3efbfe48ba','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('e4b2680851e141e8a74b0c31ada6c5d6','20160101','AR1725761',4,1,'2017-01-04 10:32:29','admin'),('e4ce5cf6ed4945d8868b314cf24ee1c9','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('ef04b9024ff04e6e8bcfa2e3a177b1f9','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('f10294439e6b49a28ed43593ab70e6eb','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('f15e240c88ff41409205fe614330941a','20160102','AR1725761',1,1,'2017-01-01 21:10:50','admin'),('f38d6823250040e4b6a0bdd162d63be3','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('f713df59dfa7446b8035ab905e939041','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('fadaabffbf634882b5c1ff6a49b51c81','22','AR1725761',1,1,'2017-01-03 21:16:52','admin'),('fd74ed191ba6434085021678eb531083','22','AR1725761',1,1,'2017-01-03 21:16:52','admin');
/*!40000 ALTER TABLE `crm_order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_order_detail_sum`
--

DROP TABLE IF EXISTS `crm_order_detail_sum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_order_detail_sum` (
  `detailsum_id` varchar(32) NOT NULL,
  `detailsum_ckdh` varchar(20) NOT NULL,
  `detailsum_khbh` varchar(20) NOT NULL,
  `detailsum_sl` int(11) DEFAULT NULL,
  `detailsum_zmz` decimal(20,6) DEFAULT '0.000000',
  `detailsum_zjz` decimal(20,6) DEFAULT '0.000000',
  `detailsum_ztj` decimal(20,6) DEFAULT '0.000000',
  `detailsum_bzsj` varchar(20) DEFAULT NULL,
  `detailsum_bzr` varchar(60) DEFAULT NULL,
  `detailsum_xgsj` varchar(20) DEFAULT NULL,
  `detailsum_xgr` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`detailsum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_order_detail_sum`
--

LOCK TABLES `crm_order_detail_sum` WRITE;
/*!40000 ALTER TABLE `crm_order_detail_sum` DISABLE KEYS */;
INSERT INTO `crm_order_detail_sum` VALUES ('0265d20a0dd84a6b9a72f2e93803622','20160102','AR1727973B',18,160.560000,132.120000,0.918000,'2017-01-01 21:11:03','admin','2017-01-01 21:11:03','admin'),('0265d20a0dd84a6b9a72f2e938036c43','20160102','AR1725761',18,160.560000,132.120000,0.918000,'2017-01-01 21:11:03','admin','2017-01-01 21:11:03','admin'),('555b13b8a4f84f818b45003ec1c1a52c','20160101','AR1725761',18,160.560000,132.120000,0.918000,'2017-01-05 07:58:36','001','2017-01-05 07:58:36','001'),('df0ace3ff7b74cc6a01991956e43d81c','22','AR1725761',39,347.880000,286.260000,1.989000,'2017-01-03 21:17:11','admin','2017-01-03 21:17:11','admin');
/*!40000 ALTER TABLE `crm_order_detail_sum` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-06 21:40:02
