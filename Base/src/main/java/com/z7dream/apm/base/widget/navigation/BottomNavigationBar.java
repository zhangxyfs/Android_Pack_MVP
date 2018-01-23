package com.z7dream.apm.base.widget.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.z7dream.apm.base.R;
import com.z7dream.apm.base.widget.ViewClickObservable;
import com.z7dream.apm.base.widget.draggabledot.DotView;
import com.z7dream.apm.base.widget.draggabledot.OnStateChangeListener;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by Z7Dream on 2017/9/27 10:01.
 * Email:zhangxyfs@126.com
 */

public class BottomNavigationBar extends LinearLayout {
    private Pools.Pool<BottomNavigationItemView> mItemPool = new Pools.SynchronizedPool<>(5);
    private BottomNavigationItemView[] mButtons;
    private int mCheckMenuId, mUnCheckMenuId;
    private int mHeight = 0;
    private int mBigIndex = -1;
    private int mBigIconSize = 0;
    private int mItemBackgroundResId;
    private int mCheckTextColor, mUnCheckTextColor;
    private Integer[] dotIndexs;
    private OnCheckedItemListener mOnCheckedItemListener;
    private OnDotNumClearListener mOnDotNumClearListener;
    private int doubleClickTime = 100;


    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.bottom_navigation);
        if (a.hasValue(R.styleable.bottom_navigation_check_menu)) {
            mCheckMenuId = a.getResourceId(R.styleable.bottom_navigation_check_menu, 0);
        } else {
            throw new RuntimeException("not has check_menu.");
        }
        if (a.hasValue(R.styleable.bottom_navigation_uncheck_menu)) {
            mUnCheckMenuId = a.getResourceId(R.styleable.bottom_navigation_uncheck_menu, 0);
        }
        if (a.hasValue(R.styleable.bottom_navigation_height)) {
            mHeight = a.getDimensionPixelSize(R.styleable.bottom_navigation_height, 0);
        } else {
            mHeight = dp2px(context, 64);
        }
        if (a.hasValue(R.styleable.bottom_navigation_increaseIndex)) {
            mBigIndex = a.getInteger(R.styleable.bottom_navigation_increaseIndex, -1);
        }
        if (a.hasValue(R.styleable.bottom_navigation_itemBackground)) {
            mItemBackgroundResId = a.getResourceId(R.styleable.bottom_navigation_itemBackground, 0);
        }
        if (a.hasValue(R.styleable.bottom_navigation_increaseIconSize)) {
            mBigIconSize = a.getDimensionPixelSize(R.styleable.bottom_navigation_increaseIconSize, 0);
        }
        if (a.hasValue(R.styleable.bottom_navigation_checkTextColor)) {
            mCheckTextColor = a.getColor(R.styleable.bottom_navigation_checkTextColor, 0);
        } else {
            mCheckTextColor = getResources().getColor(android.R.color.tertiary_text_dark);
        }
        if (a.hasValue(R.styleable.bottom_navigation_unCheckTextColor)) {
            mUnCheckTextColor = a.getColor(R.styleable.bottom_navigation_unCheckTextColor, 0);
        } else {
            mUnCheckTextColor = getResources().getColor(android.R.color.tertiary_text_dark);
        }
        if (a.hasValue(R.styleable.bottom_navigation_dotIndex)) {
            String dotIndexStr = a.getString(R.styleable.bottom_navigation_dotIndex);
            if (dotIndexStr != null) {
                String[] dots = dotIndexStr.split(",");
                dotIndexs = new Integer[dots.length];
                for (int i = 0; i < dots.length; i++) {
                    dotIndexs[i] = Integer.parseInt(dots[i]);

                }
            }
        }
        a.recycle();
        initView(context);
        buildMenuView();
    }

    private void initView(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        if (mBigIndex > -1) {
            setPadding(0, dp2px(context, 8), 0, 0);
            setClipChildren(false);
        }
    }

    @SuppressLint("RestrictedApi")
    public void buildMenuView() {
        removeAllViews();

        if (mButtons != null) {
            for (BottomNavigationItemView item : mButtons) {
                mItemPool.release(item);
            }
        }

        if (mCheckMenuId > 0) {
             MenuBuilder checkMenuBuilder = new MenuBuilder(getContext());
            new SupportMenuInflater(getContext()).inflate(mCheckMenuId, checkMenuBuilder);
            mButtons = new BottomNavigationItemView[checkMenuBuilder.size()];

            for (int i = 0; i < checkMenuBuilder.size(); i++) {
                MenuItem item = checkMenuBuilder.getItem(i);
                BottomNavigationItemView child = getNewItem();
                child.setCheckData(item);
                child.setHeight(mBigIndex == i ? mHeight - dp2px(getContext(), 32) + mBigIconSize : mHeight);
                child.setBigIconSize(mBigIndex == i ? mBigIconSize : 0);
                child.setTag(i);
                child.setId(item.getItemId());
                child.setBigIndex(mBigIndex);
                if (mItemBackgroundResId > 0)
                    child.setBackgroundResource(mItemBackgroundResId);
                child.setTextColor(mCheckTextColor, mUnCheckTextColor);
                mButtons[i] = child;
            }
        }

        if (mUnCheckMenuId > 0 && mButtons != null) {
            MenuBuilder unCheckMenuBuilder = new MenuBuilder(getContext());
            new SupportMenuInflater(getContext()).inflate(mUnCheckMenuId, unCheckMenuBuilder);

            if (mButtons.length == unCheckMenuBuilder.size()) {
                for (int i = 0; i < unCheckMenuBuilder.size(); i++) {
                    MenuItem item = unCheckMenuBuilder.getItem(i);
                    mButtons[i].setUnCheckData(item);
                }
            }
        }

        if (mButtons != null) {
            int needCheckIndex = 0;
            if (mButtons.length % 2 == 1) {
                needCheckIndex = (mButtons.length - 1) / 2;
            }

            for (int i = 0; i < mButtons.length; i++) {
                for (Integer dotIndex : dotIndexs) {
                    boolean isDisplayDot = i == dotIndex;
                    mButtons[i].setDisplayDot(isDisplayDot);
                    if (isDisplayDot) {
                        mButtons[i].setOnStateChangeListener(new OnStateChangeListener() {
                            @Override
                            public void onDismissed(DotView dotView) {
                                super.onDismissed(dotView);
                                if (dotView.getParent() instanceof BottomNavigationItemView) {
                                    int index = ((BottomNavigationItemView) dotView.getParent()).getIndex();
                                    if (mOnDotNumClearListener != null)
                                        mOnDotNumClearListener.dotNumClear(index);
                                }
                            }
                        });
                    }
                }

                Observable<Object> observable = new ViewClickObservable(mButtons[i]).share();
                observable.buffer(observable.debounce(doubleClickTime, TimeUnit.MILLISECONDS))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(voids -> {
                            int index = -1;
                            if (!TextUtils.isEmpty(voids.get(0).toString())) {
                                index = isNumber(voids.get(0).toString()) ? Integer.parseInt(voids.get(0).toString()) : -1;
                            }
                            if (index > -1) {
                                if (voids.size() > 2) {
                                    //在当前页面双击刷新该页面
                                    if (mOnCheckedItemListener != null)
                                        mOnCheckedItemListener.doubleClickItem(index, mButtons[index].getId());
                                } else {
                                    if (mOnCheckedItemListener != null)
                                        mOnCheckedItemListener.checkItem(index, mButtons[index].getId());
                                    for (int i1 = 0; i1 < mButtons.length; i1++) {
                                        mButtons[i1].setCheck(i1 == index);
                                    }
                                }
                            }
                        }, error -> {
                        });


                mButtons[i].setCheck(i == needCheckIndex);

                addView(mButtons[i]);
            }
            setWeightSum(mButtons.length);
        }
    }

    /**
     * 点击某个按钮
     *
     * @param position
     */
    public void setCheckButton(int position) {
        if (mButtons == null || position < 0 || position > mButtons.length) return;
        for (int i1 = 0; i1 < mButtons.length; i1++) {
            mButtons[i1].setCheck(i1 == position);
        }
        if (mOnCheckedItemListener != null)
            mOnCheckedItemListener.checkItem(position, mButtons[position].getId());
    }


    /**
     * @param index
     * @param isVisable
     */
    public void setDotVisable(int index, boolean isVisable) {
        if (mButtons == null || index < 0 || index > mButtons.length) return;
        mButtons[index].setDotVisable(isVisable);
    }


    private BottomNavigationItemView getNewItem() {
        BottomNavigationItemView item = mItemPool.acquire();
        if (item == null) {
            item = new BottomNavigationItemView(getContext());
        }
        return item;
    }

    public void setOnCheckedItemListener(OnCheckedItemListener onCheckedItemListener) {
        mOnCheckedItemListener = onCheckedItemListener;
    }

    public void setOnDotNumClearListener(OnDotNumClearListener onDotNumClearListener) {
        mOnDotNumClearListener = onDotNumClearListener;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public interface OnCheckedItemListener {
        void checkItem(int position, int resId);

        void doubleClickItem(int position, int resId);
    }

    public interface OnDotNumClearListener {
        void dotNumClear(int index);
    }
}
