/*
 业务库 ai_demo：用户表 + 订单表（与 application.yml 中 datasource.url 对应）
 可在 MySQL 中执行本脚本初始化库表与示例数据。
*/
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint(0) NOT NULL COMMENT '关联用户ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '订单金额（元）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态：未支付/已支付/已取消/已完成',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_create_time`(`create_time`) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_status`(`status`) USING BTREE COMMENT '订单状态索引',
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 10008 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (10001, 1001, 999.00, '已支付', 'Java编程思想（第4版）', '2026-03-05 10:20:30', '2026-03-05 10:25:30');
INSERT INTO `order` VALUES (10002, 1001, 1999.00, '已支付', 'Spring AI实战教程', '2026-03-10 14:15:20', '2026-03-10 14:20:20');
INSERT INTO `order` VALUES (10003, 1001, 299.00, '未支付', 'MySQL优化指南', '2026-03-15 09:30:10', NULL);
INSERT INTO `order` VALUES (10004, 1002, 599.00, '已完成', '微服务架构设计模式', '2026-02-20 16:40:00', '2026-02-20 16:45:00');
INSERT INTO `order` VALUES (10005, 1002, 899.00, '已取消', '云原生Java开发', '2026-02-25 11:10:50', NULL);
INSERT INTO `order` VALUES (10006, 1003, 1299.00, '已支付', 'Redis深度历险', '2026-03-03 09:20:33', '2026-03-03 09:30:33');
INSERT INTO `order` VALUES (10007, 1003, 799.00, '已支付', 'Kafka实战', '2026-03-05 09:20:33', '2026-03-05 09:25:33');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_name`(`user_name`) USING BTREE COMMENT '用户名索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1001, '张三', '13800138000', '2026-03-06 09:20:33', '2026-03-06 09:20:33');
INSERT INTO `user` VALUES (1002, '李四', '13900139000', '2026-03-06 09:20:33', '2026-03-06 09:20:33');
INSERT INTO `user` VALUES (1003, '王五', '13700137000', '2026-03-06 09:20:33', '2026-03-06 09:20:33');

SET FOREIGN_KEY_CHECKS = 1;
