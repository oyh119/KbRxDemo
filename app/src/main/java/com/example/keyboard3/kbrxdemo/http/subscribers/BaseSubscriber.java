package com.example.keyboard3.kbrxdemo.http.subscribers;

import android.content.Context;
import android.widget.Toast;

import com.example.keyboard3.kbrxdemo.http.exception.ApiException;

import rx.Subscriber;

/**
 * Desc:
 * Author: ganchunyu
 * Date: 2016-08-16 16:14
 */
public class BaseSubscriber<T> extends Subscriber<T> {
    SubscriberOnNextListener mSubscriberOnNextListener;

    private Context context;
    private boolean isShow=true;
    public BaseSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = subscriberOnNextListener;
        this.context = context;
    }
    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            Toast.makeText(context, "error_code:" + exception.getCode() + "\nerror_msg:" + exception.getDisplayMessage(), Toast.LENGTH_SHORT).show();
        }
        mSubscriberOnNextListener.onError(e);
    }
    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    @Override
    public void onCompleted() {
    }
}
