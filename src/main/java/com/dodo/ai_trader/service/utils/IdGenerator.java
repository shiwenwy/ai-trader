package com.dodo.ai_trader.service.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * Author: shiwen
 * Date: 2025/5/20 19:43
 * Description:
 */
public class IdGenerator {
    private static final Snowflake snowflake = IdUtil.getSnowflake();

    public static long nextId() {
        return snowflake.nextId();
    }

    public static String generateId(String bizType) {
        return bizType + snowflake.nextIdStr();
    }

    // 生成10位随机字符串
    public static String generateRandomString() {
        return IdUtil.fastSimpleUUID().substring(0, 10);
    }

}
