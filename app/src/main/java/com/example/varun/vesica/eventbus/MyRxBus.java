package com.example.varun.vesica.eventbus;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by varun on 5/10/16.
 */
public class MyRxBus {

    private static MyRxBus instance;

    private PublishSubject<Object> subject = PublishSubject.create();

    public static MyRxBus instanceOf() {
        if (instance == null) {
            instance = new MyRxBus();
        }
        return instance;
    }

    /**
     * Pass any event down to event listeners.
     */
    public void setEvent(Object object) {
        subject.onNext(object);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<Object> getEvents() {
        return subject;
    }
}