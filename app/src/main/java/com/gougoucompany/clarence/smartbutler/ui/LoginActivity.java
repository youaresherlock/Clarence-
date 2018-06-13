package com.gougoucompany.clarence.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.MainActivity;
import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.entity.MyUser;
import com.gougoucompany.clarence.smartbutler.utils.ShareUtils;
import com.gougoucompany.clarence.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   LoginActivity
 * 创建时间: 2018/5/1 9:41
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //注册按钮
    private Button btn_registered;
    private EditText et_name;
    private EditText et_password;
    private Button btnLogin;
    private CheckBox keep_password;
    private TextView tb_forget;

    //登录的进度Dialog
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        keep_password = (CheckBox) findViewById(R.id.keep_password);
        tb_forget = (TextView) findViewById(R.id.tv_forget);
        tb_forget.setOnClickListener(this);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效 也就是说点击不能退出
        dialog.setCancelable(false);


        //设置选中的状态 默认不勾选记住密码
        boolean isCheck =  ShareUtils.getBoolean(this, "keeppass", false);
        keep_password.setChecked(isCheck);
        if(isCheck){
            //设置密码 默认是空字符串
            et_name.setText(ShareUtils.getString(this, "name", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_registered:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            case R.id.btnLogin:
                //1.获取输入框的值
                String name =  et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //判断是否为空
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    dialog.show();

                    //登录
                   final  MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>(){
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            //判断结果
                            if(e == null){
                                //判断邮箱是否验证
                                if(user.getEmailVerified()){
                                    //跳转 注意:匿名内部来中不能传入this
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "请前往邮箱验证", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*假设用户输入用户名和密码，但是没有点击登录，而是直接退出了，如果这时候也想记住密码，就保存密码
    * 否则可以在startActivity()后然后执行密码的保存工作*/
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //保运CheckBox选中的状态
        ShareUtils.putBoolean(this, "keeppass", keep_password.isChecked());

        //是否记住密码
        if(keep_password.isChecked()){
            //记住用户名和密码
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            //没有勾选记住密码就要清楚密码
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");
        }
    }
}




























































































































