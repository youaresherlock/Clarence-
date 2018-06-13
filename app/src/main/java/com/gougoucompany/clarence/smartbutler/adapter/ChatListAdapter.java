package com.gougoucompany.clarence.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.entity.ChatListData;

import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.adapter
 * 文件名:   ChatListAdapter
 * 创建时间: 2018/5/9 21:51
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     对话Adapater
 */


/**
 * Adapter有一个getItemViewType方法，该方法的参数是一个int(当前item的位置)
 * 类型，该方法返回的类型是一个int(表示返回当前传入的对应位置的item属于什么类型).
 * 总体的流程就是，根据item的位置设置item类型，在初始化ViewHolder的时候，根据当前
 * 的类型初始化不同的布局(也就是不同的ViewHolder),最后再给ViewHolder绑定数据的时候
 * 根据不同的ViewHolder做不同的处理。
 */
public class ChatListAdapter  extends BaseAdapter{

    //左边的type
    public static final int VALUE_LEFT_TEXT = 1; //第一种类型
    //右边的type
    public static final int VALUE_RIGHT_TEXT = 2; //第二种类型

    private Context mContext;
    //布局加载器
    private LayoutInflater inflater;
    private ChatListData data;
    private List<ChatListData> mList;

    public ChatListAdapter(Context mContext, List<ChatListData> mList){
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolderLeftText viewHolderLeftText = null;
        ViewHolderRightText viewHolderRightText = null;
        //获取当前要显示的type 根据这个type来区分数据的加载
        int type = getItemViewType(position);
        if(convertView == null){
            switch(type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = inflater.inflate(R.layout.left_item, null);
                    viewHolderLeftText.tv_left_text = (TextView) convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = inflater.inflate(R.layout.right_item, null);
                    viewHolderRightText.tv_right_text = (TextView) convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRightText);
                    break;
            }
        } else {
            switch(type){
                case VALUE_LEFT_TEXT:
                   viewHolderLeftText =  (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }

        //赋值
        data = mList.get(position);
        switch(type){
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }

        return convertView;
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
       data =  mList.get(position);
        int type =  data.getType();
        return type;
    }

    //返回所有的layout数据

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    //左边的文本
    class ViewHolderLeftText{
        private TextView tv_left_text;
    }

    //右边的文本
    class ViewHolderRightText{
        private TextView tv_right_text;
    }
}
