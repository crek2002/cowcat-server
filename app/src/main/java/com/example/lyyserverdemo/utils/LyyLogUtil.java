package com.example.lyyserverdemo.utils;

import android.util.Log;

import java.net.PortUnreachableException;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 12:39
 * @version 1.0
 */
public class LyyLogUtil {
    private static final int CURR_LEVEL=6;//日志控制
    private static final int VERBOSE=5;
    private static final int DEBUG=4;
    private static final int INFO=3;
    private static final int WARN=2;
    private static final int ERROR=1;
    private static final String TAG = "LyyLogUtil";

    public static void logV(String content){
        if (CURR_LEVEL>=VERBOSE){
           Log.v(TAG,content);
        }
    }
    public static void logD(String content){
        if (CURR_LEVEL>=DEBUG){
            Log.d(TAG,content);
        }
    }
    public static void logI(String content){
        if (CURR_LEVEL>=INFO){
            Log.i(TAG,content);
        }
    }
    public static void logW(String content){
        if (CURR_LEVEL>=WARN){
            Log.w(TAG,content);
        }
    }
    public static void logE(String content){
        if (CURR_LEVEL>=ERROR){
            Log.e(TAG,content);
        }
    }
}
