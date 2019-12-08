package com.android.fajarsearchgithubusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.fajarsearchgithubusers.view.MainActivityView;

public class MainActivity extends AppCompatActivity {

    MainActivityView mainActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityView = new MainActivityView(MainActivity.this,null);
        setContentView(mainActivityView.getRootView());
        mainActivityView.initView();
    }
}
