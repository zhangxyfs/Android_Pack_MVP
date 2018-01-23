package com.z7dream.apm.base.widget;

import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;


/**
 * Created by Z7Dream on 2017/9/28 17:09.
 * Email:zhangxyfs@126.com
 */

final public class ViewClickObservable extends Observable<Object> {
    private final View view;

    public ViewClickObservable(View view) {
        this.view = view;
    }

    @Override
    protected void subscribeActual(Observer<? super Object> observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(view, observer);
        observer.onSubscribe(listener);
        view.setOnClickListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements View.OnClickListener {
        private final View view;
        private final Observer<? super Object> observer;

        Listener(View view, Observer<? super Object> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onClick(View v) {
            if (!isDisposed()) {
                if (v.getTag() != null) {
                    observer.onNext(v.getTag());
                } else
                    observer.onNext("");
            }
        }

        @Override
        protected void onDispose() {
            view.setOnClickListener(null);
        }
    }
}
