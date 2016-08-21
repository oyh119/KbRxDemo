package com.example.keyboard3.kbrxdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.keyboard3.kbrxdemo.R;

/**
 * Created by keyboard3 on 2016/8/21.
 */

public class DefaultLoadingViewGroup extends RelativeLayout {
    //LoadingView
    View loadingView;
    //错误视图
    RelativeLayout loadingErrorView;
    TextView tvErroLoad;
    View curView;//主视图
    View otherView;//另外的视图

    public DefaultLoadingViewGroup(Context context) {
        super(context);
    }

    public DefaultLoadingViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DefaultLoadingViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //加载Loding
        loadingView=findViewById(R.id.default_loading);
        //加载错误视图
        loadingErrorView= (RelativeLayout) findViewById(R.id.default_loading_error);
        if(loadingView==null){
            throw  new IllegalArgumentException("should have R.id.default_loading");
        }
        if(loadingErrorView==null){
            throw new IllegalArgumentException("should have R.id.default_loading_error");
        }
        tvErroLoad= (TextView) loadingErrorView.findViewById(R.id.tv_error_load);
        if(tvErroLoad==null){
            throw new IllegalArgumentException("should have R.id.tv_error_load");
        }
        //处理错误视图交互逻辑
        show(true);
    }
    public void show(boolean showLoading){
        if(showLoading){
            curView=loadingView;
            otherView=loadingErrorView;
        }
        else{
            curView=loadingErrorView;
            otherView=loadingView;
        }
        initShowView();
    }

    /**
     * 设置点击加载更多
     * @param click
     */
    public void setLoadingListener(OnClickListener click){
        loadingErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onClick(v);
                show(true);
            }
        });
    }
    //切换视图接口
    private void initShowView(){
        curView.setVisibility(View.VISIBLE);
        otherView.setVisibility(View.GONE);
    }

}
