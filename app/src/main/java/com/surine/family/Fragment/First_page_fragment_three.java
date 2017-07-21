package com.surine.family.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.family.Adapter.Recycleview.Call_adapter;
import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.JavaBean.Call;
import com.surine.family.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by surine on 2017/5/8.
 */

public class First_page_fragment_three extends Fragment {
    private static final String ARG_ ="First_page_fragment_three" ;
    private RecyclerView call_rec;
    private List<Call> mCallList = new ArrayList<>();
    View v;
    public static First_page_fragment_three getInstance(String title) {
        First_page_fragment_three fra = new First_page_fragment_three();
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
        v= inflater.inflate(R.layout.fragement_first_page_three, container, false);
        //初始化数据
        initView();
        initData();
        setListener();
        return v;

    }

    private void setListener() {

    }

    private void initView() {
        call_rec = (RecyclerView) v.findViewById(R.id.rec_in_three);
        call_rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        call_rec.setAdapter(new Call_adapter(getActivity(),mCallList));
    }

    private void initData() {
        mCallList = DataSupport.findAll(Call.class);
    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==3){
            mCallList = DataSupport.findAll(Call.class);
            initView();
        }
    }
}
