package com.surine.family.UI;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;
import com.surine.family.Data.UrlData;
import com.surine.family.R;

import java.io.IOException;

/**
 * Created by surine on 2017/6/29.
 */

public class CallInactivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.call_in);

        //保持屏幕常亮
        this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        this.wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");

        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetFileDescriptor fd = null;
                try {
                    fd = getBaseContext().getAssets().openFd("call.mp3");
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        ImageButton imageButton = (ImageButton) findViewById(R.id.start_call);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.stop();
                mediaPlayer.release();
                //反馈电话请求
                MainActivity.mClient.sendCmd("1", UrlData.voiced);
                Intent intent = new Intent(CallInactivity.this,CallActivity.class);
                intent.putExtra("CALL","GETCALL");
                startActivity(intent);
                finish();
            }
        });

        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.stop();
                mediaPlayer.release();
                //反馈电话请求
                MainActivity.mClient.sendCmd("0", UrlData.voiced);
                Toast.makeText(CallInactivity.this,"挂断",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.wakeLock.release();
    }
}
