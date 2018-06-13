package com.gougoucompany.clarence.smartbutler.entity;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.entity
 * 文件名:   CourierData
 * 创建时间: 2018/5/8 18:07
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     快递查询实体
 */

public class CourierData {

    //时间
    private String datetime;
    //状态
    private String remark;
    //城市
    private String zone;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datatime) {
        this.datetime = datatime;
    }

    @Override
    public String toString() {
        return "CourierDtdata{" +
                "datatime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}






























































