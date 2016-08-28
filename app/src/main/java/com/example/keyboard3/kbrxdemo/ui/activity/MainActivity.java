package com.example.keyboard3.kbrxdemo.ui.activity;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.ui.fragment.DataBind.DataBindingFragment;
import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        fragmentReplace(R.id.fl_content, DataBindingFragment.newInstance());
        //initMonitorNetwork();
    }

    /**
     * 监听网络变化
     */
    private void initMonitorNetwork() {
        ReactiveNetwork.observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .filter(Connectivity.hasState(NetworkInfo.State.CONNECTED))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    Log.d("keyboard3","已连接的网络类型："+connectivity.toString());
                });
    }

}
