package com.gougoucompany.clarence.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.gougoucompany.clarence.smartbutler.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.utils
 * 文件名:   PicassoUtils
 * 创建时间: 2018/5/11 22:58
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     Picasso的封装
 */

public class PicassoUtils {

    //默认加载图片
    public static void loadImageView(Context mContext, String url, ImageView imageView){
        Picasso.with(mContext).load(url).into(imageView);
    }

    //默认加载图片(指定大小)
    public static void loadImageViewSize(Context mContext, String url, ImageView imageView,
                                         int width, int height){
        Picasso.with(mContext)
                .load(url)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }

    //加载图片有默认图片
    public static void loadImageViewHolder(Context mContext, String url, int loadImg,
                                           int errorImg, ImageView imageView){
        Picasso.with(mContext)
                .load(url)
                .placeholder(loadImg) //加载默认图片
                .error(errorImg) //加载错误图片
                .into(imageView);
    }

    //裁剪图片
    public static void loadImageViewCrop(Context mContext, String url, ImageView imageView){
        Picasso.with(mContext).load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    //按比例裁剪
    public static class CropSquareTransformation implements Transformation{


        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y ,size, size);
            if(result != source){
                //回收
                source.recycle();
            }

            return result;
        }

        @Override
        public String key() {
            return "lgl";
        }
    }
}







































































