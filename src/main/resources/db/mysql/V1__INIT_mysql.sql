
CREATE TABLE IF NOT EXISTS `transactions` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `create_time` datetime DEFAULT NULL,
                                              `update_time` datetime DEFAULT NULL,
                                              `source` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
    `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `trans_time` datetime DEFAULT NULL,
    `category` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `trans_to_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `trans_to_account` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `product_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `trans_type` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `amount` double DEFAULT NULL,
    `pay_method` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `trans_status` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `trans_order_no` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `merchant_order_no` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `memo` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;