package com.gougoucompany.clarence.smartbutler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gougoucompany.clarence.smartbutler.fragment.ButlerFragment;
import com.gougoucompany.clarence.smartbutler.fragment.GirlFragment;
import com.gougoucompany.clarence.smartbutler.fragment.UserFragment;
import com.gougoucompany.clarence.smartbutler.fragment.WechatFragment;
import com.gougoucompany.clarence.smartbutler.ui.SettingActivity;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title 存储四个标题的字符串
    private List<String> mTitle;
    //fragment 加载四个fragment
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉ActionBar的阴影
        getSupportActionBar().setElevation(0);

        initData();
        initView();

        //CrashReport.testJavaCrash();记录用户崩溃的问题，及时更改Bug

    }

    //初始化数据
    private void initData(){
        mTitle = new ArrayList<>();
        mTitle.add(getResources().getString(R.string.butler_service));
        mTitle.add(getResources().getString(R.string.wechat_selected));
        mTitle.add(getResources().getString(R.string.beautiful_picture));
        mTitle.add(getResources().getString(R.string.personal_center));

        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }

    //初始化View
    private void initView(){
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);

        //默认隐藏
        fab_setting.setVisibility(View.INVISIBLE);


        //预加载
        /*viewPager.setOffscreenPageLimit(3) 表示三个页面之间来回切换都不会重新加载
        * 在使用ViewPager加载Fragment的时候，滑动每个Fragment的时候，都会重新调用onCreate()方法。
        * 因为Fragment页面没有被缓存，使用上面的方法之后，可以缓存Fragment页面，避免滑动显示的时候
        * 多次调用onCreate()方法来进行刷新*/
        mViewPager.setOffscreenPageLimit(mFragment.size());

        //ViewPager的滑动监听，当时第一个fragment的时候悬浮按钮要隐藏
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               Log.i("TAG", "position:" + position ); //可以看到position是0，1,2,3 0是服务管家
                if(position == 0){
                    fab_setting.setVisibility(View.GONE);
                } else{
                    fab_setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置适配器 ViewPager继承自ViewGroup,是个容器，和ListView类似，需要适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
























































