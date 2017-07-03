package com.gkpoter.takeoutweb.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gkpoter.takeoutweb.R;
import com.gkpoter.takeoutweb.bean.UserBean;
import com.gkpoter.takeoutweb.interface_.MyCallBack;
import com.gkpoter.takeoutweb.ui.fragment.LoginLeftFragment;
import com.gkpoter.takeoutweb.ui.fragment.LoginRightFragment;
import com.gkpoter.takeoutweb.util.DataUtils;
import com.gkpoter.takeoutweb.util.HttpUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by "GKpoter" on 2017/7/3.
 */

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.login_viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.login_tab)
    TabLayout tab;

    private Fragment[] fragments;
    private FragmentPagerAdapter adapter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initViewPager();
    }

    private void initViewPager(){
        fragments=new Fragment[2];
        fragmentManager=getSupportFragmentManager();
        fragments[0]=new LoginLeftFragment();
        fragments[1]=new LoginRightFragment();
        adapter=new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}
            @Override
            public void onPageSelected(int i) {

            }
            @Override
            public void onPageScrollStateChanged(int i) {}
        });
        tab.setupWithViewPager(viewPager);
        tab.getTabAt(0).setText("账号密码登录");
        tab.getTabAt(1).setText("手机号登录");
        viewPager.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;  //修改为true
            }
        });
    }

}
