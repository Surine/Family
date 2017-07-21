package com.surine.family.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.surine.family.Adapter.Recycleview.Medicine_adapter;
import com.surine.family.Data.UrlData;
import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.JavaBean.Medicine;
import com.surine.family.R;
import com.surine.family.Utils.GsonUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by surine on 2017/5/8.
 */

public class First_page_fragment_two extends Fragment {
    private static final String ARG_ = "First_page_fragment_two";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    private static final int IS_SUCCESS = 1;
    private static final int IS_ERROR = 0;
    private static final int AUTO = 2;
    View v;
    Medicine_adapter adpter;
    private RecyclerView medicine_rec;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Medicine> mmedicinelists = new ArrayList<>();

    public static First_page_fragment_two getInstance(String title) {
        First_page_fragment_two fra = new First_page_fragment_two();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragement_first_page_two, container, false);
        //初始化数据
        initData();
        initView();
        initListener();
        return v;

    }

    private void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //开启一个线程，做联网操作
                new Thread() {
                    @Override
                    public void run() {
                        refresh();  //刷新,上传数据到服务器
                    }
                }.start();
            }
        });
    }

    private void refresh() {
        //1.获取本地数据封装成json
        //2.创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.GsonString(mmedicinelists));
        //创建一个请求对象

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("data",
                Activity.MODE_PRIVATE);
        String url =sharedPreferences.getString("ip", UrlData.medicine_post);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //TODO:DELETE
                Log.d(ARG_, "onCreateView: "+GsonUtil.GsonString(mmedicinelists));
                //打印服务端返回结果
                Log.d(ARG_, response.body().string());
                Message message = new Message();
                message.what = IS_SUCCESS;
                mHandler.sendMessage(message);
            }
        } catch (IOException e) {
            Message message = new Message();
            message.what = IS_ERROR;
            mHandler.sendMessage(message);
            Log.d(ARG_, "REQUEST ERROR");
            e.printStackTrace();
        }
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        medicine_rec = (RecyclerView) v.findViewById(R.id.rec_in_two);
        initRec();
    }

    private void initRec() {
        medicine_rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        adpter = new Medicine_adapter(getActivity(),mmedicinelists);
        medicine_rec.setAdapter(adpter);
    }

    private void initData() {
           mmedicinelists = DataSupport.findAll(Medicine.class);
        }


    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==1){
            mmedicinelists = DataSupport.findAll(Medicine.class);
            Message message = new Message();
            message.what = AUTO;
            mHandler.sendMessage(message);
            initRec();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getActivity(),"数据上传成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getActivity(),"数据上传错误",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    autoSwipe();
                    break;
            }
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    };

    private void autoSwipe() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        //开启一个线程，做联网操作
        new Thread() {
            @Override
            public void run() {
                refresh();  //刷新,上传数据到服务器
            }
        }.start();
    }
}
