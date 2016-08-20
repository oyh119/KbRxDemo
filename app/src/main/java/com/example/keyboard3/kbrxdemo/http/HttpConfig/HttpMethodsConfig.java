package com.example.keyboard3.kbrxdemo.http.HttpConfig;


import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.utils.SDCardUtils;
import com.example.keyboard3.kbrxdemo.core.Config;
import com.example.keyboard3.kbrxdemo.http.MovieService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liukun on 16/3/9.
 */
public class HttpMethodsConfig {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private MovieService movieService;

    //构造方法私有
    private HttpMethodsConfig() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Cache cache = getCache(Config.context);
        builder.cache(cache);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    @NonNull
    private Cache getCache(Context context) {
        String cachePath;
        Cache cache;
        if(SDCardUtils.isSDCardEnable()){
            cachePath= SDCardUtils.getSDCardPath()+ Config.cacheDir+"/"+"Http";
        }else{
            cachePath=context.getCacheDir()+ Config.cacheDir+"/"+"Http";
        }
        cache=new Cache(new File(cachePath),30000);
        return cache;
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethodsConfig INSTANCE = new HttpMethodsConfig();
    }

    //获取单例
    public static HttpMethodsConfig getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public MovieService getMovieService(){
        return movieService;
    }
}