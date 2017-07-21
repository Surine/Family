package com.surine.family.UI;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;
import com.surine.family.Data.UrlData;
import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.JavaBean.Call;
import com.surine.family.R;
import com.surine.family.Utils.AgoraManager;
import com.surine.family.View.PartyRoomLayout;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class CallActivity extends AppCompatActivity {
    private PartyRoomLayout mPartyRoomLayout;
    private TextView mChannel;
    private ImageButton mEndCall;
    private TextView tex;
    String channel = "1000";
    private long recLen = 0;
    private String time_string;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_call);
        //注册eventbus
        EventBus.getDefault().register(this);
        mPartyRoomLayout = (PartyRoomLayout) findViewById(R.id.party_room_layout);
        mChannel = (TextView) findViewById(R.id.channel);
        mChannel.setText("正在呼叫……");

        DateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");
        time_string = df.format(new Date());
        mEndCall = (ImageButton) findViewById(R.id.end_call);
        mEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AgoraManager里面封装了挂断的API, 退出频道
                AgoraManager.getInstance().leaveChannel();

                Call call = new Call(R.drawable.pika_back,time_string,tex.getText().toString());
                call.save();
                try {
                if(mediaPlayer!=null){
                    mediaPlayer.reset();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }


                EventBus.getDefault().post(new SimpleEvent(3, "UPDATE"));
                Toast.makeText(CallActivity.this, "已经挂断", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //波纹动画
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
         rippleBackground.startRippleAnimation();


        tex = (TextView) findViewById(R.id.time);
        String a = getIntent().getStringExtra("CALL");
        if(a!=null) {
            if (a.equals("GETCALL")){
                AgoraCall(channel);
            }
        }else {
            //发送电话请求
            MainActivity.mClient.sendCmd("1", UrlData.call_voice);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                                AssetFileDescriptor fd = null;
                            try {
                                fd = getBaseContext().getAssets().openFd("gc.mp3");
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mediaPlayer.start();
                                        mediaPlayer.setLooping(true);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }
                }).start();
            }
    }

    private void AgoraCall(String channel) {
        Log.d("GETCALL", "run: 加入");
        //设置前置摄像头预览并开启
        AgoraManager.getInstance()
                .setupLocalVideo(getApplicationContext())
                .setOnPartyListener(new AgoraManager.OnPartyListener() {
                    @Override
                    public void onJoinChannelSuccess(String channel, int uid) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("GETCALL", "run: 加入");
                                handler.postDelayed(mRunnable,1000);
                                mChannel.setText("接入成功");
                            }
                        });
                    }

                    @Override
                    public void onGetRemoteVideo(final int uid) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //注意SurfaceView要创建在主线程
                                AgoraManager.getInstance().setupRemoteVideo(CallActivity.this, uid);
                                mPartyRoomLayout.addView(AgoraManager.getInstance().getSurfaceView(uid));
                            }
                        });
                    }

                    @Override
                    public void onLeaveChannelSuccess() {
                        mChannel.setText("挂断！");
                    }

                    @Override
                    public void onUserOffline(final int uid) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Call call = new Call(R.drawable.pika_back,time_string,tex.getText().toString());
                                call.save();
                                mChannel.setText("用户离线");
                                //从PartyRoomLayout移除远程视频的SurfaceView
                                mPartyRoomLayout.removeView(AgoraManager.getInstance().getSurfaceView(uid));
                                //清除缓存的SurfaceView
                                AgoraManager.getInstance().removeSurfaceView(uid);
                                finish();
                            }
                        });
                    }
                })
                .joinChannel(channel)
                .startPreview();
        //将摄像头预览的SurfaceView加入PartyRoomLayout
        mPartyRoomLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());
    }

    Handler handler = new Handler();
           Runnable mRunnable = new Runnable() {
               @Override
               public void run() {
                 recLen++;
                 tex.setText(change(recLen));
                 handler.postDelayed(this, 1000);
               }};

    private String change(long time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
         if (time <= 0)
         return "00:00";
        else {
         minute = (int) (time / 60);
           if (minute < 60) {
            second = (int) (time % 60);
            timeStr =minute + ":" + second;
         } else {
             hour = minute / 60;
             if (hour > 99)
                return "99:59:59";
                minute = minute % 60;
                second = (int) (time - hour * 3600 - minute * 60);
                timeStr = hour + ":" + minute + ":" + second;
            }
         }
         return timeStr;
     }
    /**
     * 返回时退出频道
     */
    @Override
    public void onBackPressed() {
        AgoraManager.getInstance().leaveChannel();
    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==4){
            Log.d("GETCALL", "GetMessage: 接受");
           if(event.getMessage().equals("1")){
               Log.d("GETCALL", "GetMessage:2 接受");
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       AgoraCall(channel);
                   }
               });
           }else{
               Toast.makeText(CallActivity.this,"对方暂时无法接听",Toast.LENGTH_SHORT).show();
               finish();
           }
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}