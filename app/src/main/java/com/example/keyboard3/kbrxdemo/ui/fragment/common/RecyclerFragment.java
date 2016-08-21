package com.example.keyboard3.kbrxdemo.ui.fragment.common;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.core.subscribers.SubscriberOnNextListener;
import com.example.keyboard3.kbrxdemo.ui.fragment.common.BaseFragment;
import com.example.keyboard3.kbrxdemo.view.DefaultLoadingViewGroup;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import cn.zhaiyifan.interestingtitlebar.CustomTitleBar;
import cn.zhaiyifan.interestingtitlebar.CustomTitleBarUtils;


/**
 * RecyclerFragment :一个抽象列表页逻辑代码的Fragment
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
    private volatile int page = start;//当前页
    private short loadMore = 1;
    private short refresh = 2;
    private short normal = 0;
    private volatile int isLoadRefresh = normal;//当前状态
    private LoadMoreWrapper mLoadMoreWrapper;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        bindView();
    }

    /**
     * 获取list item Layout的布局id
     *
     * @return
     */
    protected abstract int initItemLayout();

    /**
     * list  adapter 的convert的处理
     *
     * @param holder
     * @param subject
     * @param position
     */
    protected abstract void itemConvert(ViewHolder holder, T subject, int position);

    /**
     * 绑定View的处理
     */
    private void bindView() {
        adapter = new CommonAdapter<T>(getContext(), initItemLayout(), mDatas) {
            @Override
            protected void convert(ViewHolder holder, T subject, int position) {
                itemConvert(holder, subject, position);
            }
        };
        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        //todo  如果仿造知乎  这里就需要设置View了，监听网络情况。内部还开放请求接口
        DefaultLoadingViewGroup loadingView= (DefaultLoadingViewGroup) LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.default_loading_parent,null,false);
        mLoadMoreWrapper.setLoadMoreView(loadingView);
        mLoadMoreWrapper.setOnLoadMoreListener(() -> {
            if (isLoadRefresh == normal) {
                isLoadRefresh = loadMore;
                load(page++);
            }
        });
        rlContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rlContent.setAdapter(mLoadMoreWrapper);
    }
    /**
     * todo
     * 1.当前列表是空的，并且响应的无数据返回 显示错误页面
     * 2.有数据返回，显示列表页
     * 通用方法处理
     */
    /**
     * 初始化之前的操作
     */
    protected abstract void preInit();

    private void init() {
        preInit();
        initHeader();
        initList();
    }

    /**
     * 设置标题头
     */
    protected void initHeader() {
        final CustomTitleBar bar = (CustomTitleBar) getView().findViewById(R.id.title_bar);
        CustomTitleBarUtils utils = CustomTitleBarUtils.getInstance(bar);
        handleHeader(utils);
    }

    protected abstract void handleHeader(CustomTitleBarUtils utils);

    private void initList() {
        getListOnNext = new SubscriberOnNextListener<List<T>>() {
            @Override
            public void onNext(List<T> datas) {
                if (isLoadRefresh == refresh) {
                    swipeRefresh.setRefreshing(false);
                    mDatas.clear();
                    handleResult(datas);
                } else if (isLoadRefresh == loadMore) {
                    handleResult(datas);
                }
            }

            /**
             * 统一处理加载数据的结果
             * @param datas
             */
            private void handleResult(List<T> datas) {
                mDatas.addAll(datas);
                mLoadMoreWrapper.notifyDataSetChanged();
                isLoadRefresh = normal;
            }
        };
        swipeRefresh.setOnRefreshListener(() -> {
            if (isLoadRefresh == normal) {
                isLoadRefresh = refresh;
                //todo 如果需要列表缓存 if page=start load(start,time=0) else 1
                load(start);
            }
        });
        load(start);
    }

    /**
     * 加载列表页数据     *
     * @param page
     */
    protected abstract void load(int page);
}