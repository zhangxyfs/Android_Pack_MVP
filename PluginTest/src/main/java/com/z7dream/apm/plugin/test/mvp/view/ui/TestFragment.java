package com.z7dream.apm.plugin.test.mvp.view.ui;

import android.os.Bundle;
import android.view.View;

import com.z7dream.apm.base.utils.rx.RxBus;
import com.z7dream.apm.plugin.test.R;
import com.z7dream.apm.plugin.test.mvp.base.PluginBaseFragment;
import com.z7dream.apm.plugin.test.mvp.presenter.TestPresenter;
import com.z7dream.apm.plugin.test.mvp.view.contract.TestContract;
import com.z7dream.apm.plugin.test.mvp.view.model.TestModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Z7Dream on 2017/10/24 11:27.
 * Email:zhangxyfs@126.com
 */

public class TestFragment extends PluginBaseFragment<TestContract.Presenter> implements TestContract.View {

    @Override
    protected int layoutID() {
        return R.layout.activity_plugin_test;
    }


    @NotNull
    @Override
    protected TestContract.Presenter createPresenter() {
        return new TestPresenter(getContext(), this);
    }

    @Override
    protected void init(View view, Bundle bundle) {
        if (getHandler() != null)
            getHandler().postDelayed(() -> RxBus.Companion.get().post("host_main_activity", 1), 5000);
    }

    @Override
    protected void data() {

    }

    @Override
    public void getDataSucc(@NotNull List<? extends TestModel> modelList, boolean isRef) {

    }

    @Override
    public void getDataFail(@Nullable String str) {

    }
}
