package com.gougoucompany.clarence.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.gougoucompany.clarence.smartbutler.utils.StaticClass;
import com.gougoucompany.clarence.smartbutler.view.DispatchLinearLayout;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.service
 * 文件名:   SmsService
 * 创建时间: 2018/5/16 17:24
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     短信监听服务
 */

public class SmsService extends Service implements View.OnClickListener {

    //广播接收器对象
    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;

    //窗口管理器
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //自定义View
    private DispatchLinearLayout mView;

    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_send_sms;

    //声明广播
    private HomeWatchReceiver mHomeWatchReceiver;


    public static final String SYSTEM_DIALOGS_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    //初始化
    private void init(){
        L.i("init service");

        //动态注册
        smsReceiver = new SmsReceiver();
        /*当intent在组件间传递时，组件如果想告知Android系统自己能够响应和处理哪些intent,那么就需要用到IntentFilter对象*/
        IntentFilter intent = new IntentFilter();
        //添加action
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intent);


        //动态注册广播
        mHomeWatchReceiver = new HomeWatchReceiver();
        //每次点击Home按键都会发出一个action为Intent.ACTION_CLOSE_SYSTEM_DIALOGS的广播,它是关闭系统Dialog的广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatchReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");

        //服务销毁注销注册
        unregisterReceiver(smsReceiver);
        unregisterReceiver(mHomeWatchReceiver);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_send_sms:
                sendSms();
                //消失窗口
                if(mView.getParent() != null){
                    wm.removeView(mView);
                }
                break;
        }
    }

    //点击回复按钮之后回复短信
    private void sendSms(){
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //第二个参数是回复的内容
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    /*创建一个广播接受器的方法，只需要新建一个类，让它继承自BroadcastReceiver类，
    * 并重写父类的OnReceiver()方法就行了。*/
    //短信广播
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(StaticClass.SMS_ACTION.equals(action)){
                L.i("来短信了");

                //获取短信内容返回的是一个Object数组
                Object[] objs = (Object [])intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for(Object obj : objs){
                    //把数组元素转化成短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[])obj);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getMessageBody();
                    L.i("短信的内容: " + smsPhone + ":" + smsContent);
                    showWindow();
                }
            }
        }
    }


    //窗口提示
    private void showWindow(){
        //获得系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取当前窗口的布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记 程序永远保持屏幕亮 以及可以触摸窗体外面
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式 设置为透明
        layoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载自定义布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);

        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);
        btn_send_sms = (Button) mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);

        //设置数据
        tv_phone.setText("发件人: " + smsPhone);
        L.i("短信内容: " + smsContent);
        tv_content.setText(smsContent);


        //添加view到窗口
        wm.addView(mView, layoutParams);

        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }

    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener
            = new DispatchLinearLayout.DispatchKeyEventListener(){

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否是按返回键
            if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                L.i("我按了Back键");
                if(mView.getParent() != null){
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };

    //监听Home键的广播 按下Home键会发送一个广播
    class HomeWatchReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_REASON_KEY);
                //如果返回的字符串是homekey，则说明点击了HOME键
                if(SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    L.i("我点击了HOME键");
                    if(mView.getParent() != null){
                        wm.removeView(mView);
                    }
                }
            }
        }
    }
}






















































