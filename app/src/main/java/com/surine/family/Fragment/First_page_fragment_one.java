package com.surine.family.Fragment;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.surine.family.Data.Config.HEART_NUMBER_SHOW;
import static com.surine.family.Data.Config.Y_MAX;
import static com.surine.family.Data.Config.Y_MIN;

/**
 * Created by surine on 2017/5/8.
 */

public class First_page_fragment_one extends Fragment {
    LineChart chart;
    LineChart chart2;
    ImageView add_heart;
    ImageView add_temperature;
    TextView heart_number;
    int tag = 1;
    private static final String ARG_ ="First_page_fragment_one" ;
    View v;
    public static First_page_fragment_one getInstance(String title) {
        First_page_fragment_one fra = new First_page_fragment_one();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }


    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onMessageEvent(SimpleEvent event) {
        if(event.getId()==2){
            addEntry(event.getMessage());
            heart_number.setText(event.getMessage()+"");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_first_page_one, container, false);

        add_heart = (ImageView) v.findViewById(R.id.add);
        add_temperature = (ImageView) v.findViewById(R.id.add2);
        heart_number = (TextView) v.findViewById(R.id.heart_number);
        //初始化数据
        initData();

        add_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry(String.valueOf(0));
            }
        });
        add_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addEntry();
            }
        });

        try {
            new Thread(){
                public void run() {
                    while (true){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                playHeartbeatAnimation();
                            }
                        });
                    }
                };
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return v;

    }

        /** * x轴数据处理 * * @param valueType 数据类型 * @return x轴数据 */
    private static String[] xValuesProcess() {
        String[] time = {"10:10", "10:20", "10:30", "10:40", "10:50", "11:00", "11:10"};
        return time;
    }


    //添加数据
    private void addEntry(String message) {
        LineData data = chart.getData();

        ILineDataSet set = data.getDataSetByIndex(0);
        //如果折线不存在，创建
        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }


//                //设置x轴数据
//        chart.getXAxis().setTextColor(Color.BLACK);
//       // final Date date = new Date();
//        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
//
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                Log.d("FORM", "getFormattedValue: "+value);
//               //return new DecimalFormat("##0").format(value);
//                return new SimpleDateFormat("HH:mm").format(new Date());
//            }
//        });


        // 选择一个随机的数据集
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
        float yValue = 0;

        try {
            //设置y轴数据
            yValue = (float)Integer.parseInt(message);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //添加entry
        data.addEntry(new Entry(data.getDataSetByIndex(randomDataSetIndex).getEntryCount(), yValue), randomDataSetIndex);
        //数据更新
        data.notifyDataChanged();

        // 折线更新
        chart.notifyDataSetChanged();

        //设置应该是最大可见的区域（x轴上的范围）的大小（不允许进一步缩小）。
        chart.setVisibleXRangeMaximum(HEART_NUMBER_SHOW);

        //移动图表
        chart.moveViewTo(data.getEntryCount() - 1, 10f, AxisDependency.LEFT);
    }




    private void initData() {
        //初始化图表
        initChart();
    }

    private void initChart() {
        //findview
        chart = (LineChart) v.findViewById(R.id.chart_herat);
        chart2 = (LineChart) v.findViewById(R.id.chart_temperature);


        initChart(chart,tag = 1);   //配置心跳图表样式
        initChart(chart2,tag =2);    //配置温度图表样式

        //设置空数据
        chart.setData(new LineData());
       // chart2.setData(new LineData());

        //update
        chart.invalidate();
        chart2.invalidate();


       // notifyDataSetChanged(chart, getHeartData(),tag =1);  //更新心跳数据
       // notifyDataSetChanged(chart2,getTemperatureData(),tag =2);  //更新温度数据
    }


    /** * 初始化图表 * * @param chart 原始图表 * @return 初始化后的图表 */

    public static LineChart initChart(LineChart chart, int i) {
        // 不显示数据描述
        chart.getDescription().setEnabled(true);
        Description des = new Description();

        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("设备离线");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 图表缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);

        //获取x的实例
        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setGridColor(Color.parseColor("#40000000"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(2);

        YAxis yAxis = chart.getAxisLeft();

        // 不显示y轴
        yAxis.setDrawAxisLine(true);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(true);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(0);
        if(i == 1) {
//            //限制线
//            LimitLine ll = new LimitLine(100f, "心率偏高");
//            ll.setLineColor(Color.RED);
//            ll.setLineWidth(0f);
//            ll.setTextColor(Color.BLACK);
//            ll.setTextSize(8f);
//            LimitLine ll2 = new LimitLine(60f, "心率偏低");
//            ll2.setLineColor(Color.RED);
//            ll2.setLineWidth(0f);
//            ll2.setTextColor(Color.BLACK);
//            ll2.setTextSize(8f);

//            yAxis.addLimitLine(ll);
//            yAxis.addLimitLine(ll2);
            //设置表格最小最大值
            yAxis.setAxisMinimum(Y_MIN);
            yAxis.setAxisMaximum(Y_MAX);
            des.setText("心电图");
            //显示图表备注
            chart.setDescription(des);
        }else{
            yAxis.setAxisMinimum(33);
            yAxis.setAxisMaximum(43);
            //限制线
            LimitLine ll = new LimitLine(41f, "体温偏高");
            ll.setLineColor(Color.RED);
            ll.setLineWidth(0f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(8f);
            LimitLine ll2 = new LimitLine(35f, "体温偏低");
            ll2.setLineColor(Color.RED);
            ll2.setLineWidth(0f);
            ll2.setTextColor(Color.BLACK);
            ll2.setTextSize(8f);

            yAxis.addLimitLine(ll);
            yAxis.addLimitLine(ll2);
            des.setText("体温");
            //显示图表备注
            chart.setDescription(des);
        }

        Matrix matrix = new Matrix();
         //x轴缩放1.5倍
        matrix.postScale(1.5f, 1f);
         //在图表动画显示之前进行缩放
        chart.getViewPortHandler().refresh(matrix, chart, false);
         //x轴执行动画
        chart.animateX(2000);
      //  chart.invalidate();
        return chart;
    }



    //如果没有创建这个表，就创建
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setLineWidth(1f);
        set.setLabel("心电图");
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(AxisDependency.LEFT);
        set.setValueTextSize(8f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void playHeartbeatAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));

        animationSet.setDuration(200);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.4f, 1.0f, 1.4f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));

                animationSet.setDuration(600);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);

                // 实现心跳的View
                add_heart.startAnimation(animationSet);
                add_temperature.startAnimation(animationSet);
            }
        });

        // 实现心跳的View
        add_heart.startAnimation(animationSet);
        add_temperature.startAnimation(animationSet);
    }

}











