package com.example.keyboard3.kbrxdemo.core.presenter;

import android.content.Context;

/**
 * Created by asus on 2016/8/14.
 */

public abstract class BasePresenter {
    protected Context context;
    public BasePresenter(Context context){
        this.context = context;
    }
}
