package com.surine.family.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.MQTT.Client;
import com.surine.family.UI.CallInactivity;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import static com.surine.family.UI.MainActivity.mClient;

/**
 * Created by surine on 2017/6/29.
 */

public class MqttService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //启动MQTT客户端
                mClient= new Client();
                mClient.start();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        startService(new Intent(this,MqttService.class));
    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        //接收到打来电话消息
        if(event.getId()==5){
            Intent intent = new Intent(getBaseContext(),CallInactivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        }
    }
}
