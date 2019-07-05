
DROP TABLE IF EXISTS `biz_user`;
CREATE TABLE `biz_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
  `user_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户唯一编码',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态 0-正常 1-冻结',
  `remark` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_id`(`id`) USING BTREE,
  UNIQUE INDEX `index_user_code`(`user_code`) USING BTREE,
  INDEX `index_username`(`username`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  INDEX `index_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
