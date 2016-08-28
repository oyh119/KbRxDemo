package com.example.keyboard3.kbrxdemo.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.cjj.loadmore.DefaultFootItem;
import com.cjj.loadmore.OnLoadMoreListener;
import com.cjj.loadmore.RecyclerViewWithFooter;
import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.core.presenter.MoviePresenter;
import com.example.keyboard3.kbrxdemo.core.subscribers.SubscriberOnNextListener;
import com.example.keyboard3.kbrxdemo.model.Subject;
import com.example.keyboard3.kbrxdemo.ui.fragment.common.BaseFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindDingRecyclerFragment extends BaseFragment {
    private MoviePresenter presenter;
    List<Subject> mDatas = new LinkedList<>();
    private RecyclerViewWithFooter mRecyclerViewWithFooter;
    private SubscriberOnNextListener getListOnNext;
    private int start = 0;
    private volatile int page = start;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter adapter;

    public static BindDingRecyclerFragment newInstance() {

        Bundle args = new Bundle();

        BindDingRecyclerFragment fragment = new BindDingRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = MoviePresenter.getInstance(getContext());
        getListOnNext = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    mDatas.clear();
                    mDatas.addAll(list);
                }
                if (mRecyclerViewWithFooter.isLoadMoreEnable()) {
                    mDatas.addAll(list);
                }
                adapter.notifyDataSetChanged();
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
                //加载第一页
                load(start);
            }
        });
        mRecyclerViewWithFooter = (RecyclerViewWithFooter) getView().findViewById(R.id.rl_content);
        adapter = new CommonAdapter<Subject>(getContext(), android.R.layout.simple_list_item_1, mDatas) {

            @Override
            protected void convert(ViewHolder holder, Subject subject, int position) {
                holder.setText(android.R.id.text1, subject.getTitle());
            }
        };
        mRecyclerViewWithFooter.setAdapter(adapter);
        mRecyclerViewWithFooter.setFootItem(new DefaultFootItem());//默认是这种
        mRecyclerViewWithFooter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //加载更多
                load(page++);
            }
        });
        //请求第一页
        load(start);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_bind_ding_recycler;
    }

    protected void load(int page) {
        presenter.getMovie(getListOnNext, this, page);
    }
}