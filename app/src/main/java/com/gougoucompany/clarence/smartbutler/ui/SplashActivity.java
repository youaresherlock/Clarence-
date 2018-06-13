package com.gougoucompany.clarence.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.gougoucompany.clarence.smartbutler.utils.ShareUtils;
import com.gougoucompany.clarence.smartbutler.utils.StaticClass;
import com.gougoucompany.clarence.smartbutler.utils.UtilTools;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   SplashActivity
 * 创建时间: 2018/4/29 23:20
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     闪屏页
 */

public class SplashActivity extends AppCompatActivity{

    /**
     * 1.延时2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     * @param savedInstanceState
     */

    private TextView tv_splash;

    private TextView tv_start_text;

    //Handler可以用作子线程更新UI，也可以延时
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    //初始化View
    private void initView(){

        //延时2000毫秒
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);

        tv_splash = (TextView) findViewById(R.id.tv_splash);
        tv_start_text = (TextView) findViewById(R.id.tv_start_text);

        //设置字体
        UtilTools.setFont(this, tv_splash);
        UtilTools.setFont(this, tv_start_text);
    }

    //判断程序是否第一次运行
    private boolean isFirst(){
        //第一次拿到的值设定为true,则意味着要加载引导页 加载之后将键值队中的值修改成false,下次运行
        //的时候就不加载引导页了
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if(isFirst){
            //标记我们已经启动过app
            L.i("第一次启动");
            ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            //是第一次运行
            return true;
        } else {
            return false;
        }
    }

    //为了不影响用户体验，我们不能让用户按返回键
    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}




































