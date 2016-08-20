package com.example.keyboard3.kbrxdemo.ui;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by asus on 2016/8/20.
 */

public class ProgramEntry extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
