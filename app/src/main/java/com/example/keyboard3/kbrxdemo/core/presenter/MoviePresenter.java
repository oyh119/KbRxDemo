package com.example.keyboard3.kbrxdemo.core.presenter;

import android.content.Context;

import com.example.keyboard3.kbrxdemo.core.subscribers.BaseSubscriber;
import com.example.keyboard3.kbrxdemo.core.subscribers.SubscriberOnNextListener;
import com.example.keyboard3.kbrxdemo.http.HttpMethods;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by keyboard3 on 2016/8/14.
 */

public class MoviePresenter extends BasePresenter {
    private MoviePresenter(Context context) {
        super(context);
    }

    public static MoviePresenter getInstance(Context context) {
        return new MoviePresenter(context);
    }

    /**
     * 进行网络请求 业务逻辑处理
     *
     * @param getTopMovieOnNext
     */
    public void getMovie(SubscriberOnNextListener getTopMovieOnNext, com.trello.rxlifecycle.components.support.RxFragment fragment, int start) {
        HttpMethods.getInstance()
                .getTopMovie(start, 12)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//必须在需要UI线程处理之前 切换到主线程
                .compose(fragment.bindToLifecycle())//自动相对应的生命周期中取消//bindUntilEvent(ActivityEvent.PAUSE)
                .subscribe(new BaseSubscriber(getTopMovieOnNext, context));
    }

}