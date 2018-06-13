package com.gougoucompany.clarence.smartbutler.utils;


import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.utils
 * 文件名:   StaticClass
 * 创建时间: 2018/4/27 8:06
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     数据/常量
 */

public class StaticClass {

    //闪屏页的延时
    public static final int HANDLER_SPLASH = 1001;
    //判断程序是否是第一次运行
    public static final String SHARE_IS_FIRST = "isFirst";

    //Bugly key
    public static final String BUGLY_APP_ID = "0d1b56947d";
    //Bmob key
    public static final String BMOB_APP_ID = "a2ef5aa098923fdedc55a4eae126451a";

    //个人用户保存的图像资源键
    public static final String USER_IMAGE = "image_title";

    //快递key
    public static final String COURIER_KEY = "79289420275486aea6a3e67d03569b3c";

    //手机号码归属地key
    public static final String PHONE_KEY = "43914e9f9515c014fe0d2f60e0354019";

    //图灵机器人key
    public static final String CHAT_LIST_KEY = "0993f47f0bcd46d8bf6c04708a7b9533";

    //微信精选key
    public static final String WECHAT_KEY = "1c632318e55c2b02d295215963c5388f";

  //  http://v.juhe.cn/exp/index?key=79289420275486aea6a3e67d03569b3c&com=sf&no=575677355677

    /**
     * Gank的API升级之后不可以在路径中出现汉字，因此我们需要对福利这两个字进行转码
     */
    public static  String  welfare = null;

//
    private  static String getWelfare() {
        try {
            //Gank 升级 需要转码
            welfare = URLEncoder.encode(getApplicationContext().getString(R.string.text_welfare), "UTF-8");
            L.i(welfare);
            return welfare;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    //美女图片接口
   // public static final String GIRL_URL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/50/1";
    public static final String GIRL_URL = "http://gank.io/api/data/" + getWelfare() + "/50/1";

    //科大讯飞语音AppId
    public static final String VOICE_KEY = "5afa9107";

    //短信Action
    public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    //版本更新
    public static final String CHECK_UPDATE_URL = "http://10.101.41.181:8080/APPUpdate/config.json";

}





















































