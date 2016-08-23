package com.example.keyboard3.kbrxdemo.http;

import com.example.keyboard3.kbrxdemo.http.config.HttpMethodsConfig;
import com.example.keyboard3.kbrxdemo.http.config.RetryWhenNetworkException;
import com.example.keyboard3.kbrxdemo.http.exception.ExceptionEngine;
import com.example.model.HttpResult;
import com.example.model.Subject;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 所有网络请求在这里实现
 * Created by keyboard3 on 2016/8/14.
 */

public class HttpMethods {
    private static HttpMethodsConfig config;
    private MovieService movieService;
    private static HttpMethods singleton;
    private HttpMethods() {
        config = HttpMethodsConfig.getInstance();
        movieService = config.getMovieService();
    }

    /**
     * 通用流处理
     *
     * @param <R>
     */
    public class LiftAllTransformer<R> implements Observable.Transformer<R, R> {
        @Override
        public Observable<R> call(Observable<R> observable) {
            return observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io());
        }
    }

    public static HttpMethods getInstance() {
        if (singleton == null) {
            singleton = new HttpMethods();
        }
        return singleton;
    }

    /**
     * 服务器指定异常处理
     *
     * @param <T>
     */
    private class ServerResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            //根据基类的返回码 来判断 是否需要抛出服务端指定异常
            /*if (httpResult.getResultCode() != 0) {
                throw new ApiException(httpResult.getResultCode());
            }*/
            return httpResult.getSubjects();
        }
    }

    /**
     * 统一异常处理引擎
     *
     * @param <T>
     */
    private class HttpResultFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param start 起始位置
     * @param count 获取长度
     */
    public Observable getTopMovie(int start, int count) {
        return movieService.getTopMovie(start, count)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWhenNetworkException())//连接重试
                .map(new ServerResultFunc<List<Subject>>())//指定服务器异常
                .onErrorResumeNext(new HttpResultFunc())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
