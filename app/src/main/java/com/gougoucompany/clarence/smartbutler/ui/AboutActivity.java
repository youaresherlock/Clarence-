package com.gougoucompany.clarence.smartbutler.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   AboutActivity
 * 创建时间: 2018/6/12 11:34
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     关于软件
 */

public class AboutActivity extends BaseActivity {

    private ListView mListView;
    private TextView tv_end_text;
    private List<String> mList = new ArrayList<>();
    //安卓自带适配器
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //去除阴影
        getSupportActionBar().setElevation(0);

        initView();
    }

    //初始化View
    private void initView(){
        tv_end_text = (TextView) findViewById(R.id.tv_end_text);
        UtilTools.setFont(this, tv_end_text);
        mListView = (ListView) findViewById(R.id.mListView);

        mList.add("作者中文名: 习伟博");
        mList.add("作者英文名: Clarence");
        mList.add("应用名: " + getString(R.string.app_name));
        mList.add("版本号: " + UtilTools.getVersion(this));
        mList.add("官网: www.youaresherlock.cn");

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        //设置适配器
        mListView.setAdapter(mAdapter);
    }

}


























