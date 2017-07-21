package com.surine.family.JavaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/5/8.

 * change the name of the Medicine attribute  06月10日 11:54 by surine
 */

//TODO this is a todo
public class Medicine extends DataSupport {
    private int id;
    private String medicinename; //名称
    private int times;    //次数
    private int amount;   //数量
    private String stime;   //开始时间
    private String etime;    //结束时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Medicine(String medicinename, int times, int amount, String stime, String etime) {
        this.medicinename = medicinename;
        this.times = times;
        this.amount = amount;
        this.stime = stime;
        this.etime = etime;
    }

    public String getMedicine_name() {
        return medicinename;
    }

    public void setMedicine_name(String medicinename) {
        this.medicinename = medicinename;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getS_time() {
        return stime;
    }

    public void setS_time(String s_time) {
        this.stime = stime;
    }

    public String getE_time() {
        return etime;
    }

    public void setE_time(String e_time) {
        this.etime = etime;
    }
}
