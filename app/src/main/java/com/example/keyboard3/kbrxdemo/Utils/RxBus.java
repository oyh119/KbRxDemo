package com.example.keyboard3.kbrxdemo.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Desc:
 * Author: ganchunyu
 * Date: 2016-08-15 19:59
 */
// this is the middleman object
public class RxBus {
    private RxBus() {
    }

    /**
     * 一个事件（主题）
     */
    private static final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public RxBus getInstance() {
        return new RxBus();
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }
}