package com.gougoucompany.clarence.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.ui
 * 文件名:   QrCodeActivity
 * 创建时间: 2018/6/10 10:57
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     生成二维码
 */

public class QrCodeActivity extends BaseActivity{

    //我的二维码
    private ImageView iv_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initView(){

        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        //屏幕的宽
        int width = getResources().getDisplayMetrics().widthPixels;

        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我的英文名字是Clarence", width / 2, width / 2,
                        BitmapFactory.decodeResource(getResources(), R.drawable.launcher_animal));

        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}






































































