/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.99.103
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : 192.168.99.103:3306
 Source Schema         : demo_cloud_store

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 29/05/2025 23:39:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_store
-- ----------------------------
DROP TABLE IF EXISTS `demo_store`;
CREATE TABLE `demo_store`  (
  `user_id` int NOT NULL,
  `storage` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_store
-- ----------------------------
INSERT INTO `demo_store` VALUES (1, 10276);

SET FOREIGN_KEY_CHECKS = 1;
