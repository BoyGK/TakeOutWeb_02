package com.gkpoter.takeoutweb.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gkpoter.takeoutweb.R;
import com.gkpoter.takeoutweb.ui.fragment.MHomeFragment;
import com.gkpoter.takeoutweb.ui.fragment.MMarketFragment;
import com.gkpoter.takeoutweb.ui.fragment.MMyFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by "GKpoter" on 2017/7/3.
 */

@ContentView(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity {

    @ViewInject(R.id.main_viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.bottom_menu)
    BottomNavigationView bottom_bar;

    private Fragment[] fragments;
    private FragmentPagerAdapter adapter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initViewPager();
    }

    private void initViewPager(){
        fragments=new Fragment[3];
        fragmentManager=getSupportFragmentManager();
        fragments[0]=new MHomeFragment();
        fragments[1]=new MMarketFragment();
        fragments[2]=new MMyFragment();
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

        viewPager.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;  //修改为true
            }
        });

        bottom_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager.setCurrentItem(item.getGroupId());
                return true;
            }
        });
    }
}
