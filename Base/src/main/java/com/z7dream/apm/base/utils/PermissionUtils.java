package com.z7dream.apm.base.utils;

import android.app.Activity;

import com.z7dream.apm.base.utils.listener.Callback;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Z7Dream on 2017/10/17 11:06.
 * Email:zhangxyfs@126.com
 */

public class PermissionUtils {
    private RxPermissions rxPermissions;

    public PermissionUtils(Activity activity) {
        rxPermissions = new RxPermissions(activity);
    }

    public void toPermission(final Callback<Boolean> callback, String... permissions) {
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean o) throws Exception {
                callback.event(o);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable o) throws Exception {

            }
        });
    }

    public Observable<Boolean> toPermission(String... permissions) {
        return rxPermissions.request(permissions);
    }

}
