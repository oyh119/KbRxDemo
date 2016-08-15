package com.example.keyboard3.kbrxdemo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.keyboard3.kbrxdemo.R;
import com.example.keyboard3.kbrxdemo.core.BasePresenter;
import com.example.keyboard3.kbrxdemo.core.MainPresenter;
import com.example.keyboard3.kbrxdemo.subscribers.SubscriberOnNextListener;
import com.example.model.Subject;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        bindView();
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
            presenter.getMovie(getTopMovieOnNext);
        });
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }
}
