package com.ssl.common.library.widgets.recyclerView

/**
 * Created by long
 * on 2017/12/13.
 */
abstract class IUnit<T> {
    var mData: T? = null;

    constructor(mData: T?) {
        this.mData = mData;
    }

    abstract fun getType(): Int;

    abstract fun getLayoutId(): Int;

    abstract fun onBindViewHolder(vh: IViewHolder?, position: Int);

}