//    private void addDataSet() {
//
//        LineData data = chart.getData();
//
//        if (data != null) {
//
//            int count = (data.getDataSetCount() + 1);
//
//            ArrayList<Entry> yVals = new ArrayList<Entry>();
//
//            for (int i = 0; i < data.getEntryCount(); i++) {
//                yVals.add(new Entry(i, (float) (Math.random() * 70f) + 70f * count));
//            }
//
//            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
//            set.setLineWidth(2.5f);
//            set.setCircleRadius(4.5f);
//
//            data.addDataSet(set);
//            data.notifyDataChanged();
//            chart.notifyDataSetChanged();
//            chart.invalidate();
//        }
//    }




//    /** * 设置图表数据 * * @param chart 图表 * @param values 数据 */
//    public static void setChartData(LineChart chart, List<Entry> values, int tag) {
//        LineDataSet lineDataSet;
//
//        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
//            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            lineDataSet.setValues(values);
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//        } else {
//            lineDataSet = new LineDataSet(values, "");
//
//            if(tag == 1){
//                // 设置曲线颜色
//                lineDataSet.setColor(Color.parseColor("#d33a31"));
//            }else{
//                lineDataSet.setColor(Color.parseColor("#1e8ae8"));
//            }
//
//            // 设置平滑曲线
//            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            // 显示坐标点的小圆点
//            lineDataSet.setDrawCircles(true);
//            // 显示坐标点的数据
//            lineDataSet.setDrawValues(true);
//            // 不显示定位线
//            lineDataSet.setHighlightEnabled(false);
//
//            LineData data = new LineData(lineDataSet);
//            chart.setData(data);
//            chart.invalidate();
//        }
//    }
//
//
//    /** * 更新图表 * * @param chart 图表 * @param values 数据 * @param valueType 数据类型 */
//    public static void notifyDataSetChanged(LineChart chart, List<Entry> values, int i) {
//        //设置x轴数据
//        chart.getXAxis().setTextColor(Color.BLACK);
//        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xValuesProcess()[(int) value];
//            }
//        });
//
//        chart.invalidate();
//
//        //设置y轴数据
//        setChartData(chart, values,i);
//    }
//
//    /** * x轴数据处理 * * @param valueType 数据类型 * @return x轴数据 */
//    private static String[] xValuesProcess() {
//        String[] time = {"10:10", "10:20", "10:30", "10:40", "10:50", "11:00", "11:10"};
//        return time;
//    }
