package com.surine.family.MQTT;

import android.util.Log;

import com.surine.family.Data.UrlData;
import com.surine.family.EventBus.SimpleEvent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import de.greenrobot.event.EventBus;

public class PushCallBack implements MqttCallback {

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
        Log.d("MQTT_MESSAGE", "链接断开");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
        Log.d("MQTT_MESSAGE", "deliveryComplete----------"+arg0.isComplete());
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		Log.d("MQTT_MESSAGE", "接受消息内容"+new String(arg1.getPayload()));
		Log.d("MQTT_MESSAGE", "接收消息主题"+arg0);
		//System.out.println("接受消息Qos"+arg1.getQos());
		if(arg0.equals(UrlData.topic)){
			if(Integer.valueOf(new String(arg1.getPayload()))>600){
				 //暂时在这里处理不合法数据
			}else {
				//心率
				EventBus.getDefault().post(
						new SimpleEvent(2, new String(arg1.getPayload())));
			}
			}else if(arg0.equals(UrlData.called_voice)){
            //终端反馈（APP拨打，终端是否接听）
			EventBus.getDefault().post(
					new SimpleEvent(4,new String(arg1.getPayload())));
	                }else if(arg0.equals(UrlData.voice)){
            ////终端拨打
			EventBus.getDefault().post(
					new SimpleEvent(5,new String(arg1.getPayload())));
		}
	}

	
}
