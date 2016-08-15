package com.example.keyboard3.kbrxdemo.core;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import rx.Subscription;

/**
 * Created by asus on 2016/8/14.
 */

public abstract class BasePresenter {
    protected List<Subscription> requestList;
    protected Context context;
    public BasePresenter(Context context){
        this.context = context;
        requestList = new LinkedList<Subscription>();
    }
    /**
     * 统一释放请求
     */
    public void cancelAll() {
        for (Subscription item:requestList){
            if(item!=null){
                item.unsubscribe();
            }
        }
    }
}
