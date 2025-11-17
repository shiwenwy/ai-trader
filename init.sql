CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账户名称',
  `available_balance` decimal(65,18) DEFAULT NULL COMMENT '可用余额',
  `balance` decimal(65,18) DEFAULT NULL COMMENT '总余额',
  `card_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '卡号ID',
  `currency` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货币类型',
  `frozen_balance` decimal(65,18) DEFAULT NULL COMMENT '冻结余额',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `version` bigint NOT NULL COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_card` (`user_id`,`card_id`,`currency`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账户表';

CREATE TABLE `account_bill` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `card_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '卡号ID',
  `currency` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货币类型',
  `order_id` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `biz_type` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
  `order_time` timestamp NOT NULL,
  `amount` decimal(64,18) NOT NULL COMMENT '金额',
  `status` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `context` text COLLATE utf8mb4_general_ci,
  `version` int NOT NULL COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`,`biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账单表';

CREATE TABLE `async_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `biz_id` varchar(128) NOT NULL COMMENT '业务ID',
  `task_type` varchar(32) NOT NULL COMMENT '任务类型',
  `task_status` varchar(32) NOT NULL COMMENT '任务状态(INIT/ERROR等)',
  `shard_id` varchar(4) NOT NULL COMMENT '分片ID',
  `next_execute_time` timestamp NOT NULL COMMENT '下次执行时间',
  `execute_count` int NOT NULL DEFAULT '0' COMMENT '执行次数',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  `ext_info` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_task_shard` (`biz_id`,`task_type`,`shard_id`) USING BTREE,
  KEY `idx_shard_status` (`shard_id`,`task_status`) COMMENT '分片状态查询索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='异步任务表';

CREATE TABLE `user_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `pay_password` varchar(255) DEFAULT NULL COMMENT '支付密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '盐',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `remark` varchar(32) DEFAULT NULL COMMENT '备注',
  `context` json DEFAULT NULL COMMENT '上下文信息',
  `version` int DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

CREATE TABLE `exchange_balance_snap` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `exchange` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易所',
  `total_equity` decimal(65,18) DEFAULT NULL COMMENT '账户总资产, 总资产+Pnl',
  `total_wallet_balance` decimal(65,18) DEFAULT NULL COMMENT '账户总余额, 仅计算usdt资产',
  `total_unrealized_profit` decimal(65,18) DEFAULT NULL COMMENT '持仓未实现盈亏总额, 仅计算usdt资产',
  `available_balance` decimal(65,18) DEFAULT NULL COMMENT '可用余额, 仅计算usdt资产',
  `timestamp` bigint NOT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='交易所余额快照表';

CREATE TABLE ai_trader.`decision_result` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `exchange` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易所',
  `thinking` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '思维链分析',
  `signal_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '信号列表',
  `timestamp` bigint NOT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='信号决策结果表';


CREATE TABLE ai_trader.open_position_order (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '账户ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    client_order_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(32),
    user_id VARCHAR(32) NOT NULL,
    exchange VARCHAR(32) NOT NULL,
    symbol VARCHAR(32) NOT NULL,
    side VARCHAR(8) NOT NULL,
    type VARCHAR(16) NOT NULL,
    time_in_force VARCHAR(16),
    quantity DECIMAL(64, 18) NOT NULL,
    entry_price DECIMAL(64, 18),
    stop_price DECIMAL(64, 18),
    profit_target DECIMAL(64, 18),
    order_time TIMESTAMP,
    status VARCHAR(16) NOT NULL,
    leverage INTEGER,
    stop_loss_client_order_id VARCHAR(64),
    profit_client_order_id VARCHAR(64),
    version INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;