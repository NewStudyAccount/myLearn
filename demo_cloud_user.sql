/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.99.103
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : 192.168.99.103:3306
 Source Schema         : demo_cloud_user

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 29/05/2025 23:39:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_order
-- ----------------------------
DROP TABLE IF EXISTS `demo_order`;
CREATE TABLE `demo_order`  (
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL,
  `user_id` int NULL DEFAULT NULL,
  `product_id` int NULL DEFAULT NULL,
  `amount` int NULL DEFAULT NULL,
  `quantity` int NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_order
-- ----------------------------
INSERT INTO `demo_order` VALUES ('28e25132-d0db-4360-b2ba-5b5c7399b1d5', 1, 1, 100, 100, 'PENDING');
INSERT INTO `demo_order` VALUES ('2fa40f0f-89ac-4a6d-9756-bd8909422f08', 1, 1, 100, 100, 'PENDING');
INSERT INTO `demo_order` VALUES ('66f0dec8-4d61-499e-bca3-25cfef896558', 1, 1, 100, 100, 'PENDING');
INSERT INTO `demo_order` VALUES ('7795e10c-a6f4-47fd-8a00-66e6a545289e', 1, 1, 100, 100, 'PENDING');
INSERT INTO `demo_order` VALUES ('b4eb7d9b-4266-419f-9673-773336436275', 1, 1, 100, 100, 'PENDING');

-- ----------------------------
-- Table structure for demo_user
-- ----------------------------
DROP TABLE IF EXISTS `demo_user`;
CREATE TABLE `demo_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_user
-- ----------------------------
INSERT INTO `demo_user` VALUES (1, 'Ti Ming Sze');
INSERT INTO `demo_user` VALUES (2, 'Koon Wing Fat');
INSERT INTO `demo_user` VALUES (3, 'Wong Wing Kuen');
INSERT INTO `demo_user` VALUES (4, 'Jesus Aguilar');
INSERT INTO `demo_user` VALUES (5, 'Liang Jiehong');
INSERT INTO `demo_user` VALUES (6, 'Bruce Bennett');
INSERT INTO `demo_user` VALUES (7, 'Zhou Lan');
INSERT INTO `demo_user` VALUES (8, 'Lok Siu Wai');
INSERT INTO `demo_user` VALUES (9, 'Chang Zhennan');
INSERT INTO `demo_user` VALUES (10, 'Marilyn Medina');

SET FOREIGN_KEY_CHECKS = 1;
