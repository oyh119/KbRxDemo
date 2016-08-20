package com.example.keyboard3.kbrxdemo.ui.fragment;

import android.media.MediaCodec;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keyboard3.kbrxdemo.R;

import butterknife.ButterKnife;

/**
 * Desc:
 * Author: ganchunyu
 * Date: 2016-08-16 11:24
 */
public abstract class BaseFragment extends com.trello.rxlifecycle.components.support.RxFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(initLayoutId(), container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }
    protected abstract int initLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}