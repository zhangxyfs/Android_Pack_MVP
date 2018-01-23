package com.z7dream.apm.base.widget.recycler;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.z7dream.apm.base.utils.tools.Utils;


/**
 * Created by xiaoyu.zhang on 2016/6/23.
 */

public class ErrorHolder extends RecyclerView.ViewHolder {
    public ErrorDataView errorDataView;
    public View parent;
    public Context context;
    public int screenWidth;

    public ErrorHolder(View itemView) {
        super(itemView);
        parent = itemView;
        context = parent.getContext();
        screenWidth = Utils.getScreenWidth(context);
        errorDataView = new ErrorDataView(context, itemView);
    }
}
