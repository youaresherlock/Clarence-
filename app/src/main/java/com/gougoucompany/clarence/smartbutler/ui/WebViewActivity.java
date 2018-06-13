package com.gougoucompany.clarence.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.utils.L;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   WebViewActivity
 * 创建时间: 2018/5/11 13:05
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     新闻详情
 */

public class WebViewActivity extends BaseActivity{

    //进度
    private ProgressBar mProgressBar;
    //网页
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    //初始化View
    private void initView(){

        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mWebView = (WebView) findViewById(R.id.mWebView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");

        L.i("url:" + url);

        //设置标题
        getSupportActionBar().setTitle(title);

        //进行加载网页的逻辑

        //支持Js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        //设置是否显示内建缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //在开始加载网页的时候显示进度条，加载结束后隐藏进度条
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);

        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //我接受这个事件
                return true;
            }
        });
    }

    public class WebViewClient extends WebChromeClient{

        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //进度条满进度消息
           if(newProgress == 100){
               mProgressBar.setVisibility(View.GONE);
           }
            super.onProgressChanged(view, newProgress);
        }
    }
}



































































