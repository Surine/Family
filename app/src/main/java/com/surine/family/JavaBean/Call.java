package com.surine.family.JavaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/5/8.
 */

public class Call extends DataSupport{
    private int id;
    private int imageId;
    private String call_date;
    private String call_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Call(int imageId, String call_date, String call_time) {
        this.imageId = imageId;
        this.call_date = call_date;
        this.call_time = call_time;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCall_date() {
        return call_date;
    }

    public void setCall_date(String call_date) {
        this.call_date = call_date;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }
}
