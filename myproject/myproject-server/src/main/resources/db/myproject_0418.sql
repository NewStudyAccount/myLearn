/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.99.100
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : 192.168.99.100:3306
 Source Schema         : myproject

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 18/04/2026 01:15:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_article
-- ----------------------------
DROP TABLE IF EXISTS `sys_article`;
CREATE TABLE `sys_article`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '文章名',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '预览图',
  `is_deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '删除标志',
  `read_num` int NULL DEFAULT 0 COMMENT '阅读次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_article
-- ----------------------------

-- ----------------------------
-- Table structure for sys_article_category_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_article_category_rel`;
CREATE TABLE `sys_article_category_rel`  (
  `id` bigint NOT NULL COMMENT 'id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `article_id` bigint NOT NULL COMMENT '文章id',
  PRIMARY KEY (`article_id`, `category_id`) USING BTREE,
  UNIQUE INDEX `sys_article_category_rel_pk`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_article_category_rel
-- ----------------------------

-- ----------------------------
-- Table structure for sys_article_content
-- ----------------------------
DROP TABLE IF EXISTS `sys_article_content`;
CREATE TABLE `sys_article_content`  (
  `id` bigint NOT NULL COMMENT '文章内容id',
  `article_id` bigint NOT NULL COMMENT '文章id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL COMMENT '文章内容',
  PRIMARY KEY (`article_id`) USING BTREE,
  UNIQUE INDEX `sys_article_content_pk`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '文章内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_article_content
-- ----------------------------

-- ----------------------------
-- Table structure for sys_article_tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_article_tag_rel`;
CREATE TABLE `sys_article_tag_rel`  (
  `id` bigint NULL DEFAULT NULL,
  `article_id` bigint NOT NULL COMMENT '文章id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  PRIMARY KEY (`tag_id`, `article_id`) USING BTREE,
  UNIQUE INDEX `sys_article_tag_rel_pk`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '文章标签关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_article_tag_rel
-- ----------------------------

-- ----------------------------
-- Table structure for sys_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_category`;
CREATE TABLE `sys_category`  (
  `id` bigint NOT NULL COMMENT '分类id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '分类名',
  `is_deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '文章分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_category
-- ----------------------------
INSERT INTO `sys_category` VALUES (7346094390013595648, '分类1', NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `menu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '菜单名称',
  `per_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '权限code',
  `menu_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '菜单类型',
  `menu_sort` int NOT NULL COMMENT '排序',
  `parent_id` int NULL DEFAULT NULL COMMENT '父级id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '组件路径',
  `component_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '组件名称',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 's', 'M', 1, 0, '/system', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (2, '文章管理', 's', 'M', 2, 0, '/blog', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (3, 'OSS配置', 's', 'M', 99, 0, '/oss', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (1001, '用户管理', 'test:001', 'C', 1, 1, '/system/user', 'system/user/index', 'User', 0);
INSERT INTO `sys_menu` VALUES (1002, '角色列表', 'confirm:002', 'C', 2, 1, '/system/role', 'system/role/index', 'Role', 0);
INSERT INTO `sys_menu` VALUES (1003, '菜单列表', 'test:002', 'C', 3, 1, '/system/menu', 'system/menu/index', 'Menu', 0);
INSERT INTO `sys_menu` VALUES (2001, '标签列表', 's', 'C', 1, 2, '/blog/tag', 'blog/tag/index', NULL, 0);

-- ----------------------------
-- Table structure for sys_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_config`;
CREATE TABLE `sys_oss_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称（唯一标识）',
  `provider` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供商类型（aliyun、minio等）',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务端点',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '访问密钥',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '秘密密钥',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '存储桶名称',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域（可选）',
  `extra_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'JSON格式的扩展配置',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1启用，0禁用）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_name`(`config_name` ASC) USING BTREE,
  INDEX `idx_provider`(`provider` ASC) USING BTREE,
  INDEX `idx_is_active`(`is_active` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OSS配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss_config
-- ----------------------------
INSERT INTO `sys_oss_config` VALUES (1, 'aliyun-default', 'aliyun', 'https://oss-cn-hangzhou.aliyuncs.com', 'your-access-key-id', 'your-access-key-secret', 'my-bucket', 'cn-hangzhou', NULL, 0, '2026-04-10 15:28:25', '2026-04-10 15:28:25');
INSERT INTO `sys_oss_config` VALUES (2, 'minio-local', 'minio', 'http://192.168.99.100:9000', 'c49ak3akZPzJpI0EoyUs', 'LQX4Cu3IbZd9nTr5g1LGQJYkTa5J3H5PVpQmuMBL', 'my-bucket', NULL, '{\"pathStyleAccess\": true}', 1, '2026-04-10 15:28:25', '2026-04-10 15:28:25');

-- ----------------------------
-- Table structure for sys_oss_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_file`;
CREATE TABLE `sys_oss_file`  (
  `oss_id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `file_suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`oss_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oss_file
-- ----------------------------
INSERT INTO `sys_oss_file` VALUES (1, '0b52a287-cbfe-4e96-b94b-88a8362f6bae.jpg', 'QQ图片20231105182412.jpg', 'jpg', 'https://qjj-learn.oss-cn-shanghai.aliyuncs.com/test2/0b52a287-cbfe-4e96-b94b-88a8362f6bae.jpg', '');
INSERT INTO `sys_oss_file` VALUES (2, '4dcac91a-d2df-4e5b-b926-16998b2d5ed1.jpg', 'QQ图片20231105182412.jpg', 'jpg', 'https://qjj-learn.oss-cn-shanghai.aliyuncs.com/test2/4dcac91a-d2df-4e5b-b926-16998b2d5ed1.jpg', '');
INSERT INTO `sys_oss_file` VALUES (3, 'c76a6bc5-8225-414d-b4d9-a7340034775b.jpg', 'QQ图片20231105182412.jpg', 'jpg', 'https://qjj-learn.oss-cn-shanghai.aliyuncs.com/test3/c76a6bc5-8225-414d-b4d9-a7340034775b.jpg', '');
INSERT INTO `sys_oss_file` VALUES (4, '16288477-fc46-41f7-a5e4-a833becb7f2c.png', 'QQ截图20240407211705.png', 'png', 'http://192.168.99.100:9000/my-bucket/16288477-fc46-41f7-a5e4-a833becb7f2c.png', '');
INSERT INTO `sys_oss_file` VALUES (5, 'f0a5f327-a6c5-4e65-bd4e-eb68325a6d9f.png', 'QQ截图20240407211705.png', 'png', 'http://192.168.99.100:9000/my-bucket/f0a5f327-a6c5-4e65-bd4e-eb68325a6d9f.png', '');
INSERT INTO `sys_oss_file` VALUES (6, 'af2fb449-394f-4624-9104-5495fd97333e.png', 'QQ截图20240407211705.png', 'png', 'http://192.168.99.100:9000/my-bucket/af2fb449-394f-4624-9104-5495fd97333e.png', '');
INSERT INTO `sys_oss_file` VALUES (7, '80b56b98-6297-454d-a93c-671ebe376ad1.png', 'QQ截图20240407211705.png', 'png', 'http://192.168.99.100:9000/my-bucket/80b56b98-6297-454d-a93c-671ebe376ad1.png', 'image/png');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '角色名',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin');
INSERT INTO `sys_role` VALUES (2, 'user');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL,
  `meun_id` int NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '角色-菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 1001);
INSERT INTO `sys_role_menu` VALUES (1, 1002);
INSERT INTO `sys_role_menu` VALUES (1, 1003);
INSERT INTO `sys_role_menu` VALUES (1, 2001);

-- ----------------------------
-- Table structure for sys_tag
-- ----------------------------
DROP TABLE IF EXISTS `sys_tag`;
CREATE TABLE `sys_tag`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '标签名',
  `id_deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '标签' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tag
-- ----------------------------
INSERT INTO `sys_tag` VALUES (7346094977862078464, '标签1', NULL);
INSERT INTO `sys_tag` VALUES (7346097769171390464, '标签2', NULL);
INSERT INTO `sys_tag` VALUES (7346097904244756480, '标签3', NULL);
INSERT INTO `sys_tag` VALUES (7346097918056599552, '标签4', NULL);
INSERT INTO `sys_tag` VALUES (7346097942756855808, '标签5', NULL);

-- ----------------------------
-- Table structure for sys_template_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_template_menu`;
CREATE TABLE `sys_template_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_template_menu
-- ----------------------------
INSERT INTO `sys_template_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2025-07-10 03:21:08', '', NULL, '系统管理目录');
INSERT INTO `sys_template_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2025-07-10 03:21:08', '', NULL, '系统监控目录');
INSERT INTO `sys_template_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2025-07-10 03:21:08', '', NULL, '系统工具目录');
INSERT INTO `sys_template_menu` VALUES (4, '若依官网', 0, 4, 'http://ruoyi.vip', NULL, '', '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', '2025-07-10 03:21:08', '', NULL, '若依官网地址');
INSERT INTO `sys_template_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2025-07-10 03:21:08', '', NULL, '用户管理菜单');
INSERT INTO `sys_template_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2025-07-10 03:21:08', '', NULL, '角色管理菜单');
INSERT INTO `sys_template_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2025-07-10 03:21:08', '', NULL, '菜单管理菜单');
INSERT INTO `sys_template_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2025-07-10 03:21:08', '', NULL, '部门管理菜单');
INSERT INTO `sys_template_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2025-07-10 03:21:08', '', NULL, '岗位管理菜单');
INSERT INTO `sys_template_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2025-07-10 03:21:08', '', NULL, '字典管理菜单');
INSERT INTO `sys_template_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2025-07-10 03:21:08', '', NULL, '参数设置菜单');
INSERT INTO `sys_template_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-07-10 03:21:08', '', NULL, '通知公告菜单');
INSERT INTO `sys_template_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2025-07-10 03:21:08', '', NULL, '日志管理菜单');
INSERT INTO `sys_template_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2025-07-10 03:21:08', '', NULL, '在线用户菜单');
INSERT INTO `sys_template_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2025-07-10 03:21:08', '', NULL, '定时任务菜单');
INSERT INTO `sys_template_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2025-07-10 03:21:08', '', NULL, '数据监控菜单');
INSERT INTO `sys_template_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2025-07-10 03:21:08', '', NULL, '服务监控菜单');
INSERT INTO `sys_template_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2025-07-10 03:21:08', '', NULL, '缓存监控菜单');
INSERT INTO `sys_template_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2025-07-10 03:21:08', '', NULL, '缓存列表菜单');
INSERT INTO `sys_template_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2025-07-10 03:21:08', '', NULL, '表单构建菜单');
INSERT INTO `sys_template_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2025-07-10 03:21:08', '', NULL, '代码生成菜单');
INSERT INTO `sys_template_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2025-07-10 03:21:08', '', NULL, '系统接口菜单');
INSERT INTO `sys_template_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2025-07-10 03:21:08', '', NULL, '操作日志菜单');
INSERT INTO `sys_template_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2025-07-10 03:21:08', '', NULL, '登录日志菜单');
INSERT INTO `sys_template_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-07-10 03:21:08', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-07-10 03:21:09', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');
INSERT INTO `sys_template_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-07-10 03:21:10', '', NULL, '');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '用户名',
  `user_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '密码',
  `user_avator_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '头像',
  `user_sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '性别',
  `user_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '手机',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '修改人id',
  `update_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT '0' COMMENT '逻辑删除0：有效，1删除',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$xB8E7korwpq54qUErSS1Re.yhFDjHDxc67P2e6nradgGBnLnC3zvG', NULL, '1', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (2, 'test', '123456', NULL, '0', '13445341234', NULL, NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (3, 'terw', '$2a$10$.GfeeC3xmREyZr8QjM9naOl33kFtg/8daUvNThWsusJau04957CLC', NULL, '0', '13456780987', NULL, NULL, NULL, NULL, '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (3, 1, 1);
INSERT INTO `sys_user_role` VALUES (4, 1, 2);

SET FOREIGN_KEY_CHECKS = 1;
