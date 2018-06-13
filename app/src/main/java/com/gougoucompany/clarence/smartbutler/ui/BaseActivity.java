package com.gougoucompany.clarence.smartbutler.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   BaseActivity
 * 创建时间: 2018/4/27 7:47
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     Activity基类
 */

/**
 * 主要做的事情:
 * 1.统一的属性
 * 2.统一的接口
 * 3.统一的方法 可以在子类直接调用这个方法
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //菜单栏操作

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //安卓默认返回键的id
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}



























