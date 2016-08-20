package com.example.keyboard3.kbrxdemo.core.presenter;

import android.content.Context;

import com.example.keyboard3.kbrxdemo.core.subscribers.BaseSubscriber;
import com.example.keyboard3.kbrxdemo.core.subscribers.SubscriberOnNextListener;
import com.example.keyboard3.kbrxdemo.http.config.RetryWhenNetworkException;
import com.example.keyboard3.kbrxdemo.http.HttpMethods;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/8/14.
 */

public class MoviePresenter extends BasePresenter {
    private static MoviePresenter sigleton;

    private MoviePresenter(Context context) {
        super(context);
    }

    public static MoviePresenter getInstance(Context context) {
        if (sigleton == null) {
            sigleton = new MoviePresenter(context);
        }
        return sigleton;
    }

    /**
     * 进行网络请求 业务逻辑处理
     *
     * @param getTopMovieOnNext
     */
    public void getMovie(SubscriberOnNextListener getTopMovieOnNext, com.trello.rxlifecycle.components.support.RxFragment fragment, int count) {
        HttpMethods.getInstance()
                .getTopMovie(count, 12)
                .observeOn(Schedulers.io())
                .retryWhen(new RetryWhenNetworkException())
                .observeOn(AndroidSchedulers.mainThread())//必须在需要UI线程处理之前 切换到主线程
                .compose(fragment.bindToLifecycle())//自动相对应的生命周期中取消//bindUntilEvent(ActivityEvent.PAUSE)
                .subscribe(new BaseSubscriber(getTopMovieOnNext,context));
    }
}