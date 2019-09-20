package com.example.varun.vesica.eventbus;

import com.google.gson.JsonObject;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by smart on 17-May-16.
 */

/* Classic eventbus with rx java library, creates instance of the singleton, a stream of data and observables
 * Replaces the need for an interface and allows for MUCH MORE customization
 * todo: Add auto sms reader and combine with NewMessageBus
 */

public class NewMessageBus {


        private PublishSubject<JsonObject> subject = PublishSubject.create();


        public void setEvent(JsonObject object) {
            subject.onNext(object);
        }


        public Observable<JsonObject> getEvent() {
            return subject;
        }

}
