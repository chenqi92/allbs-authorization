-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: authorization_demo
-- ------------------------------------------------------
-- Server version	5.7.27-3-log

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED='b5f266a0-4bb8-11ea-b0cf-fa163e6957ec:1-38986133';

--
-- Table structure for table `cm_ent_data`
--

DROP TABLE IF EXISTS `cm_ent_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_ent_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ent_id` bigint(20) NOT NULL COMMENT '企业id',
  `description` varchar(200) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='用户数据测试的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_ent_data`
--

/*!40000 ALTER TABLE `cm_ent_data` DISABLE KEYS */;
INSERT INTO `cm_ent_data` VALUES (1,1,'企业1的测试数据1号'),(2,2,'企业2的测试数据1号'),(3,3,'企业3的测试数据1号'),(4,4,'企业4的测试数据1号'),(5,5,'企业5的测试数据1号'),(6,2,'企业2的测试数据2号'),(7,3,'企业3的测试数据2号');
/*!40000 ALTER TABLE `cm_ent_data` ENABLE KEYS */;

--
-- Table structure for table `cm_enterprise`
--

DROP TABLE IF EXISTS `cm_enterprise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_enterprise` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '企业名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_enterprise`
--

/*!40000 ALTER TABLE `cm_enterprise` DISABLE KEYS */;
INSERT INTO `cm_enterprise` VALUES (1,'企业1','2023-03-01 09:33:48','2023-03-01 09:33:58','0'),(2,'企业2','2023-03-01 09:33:50','2023-03-01 09:33:59','0'),(3,'企业3','2023-03-01 09:33:55','2023-03-01 09:34:00','0'),(4,'企业4','2023-03-01 09:33:56','2023-03-01 09:34:01','0'),(5,'企业5','2023-03-01 09:33:57','2023-03-01 09:34:02','0');
/*!40000 ALTER TABLE `cm_enterprise` ENABLE KEYS */;

--
-- Table structure for table `cm_user_enterprise`
--

DROP TABLE IF EXISTS `cm_user_enterprise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cm_user_enterprise` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ent_id` bigint(20) NOT NULL COMMENT '企业id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户企业关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cm_user_enterprise`
--

/*!40000 ALTER TABLE `cm_user_enterprise` DISABLE KEYS */;
INSERT INTO `cm_user_enterprise` VALUES (1,1),(1,2),(1,3),(2,3),(2,4),(2,5);
/*!40000 ALTER TABLE `cm_user_enterprise` ENABLE KEYS */;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(32) DEFAULT NULL,
  `permission` varchar(32) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL,
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive` char(1) DEFAULT '0',
  `type` char(1) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10013 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1000,'权限管理',NULL,'/user',-1,'icon-quanxianguanli',0,'0','0','2023-02-01 08:29:53','2023-02-01 08:29:53','0'),(1100,'用户管理',NULL,'/admin/user/index',1000,'icon-yonghuguanli',1,'1','1','2023-02-01 08:29:53','2023-02-02 09:38:50','0'),(1101,'用户新增','sys_user_add',NULL,1100,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:38:54','0'),(1102,'用户修改','sys_user_edit',NULL,1100,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:38:54','0'),(1103,'用户删除','sys_user_del',NULL,1100,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:38:54','0'),(1200,'菜单管理',NULL,'/admin/menu/index',1000,'icon-caidanguanli',2,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:39','0'),(1201,'菜单新增','sys_menu_add',NULL,1200,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:17','0'),(1202,'菜单修改','sys_menu_edit',NULL,1200,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:17','0'),(1203,'菜单删除','sys_menu_del',NULL,1200,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:17','0'),(1300,'角色管理',NULL,'/admin/role/index',1000,'icon-jiaoseguanli',3,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:38','0'),(1301,'角色新增','sys_role_add',NULL,1300,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:20','0'),(1302,'角色修改','sys_role_edit',NULL,1300,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:20','0'),(1303,'角色删除','sys_role_del',NULL,1300,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:20','0'),(1304,'分配权限','sys_role_perm',NULL,1300,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:20','0'),(1400,'部门管理',NULL,'/admin/dept/index',1000,'icon-web-icon-',4,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:36','0'),(1401,'部门新增','sys_dept_add',NULL,1400,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:22','0'),(1402,'部门修改','sys_dept_edit',NULL,1400,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:23','0'),(1403,'部门删除','sys_dept_del',NULL,1400,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:23','0'),(1500,'租户管理','','/admin/tenant/index',1000,'icon-erji-zuhushouye',5,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:34','0'),(1501,'租户新增','admin_systenant_add',NULL,1500,'1',0,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:25','0'),(1502,'租户修改','admin_systenant_edit',NULL,1500,'1',1,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:25','0'),(1503,'租户删除','admin_systenant_del',NULL,1500,'1',2,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:25','0'),(2000,'系统管理',NULL,'/admin',-1,'icon-xitongguanli',1,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:33','0'),(2100,'日志管理',NULL,'/admin/log/index',2000,'icon-rizhiguanli',5,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:33','0'),(2101,'日志删除','sys_log_del',NULL,2100,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:27','0'),(2200,'字典管理',NULL,'/admin/dict/index',2000,'icon-navicon-zdgl',6,'0','1','2023-02-01 08:29:53','2023-02-02 09:39:30','0'),(2201,'字典删除','sys_dict_del',NULL,2200,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:29','0'),(2202,'字典新增','sys_dict_add',NULL,2200,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:29','0'),(2203,'字典修改','sys_dict_edit',NULL,2200,NULL,NULL,'0','2','2023-02-01 08:29:53','2023-02-02 09:39:29','0');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) DEFAULT NULL,
  `role_code` varchar(64) DEFAULT NULL,
  `role_desc` varchar(255) DEFAULT NULL,
  `ds_type` char(1) DEFAULT '2',
  `ds_scope` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'管理员','ROLE_ADMIN','管理员','0','2','2023-02-01 15:45:51','2023-02-01 14:09:11','0');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1000),(1,1100),(1,1101),(1,1102),(1,1103),(1,1200),(1,1201),(1,1202),(1,1203),(1,1300),(1,1301),(1,1302),(1,1303),(1,1304),(1,1400),(1,1401),(1,1402),(1,1403),(1,1500),(1,1501),(1,1502),(1,1503),(1,2000),(1,2100),(1,2101),(1,2200),(1,2201),(1,2202),(1,2203);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `lock_flag` char(1) DEFAULT '0',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$IVzj1Wd.ZQdOIWdb1htQjexU94uoNeuk1crlQ9ExVupPi0Iy1uv.C','','18066012345','sadxc ',1,'2023-02-01 07:15:18','2023-03-01 09:35:53','0','0'),(2,'test','$2a$10$IVzj1Wd.ZQdOIWdb1htQjexU94uoNeuk1crlQ9ExVupPi0Iy1uv.C',NULL,'13812345678','sdsdfwew',1,'2023-03-01 09:35:31','2023-03-01 09:35:34','0','0');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-06  9:45:38
