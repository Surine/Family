package com.surine.family.UI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.surine.family.Data.UrlData;
import com.surine.family.R;

public class TestActivity extends AppCompatActivity {
    EditText mEditText;
    Button mButton;
    Button mSaveButton;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //init layout
        initView();
        setListener();

    }

    private void setListener() {
       mButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               url = UrlData.medicine_post;
               mEditText.setText(url);
           }
       });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mySharedPreferences= getSharedPreferences("data",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("ip", mEditText.getText().toString());
                editor.commit();
                Toast.makeText(TestActivity.this, "数据成功写入！" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
       mEditText = (EditText) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.button);
        mSaveButton = (Button) findViewById(R.id.button2);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Myfinish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            Myfinish();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    private void Myfinish() {
      //do'sth
        finish();
    }


}
