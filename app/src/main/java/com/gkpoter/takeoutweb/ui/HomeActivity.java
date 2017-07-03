package com.gkpoter.takeoutweb.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gkpoter.takeoutweb.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by "GKpoter" on 2017/7/3.
 */

@ContentView(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
