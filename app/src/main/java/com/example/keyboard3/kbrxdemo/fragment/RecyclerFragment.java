package com.example.keyboard3.kbrxdemo.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.subscribers.SubscriberOnNextListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class RecyclerFragment<T> extends BaseFragment {
    @Bind(R.id.rl_content)
    RecyclerView rlContent;
    List<T> mDatas = new LinkedList<>();
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    protected SubscriberOnNextListener getListOnNext;
    private CommonAdapter adapter;
    private int start = 0;//第一页
    private int page = start;//当前页

    private short loadMore=1;
    private short refresh=2;
    private short normal=0;
    private volatile int isLoadRefresh = normal;//当前状态
    private LoadMoreWrapper mLoadMoreWrapper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        bindView();
    }

    protected abstract int initItemLayout();
    protected abstract void itemConvert(ViewHolder holder, T subject, int position);
    private void bindView() {
        adapter = new CommonAdapter<T>(getContext(),initItemLayout(),mDatas) {
            @Override
            protected void convert(ViewHolder holder, T subject, int position) {
                itemConvert(holder,subject,position);
            }
        };
        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(() -> {
            if(isLoadRefresh==normal){
                isLoadRefresh=loadMore;
                load(page++);
            }
        });
        rlContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rlContent.setAdapter(mLoadMoreWrapper);
    }

    protected abstract void preInit();
    private void init() {
        preInit();
        getListOnNext = new SubscriberOnNextListener<List<T>>() {
            @Override
            public void onNext(List<T> datas) {
                if(isLoadRefresh==refresh){
                    swipeRefresh.setRefreshing(false);
                    mDatas.clear();
                    handleResult(datas);
                }else if(isLoadRefresh==loadMore){
                    handleResult(datas);
                }
            }

            private void handleResult(List<T> datas) {
                mDatas.addAll(datas);
                mLoadMoreWrapper.notifyDataSetChanged();
                isLoadRefresh=normal;
            }
        };
        swipeRefresh.setOnRefreshListener(() -> {
            if(isLoadRefresh==normal){
                isLoadRefresh=refresh;
                load(start);
            }
        });
        load(start);
    }

    protected abstract void load(int page);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}