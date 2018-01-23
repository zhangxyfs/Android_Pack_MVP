package com.z7dream.apm.base.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.z7dream.apm.base.utils.tools.Utils;

/**
 * Created by xiaoyu.zhang on 2016/6/28.
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder {

    public Context context;
    public int screenWidth;
    public int photoSize;

    public BaseHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        screenWidth = Utils.getScreenWidth(context);
    }
}
