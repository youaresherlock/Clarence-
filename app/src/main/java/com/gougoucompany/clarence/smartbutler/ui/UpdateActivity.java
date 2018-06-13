package com.gougoucompany.clarence.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   UpdateActivity
 * 创建时间: 2018/6/8 14:28
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     下载更新
 */

public class UpdateActivity extends BaseActivity{

    //正在下载
    public static final int HANDLER_LODING = 10001;
    //下载完成
    public static final int HANDLER_OK = 10002;
    //下载失败
    public static final int HANDLER_FAILURE = 10003;

    private TextView tv_size;
    private String url;
    private String path;

    //下载进度条
    private NumberProgressBar number_progress_bar;


    //异步消息处理机制
    private Handler handler = new Handler(){
        /*void handleMessage(Message msg)
        *Subclasses must implement this to receive messages. 使用这个方法来发送消息*/
        @Override
        public void handleMessage(Message msg) {
            //实时更新进度
            super.handleMessage(msg);
            switch(msg.what){
                case HANDLER_LODING:
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes + " / " + totalSize);
                    //设置进度 这里参数是int类型，因此需要进行强制转换
                    number_progress_bar.setProgress((int) ((float)transferredBytes / (float)totalSize * 100));
                    break;
                case HANDLER_OK:
                    tv_size.setText("下载成功");
                    //启动这个应用安装
                    startInstallApk();
                    break;
                case HANDLER_FAILURE:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    };

    //启动安装
    private void startInstallApk(){
        Intent intent = new Intent();
        //android:intent.action.VIEW 显示指定数据
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        /*Uri fromFile(File file) Creates a Uri from a file. The URI has the form "file://". Encodes path characters with the exception of '/'.*/
        //设置数据和类型
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView(){
        tv_size = (TextView) findViewById(R.id.tv_size);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";

        number_progress_bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        number_progress_bar.setMax(100);

        //从config.json文件中获得下载的url
        url = getIntent().getStringExtra("url");
        L.i("返回正确的url: " + url + "\n正确的路径: " + path);
        if(!TextUtils.isEmpty(url)){
            //下载 参数分别是下载路径 url  进度回调函数 以及下载成功和失败的回调函数
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    //在子线程中实时将两个long基本数据传递到主线程，以便于更新UI
                    Message msg = new Message();
                    msg.what = HANDLER_LODING;
                    Bundle bundle = new Bundle();
                    //传输字节数
                    bundle.putLong("transferredBytes", transferredBytes);
                    //总字节数
                    bundle.putLong("totalSize", totalSize);
                    //void setData(Bundle data) Sets a Bundle of arbitrary data values.
                    msg.setData(bundle);
                    /*boolean sendMessage(Message msg) Pushes a message to the end of the message queue after all pending messages
                    * before the current time. It will be received in handleMessage(Message), in the thread attached to this handler.*/
                    //handler不停地发送消息，然后在主线程中handler不停地处理消息，实时更新下载进度
                    handler.sendMessage(msg);
                    L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    //安卓只能在主线程中更新UI
                    handler.sendEmptyMessage(HANDLER_OK);
                    L.i("下载成功");
                }

                @Override
                public void onFailure(VolleyError error) {
                    handler.sendEmptyMessage(HANDLER_FAILURE);
                    L.e("下载失败");
                }
            });
        }
    }
}



















































