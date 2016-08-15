package com.example.keyboard3.kbrxdemo.activity;

import android.support.v7.app.AppCompatActivity;

import com.example.keyboard3.kbrxdemo.core.BasePresenter;

/**
 * Created by asus on 2016/8/14.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public abstract BasePresenter getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(getPresenter()!=null){
            getPresenter().cancelAll();
        }
    }
}
