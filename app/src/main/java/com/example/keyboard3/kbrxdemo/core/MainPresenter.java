package com.example.keyboard3.kbrxdemo.core;

import android.content.Context;
import android.widget.Toast;

import com.example.keyboard3.kbrxdemo.http.HttpConfig.RetryWhenNetworkException;
import com.example.keyboard3.kbrxdemo.http.HttpMethods;
import com.example.keyboard3.kbrxdemo.subscribers.ProgressSubscriber;
import com.example.keyboard3.kbrxdemo.subscribers.SubscriberOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/8/14.
 */

public class MainPresenter extends BasePresenter {
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
    public void getMovie(SubscriberOnNextListener getTopMovieOnNext, RxAppCompatActivity activity) {
        HttpMethods.getInstance()
                .getTopMovie(0, 10)
                .observeOn(Schedulers.io())
                .filter(o -> {//延长请求时间 运行在IO线程中
                    try {
                        Thread.sleep(1000);
                        System.out.println("ganchunyu-filter"+Thread.currentThread());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .retryWhen(new RetryWhenNetworkException())
                .observeOn(AndroidSchedulers.mainThread())//必须在需要UI线程处理之前 切换到主线程
                .doOnUnsubscribe(() -> {
                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                })
                .compose(activity.bindToLifecycle())//自动相对应的生命周期中取消//bindUntilEvent(ActivityEvent.PAUSE)
                .subscribe(new ProgressSubscriber(getTopMovieOnNext, context));
    }
}
