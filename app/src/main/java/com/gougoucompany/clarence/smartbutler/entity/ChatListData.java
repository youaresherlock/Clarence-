package com.gougoucompany.clarence.smartbutler.entity;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.entity
 * 文件名:   ChatListData
 * 创建时间: 2018/5/9 22:25
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     对话列表的实体
 */

public class ChatListData {

    //type
    private int type;

    //文本
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
