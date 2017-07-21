package com.surine.family.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.surine.family.Adapter.Viewpager.SimpleFragmentPagerAdapter;
import com.surine.family.Fragment.First_page_fragment_one;
import com.surine.family.Fragment.First_page_fragment_three;
import com.surine.family.Fragment.First_page_fragment_two;
import com.surine.family.InitApp.BaseActivity;
import com.surine.family.MQTT.Client;
import com.surine.family.R;
import com.surine.family.Service.MqttService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fab;
    private TabLayout tab;
    private ViewPager viewpager;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments =new ArrayList<>();
    private List<String> titles =new ArrayList<>();
    private int Position_page = 0;
    public static Client mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewPager();


        new Thread(new Runnable() {
            @Override
            public void run() {
                //启动mqtt服务
                startService(new Intent(MainActivity.this, MqttService.class));
            }
        }).start();



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(Position_page == 0){
                 Toast.makeText(MainActivity.this,"关爱成功！",Toast.LENGTH_SHORT).show();
              }else if(Position_page == 1){
                  startActivity(new Intent(MainActivity.this,AddMedicineActivity.class));
              }else if(Position_page == 2){
                      Intent intent = new Intent(MainActivity.this, CallActivity.class);
                      startActivity(intent);
              }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initViewPager() {
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        tab = (TabLayout)findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(First_page_fragment_one.getInstance("1"));
        fragments.add(First_page_fragment_two.getInstance("2"));
        fragments.add(First_page_fragment_three.getInstance("3"));

        //3.使用string的list集合来添加标题
        titles.add("数据");
        titles.add("提醒");
        titles.add("通话");


        //4.初始化适配器（传入参数：FragmentManager，碎片集合，标题）
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        //5.设置viewpager适配器
        viewpager.setAdapter(pagerAdapter);

        //6.设置缓存
        /*
        *  * 注意：设置缓存的原因
        * 在加载Tab-A时会实例化Tab-B中fragment，依次调用：onAttach、
        * onCreate、onCreateView、onActivityCreated、onStart和onResume。
        * 同样切换到Tab-B时也会初始化Tab-C中的fragment。（Viewpager预加载）
        * 但是fragment中的数据(如读取的服务器数据)没有相应清除，导致重复加载数据。
        *
        *
        * 注意：ps:我们在使用viewpager时会定义一个适配器adapter，其中实例化了一个fragment列表，
        * 所以在tab切换时fragment都是已经实例化好的，所以在切换标签页时是不会重新实例化fragment
        * 对象的，因而在fragment中定义的成员变量是不会被重置的。所以为列表初始化数据需要注意这个问题。
        *
        * 参考网址：https://my.oschina.net/buobao/blog/644699
*/
        viewpager.setOffscreenPageLimit(3);
        //7.关联viewpager
        tab.setupWithViewPager(viewpager);


        //8.viewpager的监听器
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动监听器
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页卡选中监听器
            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    fab.setImageResource(R.drawable.ic_action_love);
                    Position_page = 0;
                }
                else if (position==1)
                {
                    fab.setImageResource(R.drawable.ic_action_plus);
                    Position_page = 1;
                }
                else if(position==2)
                {
                    fab.setImageResource(R.drawable.ic_action_call);
                    Position_page = 2;
                }
            }
            //滚动状态变化监听器
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tab.getTabAt(0).setIcon(R.drawable.ic_action_eye);
        tab.getTabAt(1).setIcon(R.drawable.ic_action_notify);
        tab.getTabAt(2).setIcon(R.drawable.ic_action_call);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this,"night",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
           startActivity(new Intent(MainActivity.this,TestActivity.class));
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"setting",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
           showAboutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("关于");
        builder.setMessage("\n黄昏，\n别让他们老无所依。");
        builder.setPositiveButton("如此",null);
        builder.show();
    }
}


/**
 *  @Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.main, menu);
return true;
}

 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
 // Handle action bar item clicks here. The action bar will
 // automatically handle clicks on the Home/Up button, so long
 // as you specify a parent activity in AndroidManifest.xml.
 int id = item.getItemId();

 //noinspection SimplifiableIfStatement
 if (id == R.id.action_settings) {
 return true;
 }

 return super.onOptionsItemSelected(item);
 }
 */

