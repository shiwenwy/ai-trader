package com.dodo.ai_trader.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: shiwen
 * Date: 2024/9/14 14:44
 * Description:
 */
public class LogUtil {

    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    private static final Logger controllerLogger = LoggerFactory.getLogger("CONTROLLER");

    private static final Logger bizLogger = LoggerFactory.getLogger("BIZ");

    private static final Logger serviceLogger = LoggerFactory.getLogger("SERVICE");

    private static final Logger dalLogger = LoggerFactory.getLogger("DAL");

    private static final Logger monitorLogger = LoggerFactory.getLogger("MONITOR");

    private static final Logger importanceLogger = LoggerFactory.getLogger("IMPORTANCE");

    public static void controllerLog(String msg) {
        controllerLogger.info(msg);
    }

    public static void bizLog(String msg) {
        bizLogger.info(msg);
    }

    public static void serviceLog(String msg) {
        serviceLogger.info(msg);
    }

    public static void dalLog(String msg) {
        dalLogger.info(msg);
    }

    public static void controllerLog(String msg, Object... args) {
        controllerLogger.info(msg, args);
    }

    public static void bizLog(String msg, Object... args) {
        bizLogger.info(msg, args);
    }

    public static void serviceLog(String msg, Object... args) {
        serviceLogger.info(msg, args);
    }

    public static void dalLog(String msg, Object... args) {
        dalLogger.info(msg, args);
    }

    public static void monitorLog(String msg) {
        monitorLogger.info(msg);
    }

    public static void monitorLog(String msg, Object... args) {
        monitorLogger.info(msg, args);
    }

    public static void importanceLog(String msg) {
        importanceLogger.info(msg);
    }

    public static void importanceLog(String msg, Object... args) {
        importanceLogger.info(msg, args);
    }

    public static void error(String msg) {
        errorLogger.error(msg);
    }

    public static void error(String msg, Object... args) {
        errorLogger.error(msg, args);
    }

    public static void error(String msg, Throwable t) {
        errorLogger.error(msg, t);
    }

    public static void error(String msg, Throwable t, Object... args) {
        errorLogger.error(msg, t, args);
    }
}
