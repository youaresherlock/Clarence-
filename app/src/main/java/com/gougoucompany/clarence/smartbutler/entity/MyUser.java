package com.gougoucompany.clarence.smartbutler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.entity
 * 文件名:   MyUser
 * 创建时间: 2018/5/1 20:15
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     用户属性
 */

public class MyUser extends BmobUser{

    //用户年龄
    private int age;
    //true为男，false为女 用户性别
    private boolean sex;
    //用户简介
    private String desc;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}






































































