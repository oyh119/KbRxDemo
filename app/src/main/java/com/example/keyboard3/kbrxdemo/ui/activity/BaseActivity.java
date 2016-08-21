package com.example.keyboard3.kbrxdemo.ui.activity;

import android.support.v4.app.Fragment;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by keyboard3 on 2016/8/14.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    public void fragmentReplace(int rId, Fragment fragment) {
        android.support.v4.app.FragmentTransaction tran=getSupportFragmentManager().beginTransaction();
        tran.replace(rId, fragment);
        tran.commit();
    }
}
