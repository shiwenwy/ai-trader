package com.dodo.ai_trader.service.utils;

public class AiResParseUtil {

    private static final String AI_RES_PREFIX = "```json";
    private static final String AI_RES_SUFFIX = "```";

    public static String parseAiRes(String aiRes) {
        int startIndex = aiRes.indexOf(AI_RES_PREFIX);
        aiRes = aiRes.substring(startIndex + AI_RES_PREFIX.length());
        return aiRes.substring(0, aiRes.indexOf(AI_RES_SUFFIX));
    }

    public static String parseAiThought(String aiRes) {
        int endIndex = aiRes.indexOf(AI_RES_PREFIX);
        return aiRes.substring(0, endIndex);
    }
}
