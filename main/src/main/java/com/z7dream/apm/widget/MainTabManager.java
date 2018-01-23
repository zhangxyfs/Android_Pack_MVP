package com.z7dream.apm.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.z7dream.apm.R;
import com.z7dream.apm.plugin.test.mvp.view.ui.TestFragment;


/**
 * Created by win8 -1 on 2015/8/14.
 */
public class MainTabManager {
    private static MainTabManager mInstance;
    private TestFragment testFragment; //消息

    private Bundle mBundle;

    public static MainTabManager getInstance() {
        if (mInstance == null) {
            synchronized (MainTabManager.class) {
                if (mInstance == null) {
                    mInstance = new MainTabManager();
                }
            }
        }
        return mInstance;
    }

    //设置参数
    public MainTabManager setBundle(Bundle bundle) {
        mBundle = bundle;
        return mInstance;
    }

    public Fragment getFragmentByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.nav_left_first:
//                if (messageControlFragment == null) {
//                    messageControlFragment = new MessageControlFragment();
//                }
//                fragment = messageControlFragment;
                break;
            case R.id.nav_left_second:
//                if (addressBookFragment == null) {
//                    addressBookFragment = new AddressBookFragment();
//                }
//                fragment = addressBookFragment;
                break;
            case R.id.nav_center:
                if (testFragment == null) {
                    testFragment = new TestFragment();
                }
                fragment = testFragment;
                break;
            case R.id.nav_right_first:
//                if (applicationFragment == null) {
//                    applicationFragment = new ApplicationFragment();
//                }
//                fragment = applicationFragment;
                break;
            case R.id.nav_right_second:
//                if (mineFragment == null) {
//                    mineFragment = new MineFragment();
//                }
//                fragment = mineFragment;
                break;
        }
        if (fragment != null) {
            if (mBundle != null) {
                fragment.setArguments(mBundle);
            }
        }
        return fragment;
    }

    public void destory() {
        testFragment = null;
    }

}
