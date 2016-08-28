package com.example.keyboard3.kbrxdemo.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.keyboard3.kbrxdemo.core.presenter.MoviePresenter;
import com.example.keyboard3.kbrxdemo.model.Subject;
import com.example.keyboard3.kbrxdemo.ui.fragment.common.RecyclerFragment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.zhaiyifan.interestingtitlebar.CustomTitleBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends RecyclerFragment<Subject> {
    private MoviePresenter presenter;

    public static MovieFragment newInstance() {

        Bundle args = new Bundle();

        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initItemLayout() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected void itemConvert(ViewHolder holder, Subject subject, int position) {
        holder.setText(android.R.id.text1, subject.getTitle());
    }

    @Override
    protected void preInit() {
        presenter = MoviePresenter.getInstance(getContext());
    }

    @Override
    protected void handleHeader(CustomTitleBarUtils utils) {
        utils
                .setTitle("电影列表")
                .setRightView(null)
                .setLeftView(null).start();
    }

    protected void load(int page) {
        presenter.getMovie(getListOnNext, this, page);
    }
}
