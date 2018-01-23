package com.z7dream.apm.base.widget.recycler;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z7dream.apm.base.R;
import com.z7dream.apm.base.utils.tools.Utils;

/**
 * Created by win8 -1 on 2015/9/1.
 */
public class ErrorDataView {
    private final String TAG = "ErrorDataView";
    private View parent;
    private Context mContext;
    private int size;
    private LinearLayout noDataLinearLayout;
    private ImageView noDataIv;
    private TextView noDataTv;

    private OnErrorDataListener mOnErrorDataListener;

    public static final int ERROR_NET = 0x010;
    public static final int ERROR_NODATA = 0x020;
    public static final int ERROR_NODRAFT = 0x030;
    public static final int ERROR_NONE = 0x0100;

    public static final int ITEM_FULL = 1;
    public static final int ITEM_WRAP = 2;

    public ErrorDataView(Context context) {
        mContext = context;
        parent = LayoutInflater.from(context).inflate(R.layout.layout_base_no_data, null);
        init();
        data();
    }

    public ErrorDataView(Context context, View view) {
        mContext = context;
        parent = view;
        init();
        data();
    }

    private void init() {
        noDataLinearLayout =  parent.findViewById(R.id.noDataLinearLayout);
        noDataIv = parent.findViewById(R.id.noDataIv);
        noDataTv = parent.findViewById(R.id.noDataTv);
        size = Utils.convertDipOrPx(mContext, 150);
    }

    private void data() {
        noDataLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnErrorDataListener == null) {
                    Log.e(TAG, "请实现OnErrorDataListener");
                    return;
                }
                mOnErrorDataListener.errorDataClickListener();
            }
        });
        noDataIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnErrorDataListener == null) {
                    Log.e(TAG, "请实现OnErrorDataListener");
                    return;
                }
                mOnErrorDataListener.errorDataClickListener();
            }
        });
    }

    public void setErrorImgSize(int size) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        noDataIv.setLayoutParams(lp);
    }

    public void setData(int which) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        noDataIv.setLayoutParams(lp);
        noDataIv.setVisibility(View.VISIBLE);

        switch (which) {
            case ERROR_NET:
                noDataIv.setImageResource(R.drawable.ic_no_network);
                noDataTv.setText(R.string.no_net_str);
                break;
            case ERROR_NODATA:
                noDataIv.setImageResource(R.drawable.ic_no_data);
                noDataTv.setText(R.string.no_data_str);
                break;
            case ERROR_NODRAFT:
                noDataIv.setVisibility(View.INVISIBLE);
                noDataTv.setText(R.string.no_prepare_str);
                break;


        }
    }

    public void setData(int which, int px) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (px > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = px;
            lp.height = px;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        switch (which) {
            case ERROR_NET:
                noDataIv.setVisibility(View.VISIBLE);
                noDataIv.setImageResource(R.drawable.ic_no_network);
                noDataTv.setText(R.string.no_net_str);
                break;
            case ERROR_NODATA:
                noDataIv.setVisibility(View.VISIBLE);
                noDataIv.setImageResource(R.drawable.ic_no_data);
                noDataTv.setText(R.string.no_data_str);
                break;

        }
    }

    public void setData(int pxSize, int imageResId, String text, int itemSize) {
        Context context = noDataLinearLayout.getContext();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        if (itemSize == ITEM_FULL) {
            layoutParams.width = Utils.getScreenWidth(context);
            layoutParams.height = Utils.getScreenHeight(context) - Utils.convertDipOrPx(context, 44) - Utils.getNavigationBarHeight(context);
        }
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (pxSize > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = pxSize;
            lp.height = pxSize;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        if (imageResId > 0) {
            noDataIv.setImageResource(imageResId);
        }
        if (imageResId == 0) {
            noDataIv.setVisibility(View.INVISIBLE);
        }
        if (text != null) {
            noDataTv.setText(text);
            noDataTv.setVisibility(View.VISIBLE);
        }
    }

    public void setData(int pxSize, int imageResId, String text) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (pxSize > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = pxSize;
            lp.height = pxSize;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        if (imageResId > 0) {
            noDataIv.setImageResource(imageResId);
        } else {
            noDataIv.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(text)) {
            noDataTv.setText(text);
            noDataTv.setVisibility(View.VISIBLE);
        }

    }

    public void setData(int marginTop, int pxSize, int imageResId, String text) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, Utils.convertDipOrPx(mContext, marginTop), 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (pxSize > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = pxSize;
            lp.height = pxSize;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        if (imageResId > 0) {
            noDataIv.setImageResource(imageResId);
        } else {
            noDataIv.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(text)) {
            noDataTv.setText(text);
        }
    }

    public View getErrorDataView() {
        return parent;
    }


    public void destory() {
        mContext = null;
        parent = null;
    }

    public void setOnErrorDataListener(OnErrorDataListener onErrorDataListener) {
        mOnErrorDataListener = onErrorDataListener;
    }

    public interface OnErrorDataListener {
        void errorDataClickListener();
    }

}






















