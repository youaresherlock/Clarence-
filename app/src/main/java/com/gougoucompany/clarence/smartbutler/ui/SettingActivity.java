package com.gougoucompany.clarence.smartbutler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.service.SmsService;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.gougoucompany.clarence.smartbutler.utils.ShareUtils;
import com.gougoucompany.clarence.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   SettingActivity
 * 创建时间: 2018/4/27 21:07
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     设置活动
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    //语音播报控制按钮
    private Switch sw_speak;
    //短信提醒控制按钮
    private Switch sw_sms;

    //版本更新
    private LinearLayout ll_update;
    private TextView tv_version;

    private String versionName;
    private int versionCode;


    //下载更新的URL,需要传递给UpdateActivity.java
    private String url;

    //扫一扫
    private LinearLayout ll_scan;
    //扫描的结果
    private TextView tv_scan_result;
    //生成二维码
    private LinearLayout ll_qr_code;
    //我的位置
    private LinearLayout ll_my_location;
    //关于软件
    private LinearLayout ll_about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    //初始化view
    private void initView(){

        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);

        //默认第一次是非选中状态 第二次进入恢复上一次的状态
        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);

        boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        sw_sms.setChecked(isSms);

        ll_update = (LinearLayout) findViewById(R.id.ll_update);

        ll_update.setOnClickListener(this);

        tv_version = (TextView) findViewById(R.id.tv_version);

        try {
            getVersionNameCode();
            tv_version.setText("检测版本 " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
        }

        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);

        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);

        ll_qr_code = (LinearLayout) findViewById(R.id.ll_qr_code);
        ll_qr_code .setOnClickListener(this);

        ll_my_location = (LinearLayout) findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);

        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sw_speak:
                //将当前按钮的状态切换成相反的状态(根据按钮是否选中的状态来设置)
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存按钮的状态 isSelected()是选中，isChecked()是状态
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                //切换相反
                sw_sms.setSelected(!sw_speak.isSelected());
                //保存按钮的状态
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked());
                L.i("我被选中了");
                if(sw_sms.isChecked()){
                    //启动一个服务
                    startService(new Intent(this, SmsService.class));
                    L.i("启动服务");
                } else{
                    //关闭一个服务
                    stopService(new Intent(this, SmsService.class));
                    L.i("关闭服务");
                }
                break;
            case R.id.ll_update:
                /**
                 * 步骤:
                 * 1.请求服务器的配置文件，拿到code
                 * 3.比较
                 * 4.dialog提示
                 * 5.跳转到更新界面，并且把url传递过去
                 */
                L.i("版本更新被一点击了!");
                //得到服务器上的config.json文件
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("json: " + t);
                        parsingJson(t);
                    }
                });
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this, QrCodeActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    //解析Json
    private void parsingJson(String t){
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            //服务器versionCode大于APP的versionCode，则更新
            if(code > versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            }  else {
                Toast.makeText(this, "当前是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        //设置对话框的标题和内容
        new AlertDialog.Builder(this)
                .setTitle("有新版本啦!")
                //content为json文件中关于修复内容的描述
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转到更新界面
                        //startActivity(new Intent(SettingActivity.this, UpdateActivity.class));
                        //点击更新按钮之后则跳转到另一个活动，同时将值传递过去
                        Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消什么都不做，也会执行dismiss方法
            }
        }).show();

    }

    //获取版本号/Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //在CaptureActivity活动中使用bundle.putInt()传值 使用this.setResult(RESULT_OK, resultIntent);传递
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }
    }
}




























































