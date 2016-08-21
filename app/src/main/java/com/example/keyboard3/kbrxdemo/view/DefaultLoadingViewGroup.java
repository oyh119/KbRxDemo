package com.example.keyboard3.kbrxdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.keyboard3.kbrxdemo.R;

/**
 * Created by keyboard3 on 2016/8/21.
 */

public class DefaultLoadingViewGroup extends RelativeLayout {
    //LoadingView
    View loadingView;
    //错误视图
    RelativeLayout loadingErrorView;

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
        //处理错误视图交互逻辑
        show(false);
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
    //切换视图接口
    private void initShowView(){
        curView.setVisibility(View.VISIBLE);
        otherView.setVisibility(View.GONE);
    }

}
