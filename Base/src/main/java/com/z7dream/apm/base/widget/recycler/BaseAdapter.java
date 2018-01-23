package com.z7dream.apm.base.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.z7dream.apm.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/6/28.
 */
public abstract class BaseAdapter<MODEL extends BaseModel, LISTENER, HOLDER extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<HOLDER> {

    protected List<MODEL> list;
    protected LISTENER listener;

    public BaseAdapter(LISTENER listener) {
        list = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }


    protected RecyclerView.ViewHolder onCreateErrorViewHolder(ViewGroup parent) {
        return new ErrorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_base_no_data, null));
    }

    protected void onBindErrorHolder(MODEL model, HOLDER holder) {
        if (holder instanceof ErrorHolder) {
            ErrorHolder eh = (ErrorHolder) holder;
            ViewGroup.LayoutParams parentLP;

            if (list.size() > (model.isHasHead ? 2 : 1)) {
                parentLP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, eh.screenWidth * 2 / 3);
            } else {
                parentLP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            eh.parent.setLayoutParams(parentLP);

            if (model.isError) {
                eh.errorDataView.setData(model.noDataIvSize, model.noDataIvResId, model.noDataTvStr, model.itemSize);
            } else {
                eh.errorDataView.setData(model.errorStatus);
            }

            eh.errorDataView.setOnErrorDataListener(new ErrorDataView.OnErrorDataListener() {
                @Override
                public void errorDataClickListener() {
                    //listener.onErrorClickListener();
                }
            });

        }
    }

    public List<MODEL> getList() {
        return list;
    }

    public void appendToList(List<MODEL> dataList) {
        if (dataList.size() == 0) {
            return;
        }
        int oldListSize = this.list.size();
        this.list.addAll(dataList);
        notifyItemRangeInserted(oldListSize, this.list.size());
    }

    public void appendToFirst(List<MODEL> dataList) {
        if (dataList.size() == 0) {
            return;
        }
        list.addAll(0, dataList);
        notifyItemRangeInserted(0, dataList.size());
    }

    /**
     * 完全刷新
     *
     * @param dataList
     */
    public void refAllData(List<MODEL> dataList) {
        if (dataList.size() == 0) {
            return;
        }
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void refAllData(List<MODEL> dataList, boolean noCheckNoData) {
        if (!noCheckNoData)
            if (dataList.size() == 0) {
                return;
            }
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addOne(int position, MODEL model) {
        list.add(position, model);
        notifyItemInserted(position);
    }

    public void addOne(MODEL model) {
        list.add(model);
        notifyItemInserted(list.size() - 1);
    }

    public void refOne(MODEL model, int pos) {
        if (list.size() > 0) {
            list.set(pos, model);
            notifyItemChanged(pos);
        } else {
            list.add(model);
            notifyDataSetChanged();
        }
    }

    /**
     * @param model
     * @param errorMinNum 可以显示错误提示的最小列表数量
     */
    public void appendError(MODEL model, int errorMinNum) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type == BaseModel.ERROR) {
                list.remove(i);
            }
        }
        if (list.size() == errorMinNum) {
            list.add(model);
            notifyDataSetChanged();
        }
    }

    public boolean isHasError() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type == BaseModel.ERROR) {
                return true;
            }
        }
        return false;
    }

    public boolean isHas(int type) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type == type) {
                return true;
            }
        }
        return false;
    }

    public MODEL getError() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type == MODEL.ERROR) {
                return list.get(i);
            }
        }
        return null;
    }

    public void removeError() {
        int pos = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type == BaseModel.ERROR) {
                list.remove(i);
                pos = i;
            }
        }
        if (pos > -1) {
            notifyItemRemoved(pos);
        }
    }

    public void delOne(int position) {
        if (list.size() > position) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearAll() {
        if (list.size() > 0) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (list.size() > 1) {
            MODEL model = list.get(0);
            list.clear();
            list.add(model);
            notifyDataSetChanged();
        }
    }

    /**
     * 只删除数据不做刷新
     */
    public void deleteAllData() {
        if (list.size() > 0) {
            list.clear();
        }
    }

    public void reset() {
        list.clear();
        notifyDataSetChanged();
    }


}