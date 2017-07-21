package com.surine.family.Utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;

import com.surine.family.R;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by surine on 2017/6/26.
 */

public class AgoraManager {
    private static final String TAG = "AgoraManager";

    public static AgoraManager sAgoraManager;

    private RtcEngine mRtcEngine;

    private OnPartyListener mOnPartyListener;

    private int mLocalUid = 0;

    private AgoraManager() {
        mSurfaceViews = new SparseArray<SurfaceView>();
    }

    private SparseArray<SurfaceView> mSurfaceViews;


    //AgoraManager单例
    public static AgoraManager getInstance() {
        if (sAgoraManager == null) {
            synchronized (AgoraManager.class) {
                if (sAgoraManager == null) {
                    sAgoraManager = new AgoraManager();
                }
            }
        }
        return sAgoraManager;
    }


    //远程回调
    private IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        /**
         * 当获取用户uid的远程视频的回调
         */
        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onGetRemoteVideo(uid);
                Log.d(TAG, "onFirstRemoteVideoDecoded:当获取用户uid的远程视频的回调");
            }
        }

        /**
         * 加入频道成功的回调
         */
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onJoinChannelSuccess(channel, uid);
                Log.d(TAG, "onFirstRemoteVideoDecoded: 加入频道成功的回调");
            }
        }

        /**
         * 退出频道
         */
        @Override
        public void onLeaveChannel(RtcStats stats) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onLeaveChannelSuccess();
                Log.d(TAG, "onFirstRemoteVideoDecoded: 退出频道成功的回调");
            }
        }

        /**
         * 用户uid离线时的回调
         */
        @Override
        public void onUserOffline(int uid, int reason) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onUserOffline(uid);
                Log.d(TAG, "onFirstRemoteVideoDecoded: 用户uid离线时的回调");
            }
        }
    };


    /**
     * 初始化RtcEngine
     */
    public void init(Context context) {
        //创建RtcEngine对象，mRtcEventHandler为RtcEngine的回调（传入上下文）
        mRtcEngine = RtcEngine.create(context, context.getString(R.string.private_app_id), mRtcEventHandler);
        //开启视频功能
        mRtcEngine.enableVideo();
        //启用与web的互通
        mRtcEngine.enableWebSdkInteroperability(true);

        mRtcEngine.setClientRole(1, null);
        //视频配置，设置为360P
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
        // mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);//设置为通信模式（默认）
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);//设置为直播模式
//        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_GAME);设置为游戏模式

    }

    /**
     * 设置本地视频，即前置摄像头预览
     */
    public AgoraManager setupLocalVideo(Context context) {
        //创建一个SurfaceView用作视频预览
        SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
        //将SurfaceView保存起来在SparseArray中，后续会将其加入界面。key为视频的用户id，这里是本地视频, 默认id是0
        mSurfaceViews.put(mLocalUid, surfaceView);
        //设置本地视频，渲染模式选择VideoCanvas.RENDER_MODE_HIDDEN，如果选其他模式会出现视频不会填充满整个SurfaceView的情况，
        //具体渲染模式的区别是什么，官方也没有详细的说明
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, mLocalUid));
        return this;//返回AgoraManager以作链式调用
    }


    //设置远程视频
    public AgoraManager setupRemoteVideo(Context context, int uid) {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
        mSurfaceViews.put(uid, surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        return this;
    }


    //加入频道
    public AgoraManager joinChannel(String channel) {
        mRtcEngine.joinChannel(null, channel, null, 0);
        return this;
    }

    //开始预览
    public void startPreview() {
        mRtcEngine.startPreview();
    }

    //停止预览
    public void stopPreview() {
        mRtcEngine.stopPreview();
    }

    //退出频道
    public void leaveChannel() {
        //rtcengine退出频道
        mRtcEngine.leaveChannel();
    }

    //移除surfaceview
    public void removeSurfaceView(int uid) {
        mSurfaceViews.remove(uid);
    }

    public interface OnPartyListener {
        void onJoinChannelSuccess(String channel, int uid);

        void onGetRemoteVideo(int uid);

        void onLeaveChannelSuccess();

        void onUserOffline(int uid);
    }

    public AgoraManager setOnPartyListener(OnPartyListener listener) {
        mOnPartyListener = listener;
        return this;
    }

    public List<SurfaceView> getSurfaceViews() {
        List<SurfaceView> list = new ArrayList<SurfaceView>();
        for (int i = 0; i < mSurfaceViews.size(); i++) {
            SurfaceView surfaceView = mSurfaceViews.valueAt(i);
            list.add(surfaceView);
        }
        return list;
    }

    public SurfaceView getLocalSurfaceView() {
        return mSurfaceViews.get(mLocalUid);
    }

    public SurfaceView getSurfaceView(int uid) {
        return mSurfaceViews.get(uid);
    }
}