/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.99.103
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : 192.168.99.103:3306
 Source Schema         : demo_cloud_account

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 29/05/2025 23:39:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_account
-- ----------------------------
DROP TABLE IF EXISTS `demo_account`;
CREATE TABLE `demo_account`  (
  `user_id` int NOT NULL,
  `money` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_account
-- ----------------------------
INSERT INTO `demo_account` VALUES (1, 8400);
INSERT INTO `demo_account` VALUES (2, 7000);
INSERT INTO `demo_account` VALUES (3, 6000);
INSERT INTO `demo_account` VALUES (4, 5000);
INSERT INTO `demo_account` VALUES (5, 4000);
INSERT INTO `demo_account` VALUES (6, 3000);
INSERT INTO `demo_account` VALUES (7, 2000);
INSERT INTO `demo_account` VALUES (8, 1000);
INSERT INTO `demo_account` VALUES (9, 100);
INSERT INTO `demo_account` VALUES (10, 100);

SET FOREIGN_KEY_CHECKS = 1;
