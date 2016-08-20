package com.example.keyboard3.kbrxdemo.http.okhttp;

import android.util.Log;

import com.blankj.utilcode.utils.NetworkUtils;
import com.example.keyboard3.kbrxdemo.core.Config;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 不需要就不使用
 * 云端响应头拦截器，用来配置缓存策略
 * Dangerous interceptor that rewrites the server's cache-control header. *
 */

public class RewriteCacheControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        /**
         * todo
         * 此处对列表应该有特殊处理：页码 start  时间标记 time
         * if start=0&time=0 刷新（不处理request,写缓存）
         * if start=0&time=1 不刷新（处理request,写缓存）
         * if start>0 (不处理，不写缓存)
         * 最后需要将request的Url中的这个时间标记去掉
         */
        if(!NetworkUtils.isConnected(Config.context)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)//没网强制读取缓存
                    .build();
            Log.d(Config.LOG_TAG,"no network");
        }
        Response originalResponse = chain.proceed(request);
        //响应的缓存时间设置
        if(NetworkUtils.isConnected(Config.context)){
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }else{
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
