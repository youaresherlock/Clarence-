package com.gougoucompany.clarence.smartbutler.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.gougoucompany.clarence.smartbutler.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashSet;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.application
 * 文件名:   BaseApplication
 * 创建时间: 2018/4/27 7:42
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     Application
 */

public class BaseApplication extends Application {

    /**
     * 在Application创建的时候调用，一般用于初始化一些东西，如全局的对象，环境的配置等
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        //初始化JPush
        JPushInterface.init(this);

        /**
         * 接口定义
         * 参数 context当前上下文  sequence 用户自定义的操作序列号，同操作结果一起返回
         * tags 设置的tags集合
         * public static void addTags(Context context, int sequence,Set<String> tags);
         */
        //添加tag标签，发送消息之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("beggar_user");
        JPushInterface.setTags(this, 1, set);

        /*初始化即创建语音配置对象，只有初始化后才可以使用MSC的各项服务。建议将初始化放在程序入口处*/
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=" + StaticClass.VOICE_KEY);


        //百度地图SDK 在SDK各功能组件使用之前都需要调用
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }
}







































































