package com.example.keyboard3.kbrxdemo.http;

import com.example.keyboard3.kbrxdemo.http.exception.ExceptionEngine;
import com.example.model.HttpResult;
import com.example.model.Subject;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/8/14.
 */

public class HttpMethods {
    private  static HttpMethods singleton;
    private static HttpMethodsConfig config;
    private MovieService movieService;
    private HttpMethods(){
        config=HttpMethodsConfig.getInstance();
        movieService=config.getMovieService();
    }
    public class LiftAllTransformer<R> implements Observable.Transformer<R, R> {
        @Override
        public Observable<R> call(Observable<R> observable) {
            return observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io());
        }
    }
    public static HttpMethods getInstance(){
        if(singleton==null){
            singleton=new HttpMethods();
        }
        return singleton;
    }

    private class ServerResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            /*if (httpResult.getResultCode() != 0) {
                throw new ApiException(httpResult.getResultCode());
            }*/
            return httpResult.getSubjects();
        }
    }

    private class HttpResultFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }
    /**
     * 用于获取豆瓣电影Top250的数据
     * @param start 起始位置
     * @param count 获取长度
     */
    public Observable getTopMovie(int start, int count){
        return movieService.getTopMovie(start, count)
                .compose(new LiftAllTransformer())
                .map(new ServerResultFunc<List<Subject>>())
                .onErrorResumeNext(new HttpResultFunc())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
