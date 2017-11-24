package com.surine.family.UI;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.surine.family.Data.UrlData;
import com.surine.family.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap mAmap;
    private CameraUpdate cameraUpdate;
    private FloatingActionButton mFloatingActionButton;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabs);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(14));

        latLng = new LatLng(39.906901,116.397972);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title("妈妈在这里").snippet("天津市");

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.local)));
        mAmap.addMarker(markerOption);
        cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,8,0,30));
        mAmap.animateCamera(cameraUpdate);//地图移向指定区域
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(14));

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(new Request.Builder().url(UrlData.postion_get).build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MapActivity.this,"网络未连接",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAmap.clear();
                                latLng = new LatLng(39.906901,116.397972);
                                MarkerOptions markerOption = new MarkerOptions();
                                markerOption.position(latLng);
                                markerOption.title("妈妈在这里").snippet("天津市：39.906901, 116.397972");

                                markerOption.draggable(true);//设置Marker可拖动
                                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),R.drawable.local)));
                                mAmap.addMarker(markerOption);
                                cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,8,0,30));
                                mAmap.animateCamera(cameraUpdate);//地图移向指定区域
                                mAmap.moveCamera(CameraUpdateFactory.zoomTo(14));
                            }
                        });
                    }
                });

            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}