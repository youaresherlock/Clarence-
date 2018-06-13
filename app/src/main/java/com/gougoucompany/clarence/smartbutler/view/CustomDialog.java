package com.gougoucompany.clarence.smartbutler.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.gougoucompany.clarence.smartbutler.R;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.view
 * 文件名:   CustomDialog
 * 创建时间: 2018/5/2 23:29
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     TODO
 */

public class CustomDialog extends Dialog{

    //定义模板
    public CustomDialog(Context context, int layout, int style) {
        //Dialog属于WindowManager
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout, style, Gravity.CENTER);
    }

    //定义属性
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim){
        super(context,style);
        //设置属性
        setContentView(layout);
        //拿到Window才可以更改宽高之类的属性
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        //加入动画
        window.setWindowAnimations(anim);
    }

    //实例
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity){
        //动画从下往上出来或者从上往下出来
        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);

    }
}



























































































