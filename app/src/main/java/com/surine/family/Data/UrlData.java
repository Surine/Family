package com.surine.family.Data;

/**
 * Created by surine on 2017/5/14.
 */

public class UrlData {
    //药品post地址
    public static String medicine_post = "http://iot.celitea.cn:9999/";
    //位置get地址
    public static String postion_get = "http://iot.celitea.cn:9999/";
    //MQTT通信地址
    public static String mqtt = "tcp://iot.celitea.cn:1883";
    //订阅心率主题
    public static String topic = "heart";   //subscribe
    //根
    public static String root = "/raspberry/#";   //subscribe
    //终端拨打
    public static String voice = "/raspberry/call";   //subscribe
    //APP反馈
    public static String voiced = "/raspberry/iscall";   //subscribe
    //APP拨打
    public static String call_voice = "/raspberry/phone";   //subscribe
    //终端反馈（APP拨打，终端是否接听）
    public static String called_voice = "/raspberry/isphone";   //subscribe
}
