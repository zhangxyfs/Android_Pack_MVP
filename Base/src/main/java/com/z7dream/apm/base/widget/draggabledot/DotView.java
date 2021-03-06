package com.z7dream.apm.base.widget.draggabledot;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.z7dream.apm.base.R;


/**
 * Responsible for retrieve the first down motion event,
 * and determine if notify DraggableLayout to intercept the motion event later.
 * <p/>
 * <p/>
 * author:lsxiao
 * date:2015/12/23 19:01
 */
public class DotView extends TextView {
    private DraggableLayout mDraggableLayout;
    private Circle mBgCircle;
    private Paint mPaint;
    private OnEDotStateChangedListener mOnEDotStateChangedListener;

    private float mMaxStretchLength;
    private int mRadius;
    private int mCircleColor;
    private String mContent;
    private float mTextSize;
    private int mContentColor;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DotView,
                0, 0);
        try {
            mRadius = a.getDimensionPixelOffset(R.styleable.DotView_xls_radius, dp2px(10));
            mCircleColor = a.getColor(R.styleable.DotView_xls_circle_color, Color.RED);
            mMaxStretchLength = a.getDimensionPixelOffset(R.styleable.DotView_xls_max_stretch_length, dp2px(80));
            mContent = a.getString(R.styleable.DotView_xls_content);
            mTextSize = a.getDimensionPixelOffset(R.styleable.DotView_xls_text_size, dp2px(16));
            mContentColor = a.getColor(R.styleable.DotView_xls_content_color, Color.WHITE);
        } finally {
            a.recycle();
        }
        init();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * initialize some local variables.
     */
    private void init() {
        setGravity(Gravity.CENTER);
        mPaint = new Paint();
        //画笔无锯齿
        mPaint.setAntiAlias(true);
        //文字居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        //粗体
//        Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
//        mPaint.setTypeface(font);

        mBgCircle = new Circle(mRadius, mRadius, mRadius);
    }

    /**
     * to find the draggableLayout instance layout in the view tree.
     *
     * @return null, if don't find,else return DraggableLayout instance.
     */
    public DraggableLayout findDraggableLayout() {
        Activity activity = (Activity) getContext();
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        for (int i = 0; i < decorView.getChildCount(); i++) {
            View view = decorView.getChildAt(i);
            if (view == null) {
                break;
            }
            if (view instanceof DraggableLayout) {
                return (DraggableLayout) view;
            }
        }
        return null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //if is preview mode,then don't need draggableLayout instance.
        if (isInEditMode()) {
            return;
        }

        //find the draggable instance.
        if (mDraggableLayout == null) {
            mDraggableLayout = findDraggableLayout();
            if (mDraggableLayout == null) {
                setEnabled(false);
//                throw new IllegalArgumentException("the draggableLayout isn't be attached to view tree,you must invoke the attachToActivity method of DraggableLayout");
            }
        }
        setEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSpec = MeasureSpec.makeMeasureSpec(mRadius * 2, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureSpec, measureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawText(canvas);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        mBgCircle.draw(canvas, mPaint);
    }

    private void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(mContent)) {
            return;
        }
        mPaint.setColor(mContentColor);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        //((Paint.descent() + Paint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText(mContent, xPos, yPos, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && isEnabled() && mDraggableLayout != null) {
            mDraggableLayout.preDrawDrag(this, ev);
            mDraggableLayout.setCanIntercept(true);
            return true;
        }
        return false;
    }

    public float getMaxStretchLength() {
        return mMaxStretchLength;
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public OnEDotStateChangedListener getOnDotStateChangedListener() {
        return mOnEDotStateChangedListener;
    }

    public void setOnDotStateChangedListener(OnEDotStateChangedListener OnEDotStateChangedListener) {
        mOnEDotStateChangedListener = OnEDotStateChangedListener;
    }

    /**
     * the callback interface.
     */
    public interface OnEDotStateChangedListener {
        void onStretch(DotView dotView);

        void onDrag(DotView dotView);

        void onDismissed(DotView dotView);
    }

    /**
     * the simple callback implement.
     */
    public static class SimpleDotStateChangedListener implements OnEDotStateChangedListener {
        @Override
        public void onStretch(DotView dotView) {
            //do nothing.
        }

        @Override
        public void onDrag(DotView dotView) {
            //do nothing.
        }

        @Override
        public void onDismissed(DotView dotView) {
            //do nothing.
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDraggableLayout = null;
        mOnEDotStateChangedListener = null;
    }

    public void setContentColor(int contentColor) {
        mContentColor = contentColor;
        postInvalidate();
    }

    public void setMaxStretchLength(float maxStretchLength) {
        mMaxStretchLength = maxStretchLength;
        postInvalidate();
    }

    public void setRadius(int radius) {
        mRadius = radius;
        postInvalidate();
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        postInvalidate();
    }

    public void setContent(String s) {
        mContent = s;
        postInvalidate();
    }

    public String getContent(){
        return mContent;
    }
}
