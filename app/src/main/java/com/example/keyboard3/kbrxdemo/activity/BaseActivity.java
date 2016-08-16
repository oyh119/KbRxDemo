package com.example.keyboard3.kbrxdemo.activity;

import android.support.v4.app.Fragment;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by asus on 2016/8/14.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    public void fragmentReplace(int rId, Fragment fragment) {
        android.support.v4.app.FragmentTransaction tran=getSupportFragmentManager().beginTransaction();
        tran.replace(rId, fragment);
        tran.commit();
    }
}
