package com.example.keyboard3.kbrxdemo.core;

import android.content.Context;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by asus on 2016/8/14.
 */

public abstract class BasePresenter {
    protected CompositeSubscription requestSubscriptions;
    protected Context context;
    public BasePresenter(Context context){
        this.context = context;
        requestSubscriptions = new CompositeSubscription();
    }
    /**
     * 统一释放请求
     */
    public void cancelAll() {
        requestSubscriptions.unsubscribe();
    }
}
