package com.example.keyboard3.kbrxdemo.ui;

import android.app.Application;

import com.example.keyboard3.kbrxdemo.core.Config;

/**
 * Created by keyboard3 on 2016/8/20.
 */

public class ProgramEntry extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Config.context=this;
    }
}