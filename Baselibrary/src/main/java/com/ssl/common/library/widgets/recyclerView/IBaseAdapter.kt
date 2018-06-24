package com.ssl.common.library.widgets.recyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by long
 * on 2017/12/13.
 */
class IBaseAdapter : RecyclerView.Adapter<IViewHolder> {
    private var mData: List<IUnit<*>>?;

    constructor(data: List<IUnit<*>>) : super() {
        mData = data;
    }

    override fun getItemViewType(position: Int): Int {
        return mData?.get(position)?.getType() ?: 0
    }

    override fun onBindViewHolder(holder: IViewHolder?, position: Int) {
        mData?.get(position)?.onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IViewHolder? {
        if (parent != null && mData != null)
            for (index in mData!!.indices) {
                var tem = mData!!.get(index);
                if (viewType == tem.getType()) {
                    var tem = LayoutInflater.from(parent.context).inflate(tem.getLayoutId(), parent, false)
                    return IViewHolder(tem)
                }
            }
        return null;
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.size;
    }
}