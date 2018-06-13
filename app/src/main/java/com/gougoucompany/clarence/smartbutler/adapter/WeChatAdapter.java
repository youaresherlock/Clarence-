package com.gougoucompany.clarence.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.entity.WeChatData;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.gougoucompany.clarence.smartbutler.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.adapter
 * 文件名:   WeChatAdapter
 * 创建时间: 2018/5/10 23:33
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     微信精选适配器
 */

public class WeChatAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<WeChatData>  mList;
    private WeChatData data;
//    private int width, height;
//    private WindowManager wm;

    public WeChatAdapter(Context mContext, List<WeChatData> mList){
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //获得系统服务
//        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        width = wm.getDefaultDisplay().getWidth();
//        height = wm.getDefaultDisplay().getHeight();
//        L.i("width:" + width + "height: " + height);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
       return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.wechat_item, null);
            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);


        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());

        //加载图片
        if(!(data.getImgUrl().equals(""))){
            //按指定大小裁剪图片
            PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(), viewHolder.iv_img, 200, 100);
           // PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(), viewHolder.iv_img, width / 3, 200);
        } else {
            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
        }

        //PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(), viewHolder.iv_img, 200, 100);

        return convertView;
    }

    class ViewHolder{
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;
    }
}





















































