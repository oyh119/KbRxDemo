package com.example.keyboard3.kbrxdemo.activity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.core.MainPresenter;
import com.example.keyboard3.kbrxdemo.subscribers.SubscriberOnNextListener;
import com.example.model.Subject;
import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Bind(R.id.click_me_BN)
    Button clickMeBN;
    @Bind(R.id.result_TV)
    TextView resultTV;

    private SubscriberOnNextListener getTopMovieOnNext;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initMonitorNetwork();
        bindView();
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

    private void init() {
        presenter =MainPresenter.getInstance(this);
        getTopMovieOnNext = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                resultTV.setText(subjects.toString());
            }
        };
    }

    private void bindView() {
        RxView.clicks(clickMeBN)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
            presenter.getMovie(getTopMovieOnNext,this);
        });
    }
}
