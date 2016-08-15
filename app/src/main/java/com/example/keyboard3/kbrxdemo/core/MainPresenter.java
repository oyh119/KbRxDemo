package com.example.keyboard3.kbrxdemo.core;

import android.content.Context;

import com.example.keyboard3.kbrxdemo.http.HttpConfig.RetryWhenNetworkException;
import com.example.keyboard3.kbrxdemo.http.HttpMethods;
import com.example.keyboard3.kbrxdemo.subscribers.ProgressSubscriber;
import com.example.keyboard3.kbrxdemo.subscribers.SubscriberOnNextListener;

/**
 * Created by asus on 2016/8/14.
 */

public class MainPresenter extends BasePresenter{
    private static MainPresenter sigleton;

    private MainPresenter(Context context) {
        super(context);
    }

    public static MainPresenter getInstance(Context context) {
        if (sigleton == null) {
            sigleton = new MainPresenter(context);
        }
        return sigleton;
    }

    /**
     * 进行网络请求 业务逻辑处理
     *
     * @param getTopMovieOnNext
     */
    public void getMovie(SubscriberOnNextListener getTopMovieOnNext) {
        requestSubscriptions.add(
                HttpMethods.getInstance()
                        .getTopMovie(0, 10)
                        .retryWhen(new RetryWhenNetworkException())
                        .subscribe(new ProgressSubscriber(getTopMovieOnNext, context))
        );
    }
}
