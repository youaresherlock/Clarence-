package com.gougoucompany.clarence.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.entity.MyUser;
import com.gougoucompany.clarence.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   RegisteredActivity
 * 创建时间: 2018/5/1 10:19
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     注册
 */

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_user;
    private EditText et_age;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;
    private Button btnRegistered;

    //注册的进度Dialog
    private CustomDialog dialog;

    //性别 默认为男
    private boolean isGender = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
    }

    private void initView(){
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        btnRegistered = (Button) findViewById(R.id.btnRegistered);
        btnRegistered.setOnClickListener(this);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_register_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效 也就是说点击不能退出
        dialog.setCancelable(false);
     }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRegistered:
                dialog.show();
                //获取到输入的值
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                //判断是否为空(两种情况空字符串或者是null)
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age)
                & !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(password)
                        & !TextUtils.isEmpty(email)){

                    //判断两次输入的密码是否一致
                    if(pass.equals(password)){

                        //先把性别判断一下
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if(checkedId == R.id.rb_boy){
                                    isGender = true;
                                } else if(checkedId == R.id.rb_girl){
                                    isGender = false;
                                }
                            }
                        });

                        //判断简介是否为空
                        if(TextUtils.isEmpty(desc)){
                            desc = "这个人很懒，什么都没有留下";
                        }

                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(pass);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(isGender); //true表示男
                        user.setDesc(desc);
                        
                        user.signUp(new SaveListener<MyUser>() {

                            //表示请求成功
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                dialog.dismiss();
                                if(e == null){
                                    Toast.makeText(RegisteredActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    //finish()调用之后会进入登录页面
                                    finish();
                                } else {
                                    Toast.makeText(RegisteredActivity.this, "注册失败: " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

























































































