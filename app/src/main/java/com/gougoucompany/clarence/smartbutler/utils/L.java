package com.gougoucompany.clarence.smartbutler.utils;

import android.util.Log;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.utils
 * 文件名:   L
 * 创建时间: 2018/4/27 23:34
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     Log封装类
 */

public class L {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "SmartButler";

    //五个等级 DIWEF
    public static void d(String text){
        //测试模式输出
        if(DEBUG){
            Log.d(TAG, text);
        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG, text);
        }
    }

    public static void w(String text){
        if(DEBUG){
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }
}
































