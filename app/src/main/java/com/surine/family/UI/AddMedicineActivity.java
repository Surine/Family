package com.surine.family.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.JavaBean.Medicine;
import com.surine.family.R;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class AddMedicineActivity extends AppCompatActivity {
    private EditText add_medicine_name;
    private EditText add_medicine_time;
    private EditText add_medicine_amount;
    private TextView add_medicine_s;
    private TextView add_medicine_e;
    private ImageButton add_s_button;
    private ImageButton add_e_button;
    private String s_date;
    private String e_date;
    private int mYear, mMonth, mDay;
    private int set_Year, set_Month, set_Day;
    boolean from;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        initView();
        Intent intent = getIntent();
        from = intent.getBooleanExtra("CARD",false);
        id = intent.getIntExtra("ID",0);
        if(from){
            loadData(id);  //如果从card点进来就要加载数据
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!from){
                    datecheck();  //数据检查
                }else{
                    Snackbar.make(add_e_button,"不允许改动输入",Snackbar.LENGTH_SHORT).setAction("关闭页面", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }

            }
        });

        setListener();
    }

    private void loadData(int id) {
        Medicine medicine = DataSupport.find(Medicine.class,id);
        add_medicine_name.setText(medicine.getMedicine_name());
        add_medicine_amount.setText(medicine.getAmount()+"");
        add_medicine_time.setText(medicine.getTimes()+"");
        add_medicine_s.setText(medicine.getS_time());
        add_medicine_e.setText(medicine.getE_time());
    }

    private void datecheck() {
        String menmae = add_medicine_name.getText().toString();
        String meamout = add_medicine_amount.getText().toString();
        String metime = add_medicine_time.getText().toString();
        String mes = add_medicine_s.getText().toString();
        String mee = add_medicine_e.getText().toString();
        if(menmae.equals("")||
                meamout.equals("")||
                metime.equals("")||
                mes.equals(getString(R.string.start))||
                mee.equals(getString(R.string.end))){
            Snackbar.make(add_e_button,"存在空输入",Snackbar.LENGTH_SHORT).show();
        }else {
            Medicine medicine = new Medicine(menmae,
                    Integer.parseInt(metime),
                    Integer.parseInt(meamout),
                    mes,
                    mee);
            medicine.save();
            //TODO:发出列表更新通知

            EventBus.getDefault().post(new SimpleEvent(1, "UPDATE"));
            Toast.makeText(AddMedicineActivity.this, "OK", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("s_time","null");
        editor.apply();
    }

    private void setListener() {

        add_s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePickerDialog(1);
            }
        });
        add_e_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePickerDialog(2);
            }
        });
    }

    //设置时间对话框
    private void setDatePickerDialog(final int i) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(AddMedicineActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                         set_Year = year;
                         set_Month = monthOfYear+1;
                         set_Day = dayOfMonth;
                        checkDate(i);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    private void checkDate(int i) {
        //根据标记值来判定显示及时间
        if(i == 1){
            add_medicine_s.setText(set_Year+"-"+set_Month+"-"+set_Day);
            if(compareDate(add_medicine_s.getText().toString(),mYear+"-"+(mMonth+1)+"-"+mDay) == 1){
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("s_time",add_medicine_s.getText().toString());
                editor.apply();
            }else{
                Snackbar.make(add_e_button,"时间不合法,请重新选择",Snackbar.LENGTH_SHORT).show();
                add_medicine_s.setText("");
            }
        }else {
            add_medicine_e.setText(set_Year+"-"+set_Month+"-"+set_Day);
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            if (pref.getString("s_time", "null").equals("null")) {
                Snackbar.make(add_e_button, "请先选择起始时间！", Snackbar.LENGTH_SHORT).show();
                add_medicine_e.setText("");
                } else if (compareDate(add_medicine_e.getText().toString(), add_medicine_s.getText().toString()) == -1) {
                    Snackbar.make(add_e_button, "时间小于起始时间,请重新选择", Snackbar.LENGTH_SHORT).show();
                    add_medicine_e.setText("");
                }
        }
    }

    private int compareDate(String s, String s1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(s);
            Date dt2 = df.parse(s1);
            if (dt1.getTime() >= dt2.getTime()) {
                System.out.println();
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    private void initView() {
        add_medicine_name = (EditText) findViewById(R.id.add_medicine_name);
        add_medicine_time = (EditText) findViewById(R.id.add_times);
        add_medicine_amount = (EditText) findViewById(R.id.add_amount);
        add_medicine_s = (TextView) findViewById(R.id.start_time_text);
        add_medicine_e = (TextView) findViewById(R.id.end_time_text);
        add_s_button = (ImageButton) findViewById(R.id.choose_time);
        add_e_button = (ImageButton) findViewById(R.id.choose_time2);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
