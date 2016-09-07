package com.example.keyboard3.kbrxdemo.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.loadmore.DefaultFootItem;
import com.cjj.loadmore.OnLoadMoreListener;
import com.cjj.loadmore.RecyclerViewWithFooter;
import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.RecyclerBinding;
import com.example.keyboard3.kbrxdemo.core.Config;
import com.example.keyboard3.kbrxdemo.core.presenter.MoviePresenter;
import com.example.keyboard3.kbrxdemo.http.subscribers.SubscriberOnNextListener;
import com.example.keyboard3.kbrxdemo.model.Subject;
import com.github.markzhai.recyclerview.SingleTypeAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindDingRecyclerFragment extends com.trello.rxlifecycle.components.support.RxFragment {
    private MoviePresenter presenter;
    private RecyclerViewWithFooter mRecyclerViewWithFooter;
    private SubscriberOnNextListener getListOnNext;
    private int start = 0;
    private volatile int page = start;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerBinding mBinding;
    private SingleTypeAdapter mSingleTypeAdapter;

    public static BindDingRecyclerFragment newInstance() {

        Bundle args = new Bundle();

        BindDingRecyclerFragment fragment = new BindDingRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bind_ding_recycler, container, false);

        mSingleTypeAdapter = new SingleTypeAdapter<>(getContext(), R.layout.item_bind_recycler);
        mBinding.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.setAdapter(mSingleTypeAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = MoviePresenter.getInstance(getContext());
        getListOnNext = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> list) {
                //数据加载成功之后 对刷新和加载的处理
                if (swipeRefreshLayout.isRefreshing()) {
                    Log.d(Config.LOG_TAG, "刷新:" + list.size());
                    swipeRefreshLayout.setRefreshing(false);
                    mSingleTypeAdapter.clear();
                    mSingleTypeAdapter.addAll(list);
                } else if (mRecyclerViewWithFooter.isLoadMoreEnable()) {
                    //是否包含下一页 page+1
                    page++;
                    Log.d(Config.LOG_TAG, "加载更多:" + list.size());
                    mSingleTypeAdapter.addAll(list);
                }
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mRecyclerViewWithFooter.isLoadMoreEnable()) {
                    page = start;
                    //加载第一页
                    load(page);
                }
            }
        });
        mRecyclerViewWithFooter = (RecyclerViewWithFooter) getView().findViewById(R.id.rl_content);
        mRecyclerViewWithFooter.setFootItem(new DefaultFootItem());//默认是这种
        mRecyclerViewWithFooter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!swipeRefreshLayout.isRefreshing()) {
                    //加载更多
                    load(page + 1);
                }
            }
        });
        //请求第一页
        load(page);
    }

    protected void load(int page) {
        Log.d(Config.LOG_TAG, "page:" + page);
        if (page == start && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        presenter.getMovie(getListOnNext, this, page);
    }
}