package com.z7dream.apm.base.widget.navigation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.internal.BaselineLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z7dream.apm.base.R;
import com.z7dream.apm.base.widget.draggabledot.DotView;
import com.z7dream.apm.base.widget.draggabledot.OnStateChangeListener;

import static com.z7dream.apm.base.widget.navigation.BottomNavigationBar.dp2px;


/**
 * Created by Z7Dream on 2017/9/27 10:01.
 * Email:zhangxyfs@126.com
 */

public class BottomNavigationItemView extends RelativeLayout {
    private FrameLayout iconLayout;
    private View topLine;
    private ImageView mIcon;
    private DotView mDotView;
    private BaselineLayout baselineLayout;
    private TextView mSmallLabel;
    private TextView mLargeLabel;
    private MenuItem mCheckMenuItem, mUnCheckMenuItem;
    private boolean mIsCheck = false;
    private boolean mIsBig = false;
    private boolean mDisplayDot = false;
    private boolean mNowDisplayDot = false;
    private int mBigIconSize = 0;
    private int mCheckTextColor, mUnCheckTextColor;

    public BottomNavigationItemView(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigationItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.nav_bar_item, this);
        iconLayout = (FrameLayout) findViewById(R.id.iconLayout);
        topLine = findViewById(R.id.topLine);
        mIcon = (ImageView) findViewById(R.id.icon);
        mDotView = (DotView) findViewById(R.id.dotView);
        baselineLayout = (BaselineLayout) findViewById(R.id.baselineLayout);
        mSmallLabel = (TextView) findViewById(R.id.smallLabel);
        mLargeLabel = (TextView) findViewById(R.id.largeLabel);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        setLayoutParams(lp);
    }


    public void setCheckData(MenuItem checkData) {
        mCheckMenuItem = checkData;
    }

    public void setUnCheckData(MenuItem unCheckData) {
        mUnCheckMenuItem = unCheckData;
    }

    public void setCheck(boolean isCheck) {
        mIsCheck = isCheck;
        refUI();
    }

    public void setHeight(int height) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
    }

    public void setBigIndex(int index) {
        if (index == Integer.parseInt(getTag().toString())) {
            LayoutParams lp = (LayoutParams) mIcon.getLayoutParams();
            lp.topMargin = dp2px(getContext(), 14);
            lp.width = mBigIconSize;
            lp.height = lp.width;
            lp.removeRule(RelativeLayout.ABOVE);
            mIcon.setLayoutParams(lp);

            mIsBig = true;
        }
    }


    public void setBigIconSize(int px) {
        mBigIconSize = px;
    }

    public void setTextColor(int checkColor, int unCheckColor) {
        mCheckTextColor = checkColor;
        mUnCheckTextColor = unCheckColor;
    }

    public void setDisplayDot(boolean isDisplayDot) {
        mDisplayDot = isDisplayDot;
        mNowDisplayDot = mDisplayDot;
    }

    public void setDotVisable(boolean isVisable) {
        if (!mDisplayDot) return;
        if (mNowDisplayDot == isVisable) return;
        mNowDisplayDot = isVisable;
        int size = mNowDisplayDot ? dp2px(getContext(), 22) : 0;

        LayoutParams lp = (LayoutParams) mDotView.getLayoutParams();
        lp.width = size;
        lp.height = size;
        mDotView.setLayoutParams(lp);

        mDotView.setVisibility(mNowDisplayDot ? VISIBLE : GONE);
        mDotView.postInvalidate();
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mDotView.setOnDotStateChangedListener(onStateChangeListener);
    }

    public int getIndex() {
        int index = -1;
        if (getTag() != null) {
            index = Integer.parseInt(getTag().toString());
        }

        return index;
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resid) {
        if (mIsBig) {
            LayoutParams ilLP = (LayoutParams) iconLayout.getLayoutParams();
            ilLP.topMargin = mBigIconSize / 2;
            iconLayout.setLayoutParams(ilLP);
            iconLayout.setBackgroundResource(resid);

            LayoutParams tLLP = (LayoutParams) topLine.getLayoutParams();
            tLLP.topMargin = ilLP.topMargin - 2;
            topLine.setLayoutParams(tLLP);
        } else
            super.setBackgroundResource(resid);
    }

    private void refUI() {
        if (mCheckMenuItem == null) return;
        mIcon.setImageDrawable(mIsCheck ? mCheckMenuItem.getIcon() : mUnCheckMenuItem == null ? mCheckMenuItem.getIcon() : mUnCheckMenuItem.getIcon());
        mSmallLabel.setText(mIsCheck ? mCheckMenuItem.getTitle() : mUnCheckMenuItem == null ? mCheckMenuItem.getTitle() : mUnCheckMenuItem.getTitle());
        mLargeLabel.setText(mIsCheck ? mCheckMenuItem.getTitle() : mUnCheckMenuItem == null ? mCheckMenuItem.getTitle() : mUnCheckMenuItem.getTitle());
        mSmallLabel.setTextColor(mIsCheck ? mCheckTextColor : mUnCheckTextColor);
        mLargeLabel.setTextColor(mIsCheck ? mCheckTextColor : mUnCheckTextColor);
        if (!mDisplayDot) {
            mDotView.setVisibility(GONE);
        }

        playAnimation();
    }

    private void playAnimation() {
        if (!mIsCheck) return;
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mIcon, "scaleX", 1f, 1.03f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mIcon, "scaleY", 1f, 1.03f, 1f);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSet.start();
    }
}
